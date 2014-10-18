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
    /**
     * The number of tiles the ship will occupy
     */
    public int size;

    public Stack<Integer> occupiedTiles = new Stack<Integer>();

    public int health = 100;

}
