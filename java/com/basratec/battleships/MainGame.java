package com.basratec.battleships;

import java.util.ArrayList;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainGame extends Activity {
	private ArrayList<String> ARRV = new ArrayList<String>(25);
	public int[] gridMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int CountArr = 0;
	private boolean FLAG = true;
	// custom drawing view
//	private DrawingView drawView;
	// paint color button in the palette
	private ImageButton currPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

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
		String color = view.getTag().toString();
		int x = Integer.parseInt(color);
		if (gridMap[x] != 1) {
			gridMap[x] = 1;

			// drawView.setColor(color);
			Log.d("youssef", color);
			Log.d("fffffffff", "ffffffffff");
			// update UI
			if (x == 14) {// if he hit ship red color appears
				imgView.setImageDrawable(getResources().getDrawable(
						R.drawable.paint_hit));

			} else
				imgView.setImageDrawable(getResources().getDrawable(
						R.drawable.paint_pressed));

			// currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			// currPaint = (ImageButton) view;
			// }
		}

	}
}
