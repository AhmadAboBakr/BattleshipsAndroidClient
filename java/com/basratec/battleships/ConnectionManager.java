package com.basratec.battleships;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Callable;

/**
 * Manages the connection between the client and the server
 * After initializing, the ConnectionManager should either:
 * 1. listen to the server for any
 *    incoming events, and if given an APIable class in the constructor, a function will
 *    automatically be called using its "call()" function.
 * Or
 * 2. send a message to the server, in this case, the CM will wait until a connection is made
 *    and send all messages waiting to be sent
 */
public class ConnectionManager extends Thread {

    /**
     * The connection to the server, only one connection, to preserve socket id
     */
    private SocketSinglton connection;
    private JSONObject message;
    /**
     * whether or not we are connected to a server
     */
    private int connectionStatus = STATUS_NOT_INITIALIZED;
    private boolean activityEnded=false;
    /**
     * Any messages that need to be sent but waiting for server to connect
     */
    private Stack<String> messageQueue = new Stack<String>();

    public static final int STATUS_NOT_INITIALIZED = 0;

    public static final int STATUS_SUCCESS = 1;

    public static final int STATUS_FAILED = 2;

    public static final int STATUS_TRYING = 3;

    /**
     * The owning activity, mainly kept so that we can use its handlers when a server call comes
     */
    private AAPIableActivity callingObject;

    private ArrayList<String> endingEvents;


    public ConnectionManager(AAPIableActivity callingObject)
    {
        this.callingObject = callingObject;
        this.endingEvents = new ArrayList<String>();
    }

    public ConnectionManager(AAPIableActivity callingObject, ArrayList<String> endingEvents)
    {
        this.callingObject = callingObject;
        this.endingEvents = endingEvents;
    }

    /**
     * initialize the connection, send any waiting messages
     */
    public void init()
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
        message = new JSONObject();
    }

    /**
     * initialize the connection, send any waiting messages
     *
     * @param successCallBack a callable that will be called upon success
     * @param failureCallBack a callable that will be called on failure
     */
    public void init(Callable successCallBack, Callable failureCallBack)
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
    public void listen(){
        try{
            System.out.println("started listening in "+callingObject.getClass());
            InputStream inStream = connection.getInputStream();
            Scanner in = new Scanner(connection.getInputStream());
            while(true){
                while( !(inStream.available() >0 ||activityEnded) );//wait for something or end task this should work and it's a non blocking way
                System.out.println("has next or activity not ended in  " + callingObject.getClass());
                System.out.println("receiving message: ");
                if(this.activityEnded)break;
                String s = in.nextLine();
                JSONObject message = new JSONObject(s);
                System.out.println(s);
                callingObject.call(message);
                if(endingEvents.contains(message.getString("event")))break; //this is what we discussed
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("stopped listening for activity: "+callingObject.getClass());
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

    public void stopListening(){
        System.out.println("will stop listening in "+ callingObject.getClass());
        this.activityEnded=true;
    }

}