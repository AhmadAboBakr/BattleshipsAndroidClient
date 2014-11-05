package com.basratec.battleships.singlePlayer;

import com.basratec.battleships.GridMap;
import com.basratec.battleships.Ships.BaseShip;
import com.basratec.battleships.Ships.TinyShip;

/**
 * Created by bakr on 28/10/14.
 */
public class SuperEasyAIPlayer extends AIPlayer
{
    private int currentTile;
    private final int NUMPER_OF_SHIPS=5;

    public SuperEasyAIPlayer() {
        currentTile=0;
        for(int i=0;i<NUMPER_OF_SHIPS;++i) {
            BaseShip ship = new TinyShip();
//            ship.occupiedTiles.push(new Integer(i));
//            myGrid.placeShip(ship);
        }
    }

    public void play(GridMap oponentMap){
    }

    public void firedAt(int pos){
        myGrid.shoot(pos);
    }
}
