package com.basratec.battleships.Activities;

import java.net.Socket;
import java.util.Stack;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basratec.battleships.AAPIableActivity;
import com.basratec.battleships.GridMap;
import com.basratec.battleships.Helpers.Generators;
import com.basratec.battleships.Helpers.TimeHelper;
import com.basratec.battleships.Managers.ConnectionManager;
import com.basratec.battleships.Managers.ConnectionManagerHFactory;
import com.basratec.battleships.R;
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
        OnCellClickListener ocl = new OnCellClickListener();

        LinearLayout gridContainer = (LinearLayout) findViewById(R.id.theGrid);
        gridContainer = Generators.addClickableGridToContainer(
                GridMap.NUMBER_OF_HORIZONTAL_CELLS,
                GridMap.NUMBER_OF_VERTICAL_CELLS,
                this,
                gridContainer,
                ocl
        );

        initializeShips();
        try {
            TimeHelper.setTimeOut(1000, that, this.getClass().getMethod("updateTimer", null));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public class OnCellClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
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
            gridMap.placeShip(ship, position);
            imgView.setAlpha(.5f);
//            imgView.setBackgroundColor(Color.parseColor("#FF5500"));
            imgView.setBackgroundResource(R.drawable.intact);
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu., menu);
        return true;
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
        if(startingShips.isEmpty()){
            //send grid to server  (JSONArray) is promising but require Android API 19 or more we need a decision
            try {
                final String grid = new JSONArray(gridMap.getOneDimensionalGridMap()).toString();
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
