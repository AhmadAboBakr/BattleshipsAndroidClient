package com.basratec.battleships;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ahmad on 26/09/14.
 */
public class Helper {

    public void setTimeOut(int milliSeconds,Method callback){
        try {
            while (milliSeconds > 0) {
                milliSeconds--;
                Thread.sleep(1);
            }

            callback.invoke(this);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
