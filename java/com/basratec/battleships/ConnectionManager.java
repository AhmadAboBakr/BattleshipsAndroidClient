package com.basratec.battleships;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Callable;

/**
 * Created by nookz on 9/20/2014.
 */
public class ConnectionManager extends Thread {

    /**
     * The connection to the server, only one connection, to preserve socket id
     */
    private SocketSinglton connection;

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
     * The owning activity, mainly kept so that we can use its handlers when a server call comes
     */
    private AAPIableActivity callingObject;

    public ConnectionManager(AAPIableActivity callingObject) {
        this.callingObject = callingObject;
    }

    /**
     * initialize the connection, send any waiting messages
     */
    public void init(){
        connectionStatus = STATUS_TRYING;
        while(true) {
            try {
                connection = SocketSinglton.getInstance();
                connectionStatus = STATUS_SUCCESS;
                //send any messages waiting to be sent
                while(!messageQueue.isEmpty()){
                    send(messageQueue.pop());
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
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
    public void init(Callable successCallBack, Callable failureCallBack){
        connectionStatus = STATUS_TRYING;
        while(true) {
            try {
                connection = SocketSinglton.getInstance();
                connectionStatus = STATUS_SUCCESS;
                try{
                    successCallBack.call();
                    //send any messages waiting to be sent
                    while(!messageQueue.isEmpty()){
                    	//todo start with the first message
                        send(messageQueue.pop());
                    }
                }
                catch(Exception se){
                    se.printStackTrace();
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
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
        System.out.println("manager initialized...");
    }

    private void listen(){
        try{
            Gson gson = new Gson();
            Scanner in = new Scanner(connection.getInputStream());
            while(in.hasNext()){
                System.out.println("receiving message: ");
                String s = in.nextLine();
                System.out.println(s);
                callingObject.call(
                        gson.fromJson(
                                s,
                                JSONObject.class
                        )
                );
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

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
}
