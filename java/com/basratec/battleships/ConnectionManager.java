package com.basratec.battleships;

import android.content.Intent;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by nookz on 9/20/2014.
 */
public class ConnectionManager extends Thread {

    private SocketSinglton connection;

    public void init(){
        try{
            connection = SocketSinglton.getInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        System.out.println("initializing manager ...");
        init();
        System.out.println("manager initialized...");
    }

    public void send(String message){
        System.out.println("sending message to server...");
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
}
