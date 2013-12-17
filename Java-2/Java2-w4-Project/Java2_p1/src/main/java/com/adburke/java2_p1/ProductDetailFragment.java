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


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by aaronburke on 12/16/13.
 */
public class ProductDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.product_detail, container, false);

        return view;
    }

}
