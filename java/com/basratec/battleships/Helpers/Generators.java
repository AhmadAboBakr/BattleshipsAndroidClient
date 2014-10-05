package com.basratec.battleships.Helpers;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.basratec.battleships.R;

/**
 * Created by bakr on 01/10/14.
 */
public class Generators {


    /**
     * THis function generates the grid and put it in the desired container
     * @param width the width of the grid
     *  @param height the height of the grid
     *  @param context the activity that initialized the generator
     *  @return a lineaLayout containing the grid
     *
     */
    public static LinearLayout generateGrid(int width,int height,Activity context){
        LinearLayout container = new LinearLayout(context);
        for(int i=0;i<height;++i){
            LinearLayout row= new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for(int j=0;j<width;++j){
                ImageButton ship = new ImageButton(context);
                ship.setTag(i*width+j);
                ship.setBackgroundColor(Color.rgb(0, 0, 255));
                ship.setLayoutParams(new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.cell), (int) context.getResources().getDimension(R.dimen.cell)));
                row.addView(ship);

            }
            container.addView(row);
        }
        return container;
    }
}
