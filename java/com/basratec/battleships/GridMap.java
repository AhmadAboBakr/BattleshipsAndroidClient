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

    public int[] gridMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public List<BaseShip> ships = new ArrayList<BaseShip>();

    /**
     * An inverted index of all the ships' positions
     */
    public SerializableSparseArray<BaseShip> shipPositions = new SerializableSparseArray<BaseShip>();

    /**
     * Resets all the grid cells to unoccupied
     */
    public void reset()
    {
        for(int i=0 ; i<gridMap.length ; i++){
            gridMap[i] = GridMap.STATUS_NOT_OCCUPIED;
        }
    }

    public void populate(BaseShip[] ships)
    {
        for(BaseShip ship : ships ) {
            this.ships.add(ship);
            for(int tile : ship.occupiedTiles){
                gridMap[tile] = GridMap.STATUS_OCCUPIED;
                shipPositions.put(tile, ship);
            }
        }
    }

    public void placeShip(BaseShip ship)
    {
        this.ships.add(ship);
        while(!ship.occupiedTiles.isEmpty()){
            int tile = ship.occupiedTiles.pop();
            gridMap[tile] = GridMap.STATUS_OCCUPIED;
            shipPositions.put(tile, ship);
        }
    }

    /**
     * Shoot a cell in your grid, i.e. the cell status is already known before shooting
     *
     * @param position int
     */
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
