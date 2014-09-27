package com.basratec.battleships;

import com.basratec.battleships.util.SystemUiHider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Callable;

/**
 * This activity should be shown while trying to connect a player to a game
 */
public class ConnectToServer extends AAPIableActivity {

    private SystemUiHider mSystemUiHider;

    /**
     * The status message that will appear to the user
     */
    private TextView status;

    /**
     * A list off all the functions that can be called using call()
     */
    protected String[] callables = {"start"};

    /**
     * Caching self reference for embedded functions and classes to use
     */
    private ConnectToServer that = this;
    private ConnectionManager connectionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_connecting_to_server);
        status = (TextView) findViewById(R.id.fullscreen_content);
        Button cancel = (Button)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                   //connection.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        //full screen mumbo jumbo
        final View contentView = findViewById(R.id.fullscreen_content);
        mSystemUiHider = SystemUiHider.getInstance(this, contentView,0);
        mSystemUiHider.setup();
        //start listening to the server
        connectionListener = new ConnectionManager(that);
        connectionListener.start();

        //connect to the server, show appropriate messages when connection fails or succeeds,
        //and then send the "start" event
        new Thread(new Runnable(){
            @Override
            public void run() {
                ConnectionManager manager = new ConnectionManager(that);
                manager.init(
                        new Callable() { //the success callback
                            @Override
                            public Object call() throws Exception {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        status.setText(R.string.Waiting_for_players);
                                    }
                                });
                                return null;
                            }
                        },
                        new Callable() { //the failure callback
                            @Override
                            public Object call() throws Exception {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        status.setText(R.string.connection_failed);
                                    }
                                });
                                return null;
                            }
                        }
                );
                manager.send("{\"event\":\"start\"}"); //ask the server to start a game for us
            }

        }).start();
    }

    /**
     * This function should be called to start the game
     */
    public void start(String data){
        Intent preGame = new Intent(getApplicationContext(),PreGame.class);
        startActivity(preGame);
        connectionListener.stopListening();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
                delayedHide(3000);
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}
