package com.basratec.battleships;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basratec.battleships.Helpers.Generators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nookz on 9/27/2014.
 *
 */

public class MainGame extends AAPIableActivity {

	private boolean PLAY_FLAG;

    private LinearLayout myGridContainer;

    private LinearLayout enemyGridContainer;

    private TextView turnNotifier;

    protected MainGame mainGame = this;

    protected ConnectionManager connectionListener;

    protected String[] callables = {"firedAt", "playResult", "play"};

    protected int lastFiredAtCell;

    protected GridMap gridMap;

    protected GridMap enemyGridMap = new GridMap();

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
        OnCellClickListener ocl = new OnCellClickListener();

        gridMap = (GridMap)getIntent().getSerializableExtra("gridMap");
        if(null == gridMap){
            gridMap = new GridMap();
        }
        myGridContainer = (LinearLayout) findViewById(R.id.MyGrid);
        myGridContainer = Generators.addGridToContainer(5, 5, this, R.dimen.small_cell, R.dimen.small_cell, myGridContainer, gridMap);

        enemyGridContainer = (LinearLayout) findViewById(R.id.EnemyGrid);
        enemyGridContainer = Generators.addClickableGridToContainer(
                5, 5, this, R.dimen.cell, R.dimen.cell, enemyGridContainer, ocl
        );
        turnNotifier = (TextView)findViewById(R.id.turn_notifier);
        connectionListener = ConnectionManager.getListener(this);
	}

    public class OnCellClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            mainGame.fireAt(view);
        }
    }

    public void fireAt(View view)
    {
        if(!PLAY_FLAG){ //not my turn
            return;
        }
        final String cell = view.getTag().toString();
        lastFiredAtCell = Integer.parseInt(cell);

        new Thread(new Runnable(){
            @Override
            public void run() {
                ConnectionManager manager = ConnectionManager.getSender(mainGame);
                manager.init();
                manager.send("{\"event\":\"fireAt\",\"position\":"+cell+"}");
                PLAY_FLAG = false;
            }
        }).start();
    }

    public void firedAt(String data)
    {
        int cellPos = Integer.parseInt(data);
        gridMap.shoot(cellPos);
        try{
            ImageButton cell ;
            int horizontalNumber = (int)(cellPos/5);
            LinearLayout ll1 = (LinearLayout)findViewById(R.id.MyGrid);
            LinearLayout ll2 = (LinearLayout)ll1.getChildAt(horizontalNumber);
            cell = (ImageButton)ll2.getChildAt(cellPos%5);

            if(gridMap.isOccupied(cellPos)){ //if we're hit
                cell.setBackgroundColor(Color.parseColor("#5555FF"));
            }
            else{
                cell.setBackgroundColor(Color.parseColor("#FFFF55"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playResult(String data)
    {
        System.out.println("play result: "+data);
        try{
            int cellPosition = lastFiredAtCell;
            boolean hit = Boolean.parseBoolean(data);
            enemyGridMap.shoot(cellPosition, hit);
            ImageButton cell ;
            int horizontalNumber = (int)(cellPosition/5);
            LinearLayout ll1 = (LinearLayout)findViewById(R.id.EnemyGrid);
            LinearLayout ll2 = (LinearLayout)ll1.getChildAt(horizontalNumber);
            cell = (ImageButton)ll2.getChildAt(cellPosition%5);
            System.out.println(cell.getClass());
            if(hit){
                cell.setBackgroundColor(Color.parseColor("#5555FF"));
            }
            else{
                cell.setBackgroundColor(Color.parseColor("#FFFF55"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        enemyGridContainer.setBackgroundColor(Color.parseColor("#CCCCCC"));
        myGridContainer.setBackground(getResources().getDrawable(R.drawable.border));
        turnNotifier.setText("Waiting for other player..");
    }

	public void play(String data)
    {
		PLAY_FLAG = true;
        enemyGridContainer.setBackground(getResources().getDrawable(R.drawable.enemy_border));
        myGridContainer.setBackgroundColor(Color.parseColor("#CCCCCC"));
        turnNotifier.setText("Your Turn!");
	}
}