/*
 * Project:		Java2-w4-Project
 *
 * Package:		Java2_p1
 *
 * Author:		aaronburke
 *
 * Date:		 	12 16, 2013
 */

package com.adburke.java2_p1;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by aaronburke on 12/16/13.
 */
// Extend SimpleAdapter to create zebra coloring on rows
public class ProductListAdapter extends SimpleAdapter {

    public ProductListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get reference to the row
        View view = super.getView(position, convertView, parent);
        // Check for odd or even to set alternate colors to the row background
        if(position % 2 == 0){
            if (view != null) {
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            }
        }
        else {
            if (view != null) {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
        }
        return view;
    }
}
