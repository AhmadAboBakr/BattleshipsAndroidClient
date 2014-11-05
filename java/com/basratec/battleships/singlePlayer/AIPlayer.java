package com.basratec.battleships.singlePlayer;

import com.basratec.battleships.GridMap;

/**
 * Created by ahmad on 29/10/14.
 */
public abstract class AIPlayer
{
    protected GridMap myGrid, oponentGrid;
    public abstract void play(GridMap oponentMap);
}
