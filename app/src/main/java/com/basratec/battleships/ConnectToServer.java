package com.basratec.battleships;

import com.basratec.battleships.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ConnectToServer extends Activity {
    private SystemUiHider mSystemUiHider;
    private Socket connection;
    private boolean isConnected=false;
    private boolean foundPlayer=false;
    private TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connecting_to_server);
        status = (TextView) findViewById(R.id.fullscreen_content);
        //full screen mumbo jumbo
        final View contentView = findViewById(R.id.fullscreen_content);
        mSystemUiHider = SystemUiHider.getInstance(this, contentView,0);
        mSystemUiHider.setup();
         new ConnectionManager().start();
    }

    class ConnectionManager extends Thread{
        @Override
        public void run(){
            while(true) {
                try {
                    connection = new Socket("192.168.43.22", 3000);
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    status.setText(R.string.connection_failed);
                }
            }
            status.setText(R.string.Waiting_for_players);
            //ToDo start actual game
        }
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
