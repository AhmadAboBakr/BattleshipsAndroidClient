package com.basratec.battleships.Managers;

import com.basratec.battleships.AAPIableActivity;
import com.basratec.battleships.ConnectToServer;
import com.basratec.battleships.SocketSinglton;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Callable;

/**
 * Manages the connection between the client and the server
 * After initializing, the ServerConnectionManager should either:
 * 1. listen to the server for any
 *    incoming events, and if given an APIable class in the constructor, a function will
 *    automatically be called using its "call()" function.
 * Or
 * 2. send a message to the server, in this case, the CM will wait until a connection is made
 *    and send all messages waiting to be sent
 */
public class ServerConnectionManager extends ConnectionManager {

    /**
     * The connection to the server, only one connection, to preserve socket id
     */
    private SocketSinglton connection;

    private static ServerConnectionManager listener;

    private static ServerConnectionManager sender;
    /**
     * whether or not we are connected to a server
     */
    private int connectionStatus = STATUS_NOT_INITIALIZED;
    /**
     * Any messages that need to be sent but waiting for server to connect
     */
    private Stack<String> messageQueue = new Stack<String>();

    public static final int STATUS_NOT_INITIALIZED = 0;

    public static final int STATUS_SUCCESS = 1;

    public static final int STATUS_FAILED = 2;

    public static final int STATUS_TRYING = 3;

    /**
     * The currently running activity
     */

    private ArrayList<String> endingEvents;

    /**
     * Should be set to true to stop listening to the server
     */
    protected boolean stopListening = false;

    protected ServerConnectionManager()
    {
        super();
    }

    public  void startListning(AAPIableActivity activity)
    {
            listener.setCurrentActivity(activity);
            listener.start();
        System.out.println("started listening for activity: "+activity.getClass());
    }

    public static ServerConnectionManager getSender(AAPIableActivity activity)
    {
        if(null == sender){
          //  sender = new ServerConnectionManager(activity);
        }
        else{
            sender.setCurrentActivity(activity);
        }
        return sender;
    }

    public void setCurrentActivity(AAPIableActivity activity)
    {
        currentActivity = activity;
    }

    /**
     * initialize the connection, send any waiting messages
     */
    private void init()
    {
        connectionStatus = STATUS_TRYING; //initialize connection status
        while(true) { //keep trying until a connection is made (maybe there is a better way)
            try {
                connection = SocketSinglton.getInstance();
                connectionStatus = STATUS_SUCCESS;
                //send any messages waiting to be sent
                while(!messageQueue.isEmpty()){
                    send(messageQueue.pop());
                }
                break; //successfully connected, no need to stay in this loop
            } catch (IOException e) {
                connectionStatus = STATUS_FAILED;
            }
        }
    }

    /**
     * initialize the connection, send any waiting messages
     *
     * @param successCallBack a callable that will be called upon success
     * @param failureCallBack a callable that will be called on failure
     */
    private void init(Callable successCallBack, Callable failureCallBack)
    {
        connectionStatus = STATUS_TRYING;
        while(true) {
            try {
                connection = SocketSinglton.getInstance();
                connectionStatus = STATUS_SUCCESS;
                try{
                    successCallBack.call();
                    //send any messages waiting to be sent
                    while(!messageQueue.isEmpty()){
                        //todo start with the first message not the last
                        //todo Discuss this further
                        send(messageQueue.pop());
                    }
                }
                catch(Exception se){
                    se.printStackTrace();
                }
                break;
            } catch (IOException e) {
                connectionStatus = STATUS_FAILED;
                try{
                    failureCallBack.call();
                }
                catch (Exception e1){ //an exception occured while handling the exception. Lovely :D
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run(){
        System.out.println("initializing manager ...");
        init();
        listen();
    }

    /**
     * Listen to the server and call the calling object's "call" function if a message is received
     */
    protected void listen(){
        try{
            System.out.println("started listening in "+currentActivity.getClass());
            InputStream inStream = connection.getInputStream();
            Scanner in = new Scanner(connection.getInputStream());
            System.out.println("stopped listening: "+ stopListening);
            while(  in.hasNext() && !stopListening ){
                System.out.println("has next and activity not ended in  " + currentActivity.getClass());
                if(this.stopListening)break;
                System.out.println("did not stop listening!");
                String s = in.nextLine();
                System.out.println("receiving message: "+s);
                JSONObject message = new JSONObject(s);
                currentActivity.call(message);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("stopped listening for activity: "+currentActivity.getClass());
    }


    /**
     * Send a message to the server,
     * if no connection is present it will wait until there is and then try sending again
     *
     */
    public void send(String message){
        if(connectionStatus == STATUS_SUCCESS){
            System.out.println("sending message to server...");
            System.out.println("message content: " + message);
            try{
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(connection.getOutputStream())),true);
                System.out.println("sending...");
                out.println(message);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("message should be sent!");
        }
        else{
            messageQueue.push(message);
        }
    }

    private void stopListening(){
        System.out.println("will stop listening in "+ currentActivity.getClass());
        stopListening = true;
    }

}