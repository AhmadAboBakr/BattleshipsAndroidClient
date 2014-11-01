package com.basratec.battleships;


import android.util.SparseArray;

import com.basratec.battleships.CustomTypes.SerializableSparseArray;
import com.basratec.battleships.Ships.BaseShip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridMap implements Serializable
{
    public static int STATUS_UNKNOWN = 0;

    public static int STATUS_HIT = 1;

    public static int STATUS_MISS = 2;

    public static int STATUS_OCCUPIED = 3;

    public static int STATUS_NOT_OCCUPIED = 4;

    public int[][] gridMap;

    public List<BaseShip> ships = new ArrayList<BaseShip>();

    /**
     * Number of horizontal cells in grid
     */
    public static int NUMBER_OF_HORIZONTAL_CELLS = 10;

    /**
     * Number of vertical cells in grid
     */
    public static int NUMBER_OF_VERTICAL_CELLS = 10;

    /**
     * An inverted index of all the ships' positions
     * find a way to convert it to a 2D array
     */
    public SerializableSparseArray<BaseShip> shipPositions = new SerializableSparseArray<BaseShip>();

    public GridMap()
    {
        this.gridMap = new int[GridMap.NUMBER_OF_HORIZONTAL_CELLS][GridMap.NUMBER_OF_VERTICAL_CELLS];
        reset();
    }

    /**
     * Resets all the grid cells to unknown
     */
    public void reset()
    {
        for(int i = 0 ; i<gridMap.length ; i++){
            for(int j = 0 ; j<gridMap[i].length ; j++) {
                gridMap[i][j] = GridMap.STATUS_UNKNOWN;
            }
        }
    }

    public boolean placeShip(BaseShip ship,int startingPosition)
    {
        //todo FixLater
        //todo question the logic!
        int y = startingPosition/NUMBER_OF_HORIZONTAL_CELLS;
        int x = startingPosition%NUMBER_OF_VERTICAL_CELLS;

        this.ships.add(ship);
        for(int i = 0;i<ship.width;++i){
            for(int j = 0;j<ship.height;++j){
                if(gridMap[i][j]==STATUS_OCCUPIED)return false;
            }
        }
        for(int i = 0;i<ship.width;++i){
            for(int j = 0;j<ship.height;++j){
                gridMap[x][y] = GridMap.STATUS_OCCUPIED;
                shipPositions.put(startingPosition, ship);

            }
        }
        return true;
    }

    /**
     * Shoot a cell in your grid, i.e. the cell status is already known before shooting
     *
     * @param position int
     */
    //TODO Fix this shit
    public BaseShip shoot(int position)
    {
        if(GridMap.STATUS_OCCUPIED == gridMap[position]){
            gridMap[position] = GridMap.STATUS_HIT;
            //find the hit ship
            BaseShip hitShip = shipPositions.get(position);
            //update its health = its health - 100 / its size
//            hitShip.health -= 100/hitShip.size;
            //return the ship
            return hitShip;
        }
        else if(GridMap.STATUS_NOT_OCCUPIED == gridMap[position]){
            gridMap[position] = GridMap.STATUS_MISS;
        }
        return null;
    }

    /**
     * Shoot a cell in an enemy grid. You should provide the cell position and weather or not it was hit
     *
     * todo remove duplicate code from one of the shoot functions
     * @param position int
     * @param result boolean
     */

    //TODO Fix this shit
    public BaseShip shoot(int position, boolean result)
    {
        if(result){
            gridMap[position] = GridMap.STATUS_HIT;
            //find the hit ship
            BaseShip hitShip = shipPositions.get(position);
            //update its health = its health - 100 / its size
//            hitShip.health -= 100/hitShip.size;
            //return the ship
            return hitShip;
        }
        else{
            gridMap[position] = GridMap.STATUS_MISS;
            return null;
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
