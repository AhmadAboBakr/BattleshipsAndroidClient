package com.basratec.battleships.Managers;

import android.app.Activity;

import com.basratec.battleships.AAPIableActivity;
import com.basratec.battleships.SocketSinglton;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by bakr on 10/10/14.
 */
public abstract class ConnectionManager extends Thread {

    /**
     * The currently running activity
     */
    protected  AAPIableActivity currentActivity;

    public void setCurrentActivity(AAPIableActivity activity) {
        currentActivity = activity;
    }
    public abstract void startListning(AAPIableActivity activity);
    protected abstract void listen();
    public abstract void send(String Message);


}