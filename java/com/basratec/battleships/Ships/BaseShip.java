package com.basratec.battleships.Ships;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Abstract ship that all ship types should inherit from
 */
abstract public class BaseShip implements Serializable
{
    public final int HORIZONTAL = 0;
    public final int VERTICAL = 1;
    /**
     * The number of tiles the ship will occupy
     */
    public int width, height;
    public int oreantation;
    public int health = 100;

    public void flip()
    {
        int temp = width;
        width = height;
        height = temp;

    }
}
