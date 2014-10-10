package com.basratec.battleships;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PreGame extends AAPIableActivity {
    private ConnectionManager connectionListener;
    private Socket connection;
    private int[] gridMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private final int NUMBER_OF_SHIPS=6;
    private  boolean listIsEmpty;
    private TextView timer;
    private Vector<Boolean> shipsStatus;
    private LinearLayout shipContainer;
    private PreGame that = this;


    private void initializeShips(){
        shipsStatus = new Vector<Boolean>(NUMBER_OF_SHIPS);

        for(int i =0;i<NUMBER_OF_SHIPS;++i){
            ImageButton ship = new ImageButton(getApplicationContext());
            shipContainer.addView(ship);
            ship =(ImageButton) shipContainer.getChildAt(i);
            ship.setLayoutParams(new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.ship),(int)getResources().getDimension(R.dimen.ship)));
            ((LinearLayout.LayoutParams)ship.getLayoutParams()).setMargins(10, 10, 10, 10);
            ship.setBackgroundResource(R.drawable.ship);

            shipsStatus.add(new Boolean("true"));
        }
        shipContainer.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectionListener = ConnectionManager.getListener(that);
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
        int x = Integer.parseInt(cell);
        listIsEmpty=true;
        if (gridMap[x] == 1){
            System.out.println("Ship already placed!");
            //TODo add code to free a ship
            return;
        }
        System.out.println("looping something...");
        for(int i=0;i<shipsStatus.size();++i){
            if(shipsStatus.elementAt(i).booleanValue()){ // if a ship is still not clicked
                System.out.println("Ship found!");
                gridMap[x] = 1;
                ImageButton ship= (ImageButton)shipContainer.getChildAt(i);
                ship.setAlpha(.5f);
                shipsStatus.set(i,Boolean.FALSE);
                imgView.setImageDrawable(getResources().getDrawable(
                        R.drawable.ship));
                listIsEmpty=false;
                System.out.println("breaking...");
                break;

            }
        }
        if(listIsEmpty){
            System.out.println("list is empty!");
            //toast
        }
        else{
            System.out.println("list is not empty!");
        }

    }


    /**
     * This function should be called to start the main game
     */
    public void start(String data){
        Intent mainGame = new Intent(getApplicationContext(),MainGame.class);
        mainGame.putExtra("gridMap", gridMap);
//        connectionListener.stopListening();
        startActivity(mainGame);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void eshtaHandler(View view){
        System.out.println("Handling eshta!");
        if(listIsEmpty){
            System.out.println("List is empty, continuing...");
            //send grid to server  (JSONArray) is promising but require Android API 19 or more we need a decision
            try {
                final String grid = new JSONArray(gridMap).toString();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        ConnectionManager manager = ConnectionManager.getSender(that);
                        manager.init();
                        manager.send("{\"event\":\"ready\",\"grid\":"+grid+"}");
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
