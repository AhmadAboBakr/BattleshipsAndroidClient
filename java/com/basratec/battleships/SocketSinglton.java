package com.basratec.battleships;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ahmad on 9/1/2014.
 */
public class SocketSinglton  extends Socket{
    private static SocketSinglton instance= null;
    private static final String address="192.168.1.67";
    private static final int port=6969;
    private SocketSinglton(String adress,int port) throws IOException {
        super(adress,port);
        System.out.println();

    }
    public static SocketSinglton getInstance()throws IOException{
        if (instance == null) {
            instance=new SocketSinglton(address,port);
        }
        return instance;

    }
}
