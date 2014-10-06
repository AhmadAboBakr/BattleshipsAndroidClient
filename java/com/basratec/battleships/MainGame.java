package com.basratec.battleships;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.basratec.battleships.Helpers.Generators;

/**
 * Created by nookz on 9/27/2014.
 *
 */

public class MainGame extends Activity {

	private boolean PLAY_FLAG;

    private LinearLayout myGridContainer;

    private LinearLayout enemyGridContainer;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
        myGridContainer = (LinearLayout) findViewById(R.id.MyGrid);
        myGridContainer = Generators.addGridToContainer(5, 5, this, R.dimen.small_cell, R.dimen.small_cell, myGridContainer);

        enemyGridContainer = (LinearLayout) findViewById(R.id.EnemyGrid);
        enemyGridContainer = Generators.addGridToContainer(5, 5, this, R.dimen.cell, R.dimen.cell, enemyGridContainer);

	}

	public boolean play() {
		PLAY_FLAG = true;
        enemyGridContainer.setBackground(getResources().getDrawable(R.drawable.enemy_border));
        //wait for the player to play or something.
        myGridContainer.setBackground(getResources().getDrawable(R.drawable.border));
		return PLAY_FLAG;
	}
}