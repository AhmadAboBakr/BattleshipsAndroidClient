package com.basratec.battleships;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by nookz on 9/27/2014.
 * 
 * @param <boolen>
 */

public class MainGame extends Activity {

	private boolean PLAY_FLAG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
	}
// not working yet ..
	private void intializeMyGrid(int [] MyGrid) {
/*
		LinearLayout MyGridLayout = (LinearLayout) findViewById(R.id.MyGrid);
		for (int i = 0; i <= gridSize; i++) {
			if (MyGrid[i] == 1) {
				currCell = (ImageButton) MyGridLayout.getChildAt(i);
				currCell.setImageDrawable(getResources().getDrawable(
						R.drawable.ship));
			}
		}*///endFor
	}//endmethod

	public boolean play() {
		PLAY_FLAG = true;
		// don't forget to disable it after play
		return PLAY_FLAG;
	}
}