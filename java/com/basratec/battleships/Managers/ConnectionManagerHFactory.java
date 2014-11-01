package com.basratec.battleships.Managers;

import com.basratec.battleships.AAPIableActivity;

/**
 * Created by ahmad on 10/10/14.
 */
public class ConnectionManagerHFactory {
    private static ConnectionManager connectionManager=null;
    public final static int SERVER=0;
    public final static int LOCAL=1;

    public static ConnectionManager newGame(AAPIableActivity activity,int gameType){
        if(gameType == SERVER){
            connectionManager= new ServerConnectionManager();
            System.out.println("Started the connection");
        }
        else if(gameType == LOCAL){
            connectionManager = new LocalConnectionManager();
        }
        connectionManager.startListning(activity); //ToDO Enhance

        return connectionManager;
    }
    public static ConnectionManager currentGame(AAPIableActivity activity){

        connectionManager.setCurrentActivity(activity);
        return connectionManager;
    }
}
