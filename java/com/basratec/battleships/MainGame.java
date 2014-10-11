package com.basratec.battleships;

import android.app.Activity;
import android.content.Intent;
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

    protected String[] callables = {"firedAt", "playResult", "play", "end"};

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
        final String cell = view.getTag().toString();
        int position = Integer.parseInt(cell);
        System.out.println("fn: fireAt. position: "+position);

        if(!PLAY_FLAG || GridMap.STATUS_UNKNOWN != enemyGridMap.getCellStatus(position)){
            System.out.println("will not fire, play_flag= "+PLAY_FLAG+" and cell status= "+enemyGridMap.getCellStatus(position));
            return; //not my turn or cell already shot before
        }

        lastFiredAtCell = Integer.parseInt(cell);

        System.out.println("finishing fn: fireAt (running a new thread)");
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
        System.out.println("fn: firedAt");
        int cellPos = Integer.parseInt(data);
        gridMap.shoot(cellPos);
        try{
            ImageButton cell ;
            int horizontalNumber = (int)(cellPos/5);
            LinearLayout ll1 = (LinearLayout)findViewById(R.id.MyGrid);
            LinearLayout ll2 = (LinearLayout)ll1.getChildAt(horizontalNumber);
            cell = (ImageButton)ll2.getChildAt(cellPos%5);

            if(gridMap.isOccupied(cellPos)){ //if we're hit
                cell.setBackgroundResource(R.drawable.kill);
            }
            else{
                cell.setBackgroundResource(R.drawable.miss);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("finished fn: firedAt");
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
                cell.setBackgroundResource(R.drawable.broken);
            }
            else{
                cell.setBackgroundResource(R.drawable.miss_sea);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        enemyGridContainer.setBackgroundColor(Color.parseColor("#CCCCCC"));
        myGridContainer.setBackground(getResources().getDrawable(R.drawable.border));
        turnNotifier.setText("Waiting for other player..");
        System.out.println("finished fn: playResult");
    }

    public void end(String data)
    {
        boolean won = Boolean.parseBoolean(data);
        //change all the colors

        //wait for a couple of seconds

        //go to the end activity
        Intent eG = new Intent(getApplicationContext(),EndGame.class);
        eG.putExtra("result", won);
        startActivity(eG);
    }

	public void play(String data)
    {
        System.out.println("started fn: play");
		PLAY_FLAG = true;
        enemyGridContainer.setBackground(getResources().getDrawable(R.drawable.enemy_border));
        myGridContainer.setBackgroundColor(Color.parseColor("#CCCCCC"));
        turnNotifier.setText("Your Turn!");
        System.out.println("finished fn: play");
	}
}