package com.basratec.battleships;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nookz on 9/20/2014.
 */
public abstract class AAPIableActivity extends Activity implements IAPIable {

    /**
     *A list of functions that can be called from here, every child should provide his own
    */
    protected String[] callables;

    public void call(JSONObject data){
        try{
            String eventName = data.getString("event");
            try{
                Class[] cArg = new Class[1];
                cArg[0] = String.class;
                //we should find a way to support other "data" datatypes other than String
                this.getClass().getMethod(eventName, cArg).invoke(this, data.getString("data"));
            }
            catch (Exception e){

            }
        }
        catch (JSONException e){

        }
    }
}
