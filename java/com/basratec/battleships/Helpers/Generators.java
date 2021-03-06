package com.basratec.battleships.Helpers;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.basratec.battleships.GridMap;
import com.basratec.battleships.R;

/**
 * Created by bakr on 01/10/14.
 */
public class Generators {


    /**
     * This function generates the grid and put it in the desired container
     * todo get the values from config file (padding, background, cellWidth, etc..)
     * todo what if we want to supply a RelativeLayout?
     *
     * @param numberOfHorizontalCells the width of the grid in cells
     * @param numberOfVerticalCells the height of the grid in cells
     * @param context the activity that initialized the generator
     * @param container the container layout
     * @return a lineaLayout containing the grid
     */
    public static LinearLayout addGridToContainer(
            int numberOfHorizontalCells,
            int numberOfVerticalCells,
            Activity context,
            LinearLayout container,
            GridMap gridMap
    ){
        for(int i=0;i<numberOfVerticalCells;++i){
            LinearLayout row= new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1
            ));
            for(int j=0;j<numberOfHorizontalCells;++j){
                ImageButton ship = new ImageButton(context);
                ship.setTag(i*numberOfHorizontalCells+j);
                if(gridMap.isOccupied(i*numberOfHorizontalCells+j)){
                    ship.setBackgroundResource(R.drawable.intact);
                }
                else{
                    ship.setBackgroundColor(Color.rgb(255, 255, 255));
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );
                lp.setMargins(1,1,1,1);
                ship.setLayoutParams(lp);
                row.addView(ship);
            }
            container.addView(row);
        }
        return container;
    }

    /**
     * This function is the exact same function as the above, except it adds a onClickListener
     * to every button.
     *
     * todo the same todos as above
     * todo Someone PLEASE find a better way to do this!
     *
     * @param numberOfHorizontalCells the width of the grid in cells
     * @param numberOfVerticalCells the height of the grid in cells
     * @param context the activity that initialized the generator
     * @param container the container layout
     * @return a lineaLayout containing the grid
     */
    public static LinearLayout addClickableGridToContainer(
            int numberOfHorizontalCells,
            int numberOfVerticalCells,
            Activity context,
            LinearLayout container,
            View.OnClickListener ocl
    ){
        for(int i=0;i<numberOfVerticalCells;++i){
            LinearLayout row= new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1
            ));
            for(int j=0;j<numberOfHorizontalCells;++j){
                ImageButton ship = new ImageButton(context);
                ship.setTag(i*numberOfHorizontalCells+j);
                ship.setBackgroundColor(Color.rgb(255, 255, 255));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );
                lp.setMargins(2,2,2,2);
                ship.setLayoutParams(lp);
                ship.setOnClickListener(ocl);
                row.addView(ship);
            }
            container.addView(row);
        }
        return container;
    }
}
