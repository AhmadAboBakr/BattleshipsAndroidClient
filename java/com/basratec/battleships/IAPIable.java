package com.basratec.battleships;

import org.json.JSONObject;

/**
 * An APIable class is a class that has functions available for public use,
 * and has a "call" function to dynamically call any of the public functions
 */
public interface IAPIable {
    /**
     * Takes a JSON object and calls a function with the same name as object.event,
     * with object.data as argument to the function
     *
     * @param data the data received from third party
     */
    public void call(JSONObject data);
}
