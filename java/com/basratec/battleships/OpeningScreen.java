package com.basratec.battleships;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.basratec.battleships.Managers.ConnectionManagerHFactory;
import com.basratec.battleships.Managers.ServerConnectionManager;


public class OpeningScreen extends AAPIableActivity {

    /**
     * caching for use in embedded classes
     */
    private OpeningScreen that = this;
    private ServerConnectionManager connectionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        Button start= (Button) findViewById(R.id.start);
        Button exit = (Button) findViewById(R.id.exit);

        LinearLayout buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);
        Button testMainGameUI = new Button(this);
        testMainGameUI.setText("Test UI");
        Button playAgainstAI = new Button(this);
        playAgainstAI.setText("Play against AI");

        buttonsContainer.addView(testMainGameUI);

        //start a connection manager to listen to the server
//        new Thread(new Runnable(){
//			@Override
//			public void run() {
//                //initialize a new manager to send an event to the server
//                ServerConnectionManager manager = new ServerConnectionManager(that);
//                manager.init();
//                manager.send("{\"event\":\"OS\",\"data\":\"nothing\"}");
//            }
//        }).start();
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startButtonHandler();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        testMainGameUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainGame = new Intent(getApplicationContext(),MainGame.class);
                startActivity(mainGame);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
    private void startButtonHandler(){

        System.out.println("Starting Connect to server activity");
        ConnectionManagerHFactory.newGame(that,ConnectionManagerHFactory.SERVER);
        Intent serverConnector = new Intent(this,ConnectToServer.class);
        startActivity(serverConnector);
    }
}
