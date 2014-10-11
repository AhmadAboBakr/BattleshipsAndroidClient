package com.basratec.battleships;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An APIable activity is any activity that has functions available for public use,
 * It inherits or implements "call()" that can be used to call a function from
 * a list of functions provided by activity without hardcoding the function name.
 */
public abstract class AAPIableActivity extends Activity implements IAPIable {

    /**
     *A list of functions that can be called from here, every child should provide his own
    */
    protected String[] callables;

    /**
     * Caching this for use in embedded classes
     */
    protected AAPIableActivity that = this;

    public void call(final JSONObject data){
        try{
            final String eventName = data.getString("event");
            System.out.println("starting up the call function...");
            try{
                final Class[] cArg = new Class[1];
                cArg[0] = String.class;
                System.out.println("will try to run in UI thread, in class: "+that.getClass());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            System.out.println("calling "+eventName + " in class: "+that.getClass());
                            //we should find a way to support other "data" datatypes other than String
                            that.getClass().getMethod(eventName, cArg).invoke(that, data.getString("data"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
