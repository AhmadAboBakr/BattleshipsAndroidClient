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

    /**
     *
     * @param ship the ship to place
     * @param startingPosition the starting position on the grid
     * @return true for success, false for failure
     */
    public boolean placeShip(BaseShip ship, int startingPosition)
    {
        int y = startingPosition/NUMBER_OF_HORIZONTAL_CELLS;
        int x = startingPosition%NUMBER_OF_VERTICAL_CELLS;

        this.ships.add(ship);

        //Check if any of the positions to occupy is already taken
        for(int i = 0;i<ship.width;++i){
            for(int j = 0;j<ship.height;++j){
                if(gridMap[x + i][y + j]==STATUS_OCCUPIED)return false;
            }
        }

        //Occupy the positions
        for(int i = 0;i<ship.width;++i){
            for(int j = 0;j<ship.height;++j){
                gridMap[x + i][y + j] = GridMap.STATUS_OCCUPIED;
                //todo don't we need all the positions? not just the starting position
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
        int y = position/NUMBER_OF_HORIZONTAL_CELLS;
        int x = position%NUMBER_OF_VERTICAL_CELLS;

        if(GridMap.STATUS_OCCUPIED == gridMap[x][y]){
            gridMap[x][y] = GridMap.STATUS_HIT;
            //find the hit ship
            BaseShip hitShip = shipPositions.get(position);
            //update its health = its health - 100 / its size
//            hitShip.health -= 100/hitShip.size;
            //return the ship
            return hitShip;
        }
        else if(GridMap.STATUS_NOT_OCCUPIED == gridMap[x][y]){
            gridMap[x][y] = GridMap.STATUS_MISS;
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
        int y = position/NUMBER_OF_HORIZONTAL_CELLS;
        int x = position%NUMBER_OF_VERTICAL_CELLS;

        if(result){
            gridMap[x][y] = GridMap.STATUS_HIT;
            //find the hit ship
            BaseShip hitShip = shipPositions.get(position);
            //update its health = its health - 100 / its size
//            hitShip.health -= 100/hitShip.size;
            //return the ship
            return hitShip;
        }
        else{
            gridMap[x][y] = GridMap.STATUS_MISS;
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
        int y = position/NUMBER_OF_HORIZONTAL_CELLS;
        int x = position%NUMBER_OF_VERTICAL_CELLS;

        return gridMap[x][y];
    }

    /**
     * Sets the status of a cell
     *
     * @param position int
     * @param status int
     */
    public void setCellStatus(int position, int status)
    {
        int y = position/NUMBER_OF_HORIZONTAL_CELLS;
        int x = position%NUMBER_OF_VERTICAL_CELLS;

        gridMap[x][y] = status;
    }

    /**
     * Returns weather a cell is occupied
     *
     * @param position int
     * @return boolean
     */
    public boolean isOccupied(int position)
    {
        int y = position/NUMBER_OF_HORIZONTAL_CELLS;
        int x = position%NUMBER_OF_VERTICAL_CELLS;

        return (GridMap.STATUS_OCCUPIED == gridMap[x][y]) || (GridMap.STATUS_HIT == gridMap[x][y]);
    }

}
