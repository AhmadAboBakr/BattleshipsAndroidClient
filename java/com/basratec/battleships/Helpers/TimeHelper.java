package com.basratec.battleships.Helpers;

import com.basratec.battleships.AAPIableActivity;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ahmad on 26/09/14.
 */
public class TimeHelper {

    public static void setTimeOut(final int milliSeconds, final AAPIableActivity context,final Method callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0;i<milliSeconds;++i){
                        Thread.sleep(1);
                    }
                    callback.invoke(context);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public static void setTimeInterval(final int milliSeconds,final Method callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        for (int i = 0; i < milliSeconds; ++i) {
                            Thread.sleep(1);
                        }
                        callback.invoke(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
