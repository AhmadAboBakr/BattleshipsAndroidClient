package com.basratec.battleships.Managers;

import com.basratec.battleships.AAPIableActivity;
import com.basratec.battleships.singlePlayer.AIPlayer;
import com.basratec.battleships.singlePlayer.SuperEasyAIPlayer;

import java.util.concurrent.Callable;

/**
 * Created by bakr on 10/10/14.
 */


public class LocalConnectionManager extends ConnectionManager{
    private AIPlayer player;
    @Override
    protected void listen() {

    }

    @Override
    public void send(String Message) {

    }

    @Override
    public void init(Callable successCallBack, Callable failureCallBack) {

    }

    @Override
    public void startListning(AAPIableActivity activity) {
        player= new SuperEasyAIPlayer();
    }
}

