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
    protected AAPIableActivity that = this;

    public void call(final JSONObject data){
        try{
            final String eventName = data.getString("event");
            System.out.println("starting up the call function...");
            try{
                final Class[] cArg = new Class[1];
                cArg[0] = String.class;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            System.out.println("calling "+eventName);
                            //we should find a way to support other "data" datatypes other than String
                            that.getClass().getMethod(eventName, cArg).invoke(this, data.getString("data"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }
            catch (Exception e){

            }
        }
        catch (JSONException e){

        }
    }
}
