package com.basratec.battleships;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EndGame extends AAPIableActivity {

    /**
     * caching for use in embedded classes
     */
    private EndGame that = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Boolean won = (Boolean)getIntent().getBooleanExtra("result", false);

        final Button start= (Button) findViewById(R.id.start);
        Button exit = (Button) findViewById(R.id.exit);
        TextView result = (TextView) findViewById(R.id.result);
        result.setText(won?"You Win!":"You Lose!");

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startButtonHandler();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oS = new Intent(getApplicationContext(),OpeningScreen.class);
                startActivity(oS);
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
        Intent serverConnector = new Intent(this,ConnectToServer.class);
//        connectionListener.stopListening();
        startActivity(serverConnector);
//        connectionListener.start();
    }
}
