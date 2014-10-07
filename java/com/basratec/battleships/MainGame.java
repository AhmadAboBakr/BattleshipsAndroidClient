package com.basratec.battleships;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.basratec.battleships.Helpers.Generators;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by nookz on 9/27/2014.
 *
 */

public class MainGame extends AAPIableActivity {

	private boolean PLAY_FLAG;

    private LinearLayout myGridContainer;

    private LinearLayout enemyGridContainer;

    protected MainGame mainGame = this;

    protected ConnectionManager connectionListener;

    protected String[] callables = {"firedAt", "playResult", "play"};

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
        OnCellClickListener ocl = new OnCellClickListener();
        myGridContainer = (LinearLayout) findViewById(R.id.MyGrid);
        myGridContainer = Generators.addGridToContainer(5, 5, this, R.dimen.small_cell, R.dimen.small_cell, myGridContainer);

        enemyGridContainer = (LinearLayout) findViewById(R.id.EnemyGrid);
        enemyGridContainer = Generators.addClickableGridToContainer(
                5, 5, this, R.dimen.cell, R.dimen.cell, enemyGridContainer, ocl
        );

        connectionListener = new ConnectionManager(mainGame);
        connectionListener.start();
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

        new Thread(new Runnable(){
            @Override
            public void run() {
                ConnectionManager manager = new ConnectionManager(that);
                manager.init();
                manager.send("{\"event\":\"fireAt\",\"position\":"+cell+"}");
                PLAY_FLAG = false;
            }
        }).start();
    }

    public void firedAt(int position)
    {
//        if(hit){
//            cell.setBackgroundColor(Color.parseColor("#55f"));
//        }
//        else{
//            cell.setBackgroundColor(Color.parseColor("#ff5"));
//        }
    }

    public void playResult(int cellPosition, boolean hit)
    {
        ImageButton cell = (ImageButton)findViewById(cellPosition);
        if(hit){
            cell.setBackgroundColor(Color.parseColor("#55f"));
        }
        else{
            cell.setBackgroundColor(Color.parseColor("#ff5"));
        }
        enemyGridContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
        myGridContainer.setBackground(getResources().getDrawable(R.drawable.border));
    }

	public void play(String data)
    {
		PLAY_FLAG = true;
        enemyGridContainer.setBackground(getResources().getDrawable(R.drawable.enemy_border));
        myGridContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
	}
}