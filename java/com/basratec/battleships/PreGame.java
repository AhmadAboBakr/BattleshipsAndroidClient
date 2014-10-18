package com.basratec.battleships;

import java.net.Socket;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basratec.battleships.Helpers.TimeHelper;
import com.basratec.battleships.Managers.ConnectionManager;
import com.basratec.battleships.Managers.ConnectionManagerHFactory;
import com.basratec.battleships.Ships.BaseShip;
import com.basratec.battleships.Ships.TinyShip;

import org.json.JSONArray;
import org.json.JSONException;

public class PreGame extends AAPIableActivity {
    private ConnectionManager connectionManager;
    private Socket connection;
    private GridMap gridMap = new GridMap();
    private final int NUMBER_OF_SHIPS=6;
    private TextView timer;
    private Stack<BaseShip> startingShips = new Stack<BaseShip>();
    private boolean[][] availableShips = new boolean[4][10];
    private LinearLayout shipContainer;
    private PreGame that = this;


    private void initializeShips(){
        for(int i =0;i<NUMBER_OF_SHIPS;++i){
            startingShips.push(new TinyShip());
            ImageButton ship = new ImageButton(getApplicationContext());
            shipContainer.addView(ship);
            ship =(ImageButton) shipContainer.getChildAt(i);
            ship.setLayoutParams(new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.ship),(int)getResources().getDimension(R.dimen.ship)));
            ((LinearLayout.LayoutParams)ship.getLayoutParams()).setMargins(10, 10, 10, 10);
            ship.setBackgroundResource(R.drawable.ship);
        }
        shipContainer.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectionManager = ConnectionManagerHFactory.currentGame(this);
        setContentView(R.layout.activity_pre_game);
        timer = (TextView)findViewById(R.id.timer);
        shipContainer = (LinearLayout) findViewById(R.id.shipContainer);
        initializeShips();
        try {
            TimeHelper.setTimeOut(1000, that, this.getClass().getMethod("updateTimer", null));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu., menu);
        return true;
    }

    // usr clicked color
    public void placeShip(View view) {
        ImageButton imgView = (ImageButton) view;
        String cell = view.getTag().toString();
        int position = Integer.parseInt(cell);
        if (gridMap.isOccupied(position)){
            //TODo add code to free a ship
            return;
        }
        if(startingShips.isEmpty()){
            return;
        }
        BaseShip ship = startingShips.pop();
        ship.occupiedTiles.push(position);
        gridMap.placeShip(ship);
        imgView.setAlpha(.5f);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.intact));
    }


    /**
     * This function should be called to start the main game
     */
    public void start(String data){
        Intent mainGame = new Intent(getApplicationContext(),MainGame.class);
        mainGame.putExtra("gridMap", gridMap);
        startActivity(mainGame);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void eshtaHandler(View view){
        System.out.println("Handling eshta!");
        if(startingShips.isEmpty()){
            //send grid to server  (JSONArray) is promising but require Android API 19 or more we need a decision
            try {
                final String grid = new JSONArray(gridMap.gridMap).toString();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        connectionManager.send("{\"event\":\"ready\",\"grid\":"+grid+"}");
                    }
                }).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void updateTimer(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.setText("00:" + (Integer.parseInt(String.valueOf(timer.getText()).split(":")[1])-1)); //counter is reduced by one
            }
        });

        if(Integer.parseInt(String.valueOf(timer.getText()).split(":")[1])==0)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Time ran out", Toast.LENGTH_LONG).show();
                }
            });
        else {
            try {
                TimeHelper.setTimeOut(1000, that,this.getClass().getMethod("updateTimer",null));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }
}
