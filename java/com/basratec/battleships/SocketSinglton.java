package com.basratec.battleships;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ahmad on 9/1/2014.
 */
public class SocketSinglton  extends Socket
{
    public static SocketSinglton instance = null;

    private static final String address="192.168.1.69";

    private static final int port=6969;

    private static int connectionStatus = SocketSinglton.CONNECTION_NOT_ATTEMPTED;

    public static final int CONNECTION_NOT_ATTEMPTED = 0;

    public static final int CONNECTION_ATTEMPTING = 1;

    public static final int CONNECTION_SUCCESS = 2;

    public static final int CONNECTION_FAILED = -1;

    private SocketSinglton(String address, int port) throws IOException
    {
        super(address, port);
    }

    public static SocketSinglton getInstance() throws IOException
    {
        while(SocketSinglton.connectionStatus != SocketSinglton.CONNECTION_SUCCESS){
            if (
                    SocketSinglton.connectionStatus == SocketSinglton.CONNECTION_NOT_ATTEMPTED
                    || SocketSinglton.connectionStatus == SocketSinglton.CONNECTION_FAILED
            ) {
                SocketSinglton.connectionStatus = SocketSinglton.CONNECTION_ATTEMPTING;
                instance = new SocketSinglton(address, port);
                SocketSinglton.connectionStatus = SocketSinglton.CONNECTION_SUCCESS;
            }
        }
        return instance;

    }
}
