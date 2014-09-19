package com.basratec.battleships;

import java.util.ArrayList;
import java.util.Vector;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreGame extends Activity {
    private ArrayList<String> ARRV = new ArrayList<String>(25);
    private int[] gridMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private final int NUMBER_OF_SHIPS=6;
    private TextView timer;
    private Vector<Boolean> shipsStatus;
    private LinearLayout shipContainer;
    private void initilizeShips(){
        shipsStatus = new Vector< Boolean>(NUMBER_OF_SHIPS);
        for(int i =0;i<NUMBER_OF_SHIPS;++i){
            ImageButton ship = new ImageButton(getApplicationContext());
            ship.setBackgroundColor(Color.parseColor("#99aa99"));
            ship.setBackground(Drawable.createFromPath("drawable/ship.png"));
            ship.setLayoutParams(new ViewGroup.LayoutParams(40,40));
            shipContainer.addView(ship);
            shipsStatus.add(new Boolean("true"));
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        timer = (TextView)findViewById(R.id.timer);
        shipContainer = (LinearLayout) findViewById(R.id.shipContainer);
        //shipContainer.invalidate();
        initilizeShips();
        /*
        Thread timerThread = new Thread(new Runnable(){

            @Override
            public void run() {
                int counter=15;
                while(counter>0){
                    counter--;
                    System.out.println("test");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText("00:" + (Integer.parseInt(String.valueOf(timer.getText()).split(":")[1])-1)); //counter is reduced by one
                        }
                    });
                }
                Toast.makeText(getApplicationContext(),"Time ran out",Toast.LENGTH_LONG).show();
            }

        });
        timerThread.start();
        */


        // get drawing view
        // instantiate this variable by retrieving a reference to it from the
        // layout
        // drawView = (DrawingView) findViewById(R.id.drawing);
		/*
		 * first paint color button First retrieve the Linear Layout it is
		 * contained in..
		 */
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        // first button and store it as the instance variable
        // currPaint = (ImageButton)paintLayout.getChildAt(0);
        // currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
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
        boolean listIsEmpty=true;

        for(int i=0;i<shipsStatus.size();++i){
            if(shipsStatus.elementAt(i).booleanValue()){ // if a ship is still not clicked
                if (gridMap[x] == 1){
                    //toast is needed here
                    continue;
                }
                gridMap[x] = 1;

                ImageButton ship= (ImageButton)shipContainer.getChildAt(i);
                ship.setBackgroundColor(Color.parseColor("#554455"));
                shipsStatus.set(i,Boolean.FALSE);

                imgView.setImageDrawable(getResources().getDrawable(
                        R.drawable.ship));
                listIsEmpty=false;
                break;

            }
            if(listIsEmpty){
                //toast
            }
        }

    }
}
