package com.basratec.battleships;


import java.io.Serializable;
import java.util.Arrays;

public class GridMap implements Serializable
{
    public static int STATUS_UNKNOWN = 0;

    public static int STATUS_HIT = 1;

    public static int STATUS_MISS = 2;

    public static int STATUS_OCCUPIED = 3;

    public static int STATUS_NOT_OCCUPIED = 4;

    public int[] gridMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0 };

    /**
     * Resets all the grid cells to unoccupied
     */
    public void reset()
    {
        for(int i=0 ; i<gridMap.length ; i++){
            gridMap[i] = GridMap.STATUS_NOT_OCCUPIED;
        }
    }

    public void populate(int[] map)
    {
        gridMap = map;
    }

    /**
     * Shoot a cell in your grid, i.e. the cell status is already known before shooting
     *
     * @param position int
     */
    public void shoot(int position)
    {
        if(GridMap.STATUS_OCCUPIED == gridMap[position]){
            gridMap[position] = GridMap.STATUS_HIT;
        }
        else if(GridMap.STATUS_NOT_OCCUPIED == gridMap[position]){
            gridMap[position] = GridMap.STATUS_MISS;
        }
    }

    /**
     * Shoot a cell in an enemy grid. You should provide the cell position and weather or not it was hit
     *
     * @param position int
     * @param result boolean
     */
    public void shoot(int position, boolean result)
    {
        if(result){
            gridMap[position] = GridMap.STATUS_HIT;
        }
        else{
            gridMap[position] = GridMap.STATUS_MISS;
        }
    }

    /**
     * Returns the status of a cell
     *
     * @param position int
     * @return int
     */
    public int getCellStatus(int position)
    {
        return gridMap[position];
    }

    /**
     * Sets the status of a cell
     *
     * @param position int
     * @param status int
     */
    public void setCellStatus(int position, int status)
    {
        gridMap[position] = status;
    }

    /**
     * Returns weather a cell is occupied
     *
     * @param position int
     * @return boolean
     */
    public boolean isOccupied(int position)
    {
        return (GridMap.STATUS_OCCUPIED == gridMap[position]) || (GridMap.STATUS_HIT == gridMap[position]);
    }

}
