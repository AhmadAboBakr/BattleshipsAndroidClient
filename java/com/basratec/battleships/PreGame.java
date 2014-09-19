package com.basratec.battleships;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreGame extends Activity {
    private ArrayList<String> ARRV = new ArrayList<String>(25);
    public int[] gridMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int CountArr = 0;
    private boolean FLAG = true;
    // custom drawing view
//	private DrawingView drawView;
    // paint color button in the palette
    private ImageButton currPaint;
    private TextView timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);
        timer = (TextView)findViewById(R.id.timer);
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
    public void paintClicked(View view) {

        // if(view != currPaint){
        // new color
        // retrieve the tag of button
        ImageButton imgView = (ImageButton) view;
        String cell = view.getTag().toString();
        int x = Integer.parseInt(cell);
        if (gridMap[x] != 1) {
            gridMap[x] = 1;

            // drawView.setColor(color);
            // update UI
            imgView.setImageDrawable(getResources().getDrawable(
                    R.drawable.ship));

            // currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            // currPaint = (ImageButton) view;
            // }
        }

    }
}
