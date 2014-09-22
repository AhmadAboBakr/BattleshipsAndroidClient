package com.basratec.battleships;

import org.json.JSONObject;

/**
 * Created by nookz on 9/20/2014.
 */
public interface IAPIable {
    /**
     * Handles the data received in whichever way necessary
     *
     * @param data the data received from third party
     */
    public void call(JSONObject data);
}
