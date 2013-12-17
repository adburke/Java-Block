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


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by aaronburke on 12/16/13.
 */
public class ProductDetailFragment extends Fragment {

    private ProductDetailListener listener;

    public interface ProductDetailListener {
        public void onWebLaunchClick();
    }

    Button webLaunchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.product_detail, container, false);

        webLaunchBtn = (Button) view.findViewById(R.id.webLaunchBtn);

        // Set button text
        webLaunchBtn.setText(R.string.webBtn);

        webLaunchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Start Activity to view the selected file
                listener.onWebLaunchClick();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (ProductDetailListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " need to implement ProductDetailListener");
        }

    }

}
