/*
 * Project:		Java2-w3-Project
 *
 * Package:		Java2_p1
 *
 * Author:		aaronburke
 *
 * Date:		12 11, 2013
 */

package com.adburke.java2_p1;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;

public class ProductListDetail extends ActionBarActivity {
    static Context mContext;

    // Passed in values from Product List selection
    int productIndex;
    int filterIndex;
    String filterString;

    // Holds Cursor data from content provider
    HashMap<String, String> productInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        // Initialize context
        mContext = this;

        // URI for ContentProvider
        Uri productUri = null;

        // Capture incoming data
        Bundle incomingData = getIntent().getExtras();
        if (incomingData != null) {
            productIndex = incomingData.getInt("index");
            Log.i("productIndex", String.valueOf(productIndex));
            filterIndex = incomingData.getInt("filterIndex");
            Log.i("filterIndex", String.valueOf(filterIndex));
            filterString = incomingData.getString("filterString");
            Log.i("filterString", filterString);

            // Create URI for product call to capture data
            if (filterIndex == 0) {
                productUri = Uri.parse("content://" + CollectionProvider.AUTHORITY + "/items/" + productIndex);
            } else {
                productUri = Uri.parse("content://" + CollectionProvider.AUTHORITY + "/items/type/" + filterString + "/" + productIndex);
            }
        }

        if (productUri != null) {
            // Call the ContentProvider with the relevant URI
            Cursor cursor = getContentResolver().query(productUri, null, null, null, null);

            // Check if the cursor returned values
            if (cursor == null) {
                Toast.makeText(this, "Cursor value is null", Toast.LENGTH_LONG).show();
                Log.i("ProductListDetail", "NULL CURSOR AT: " + productUri.toString());
            }
            // Clear the product list of values if already created
            if (productInfo != null) {
                productInfo.clear();
            }

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    for (int i = 0, j = cursor.getCount(); i < j; i++) {
                        productInfo = new HashMap<String, String>();

                        productInfo.put("productName", cursor.getString(1));
                        productInfo.put("vendor", cursor.getString(2));
                        productInfo.put("productPrice", cursor.getString(3));
                        productInfo.put("productColor", cursor.getString(4));
                        productInfo.put("productMpn", cursor.getString(5));
                        productInfo.put("productUpc", cursor.getString(6));
                        productInfo.put("productManufacturer", cursor.getString(7));
                        productInfo.put("productUrl", cursor.getString(8));

                    }
                }

            }
            if (productInfo != null) {
                Log.i("PRODUCT DETAIL", "VALUES: " + productInfo.values().toString());

            }

        }



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.product_detail);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}