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
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.HashMap;


public class ProductListDetail extends ActionBarActivity implements ProductDetailFragment.ProductDetailListener {
    static Context mContext;

    // Passed in values from Product List selection
    int productIndex;
    int filterIndex;
    String filterString;

    // Holds Cursor data from content provider
    HashMap<String, String> productInfo;

    // Layout variables and array containers for each column
    TextView productName;

    TextView row2;
    TextView row3;
    TextView row4;
    TextView row5;
    TextView row6;
    TextView row7;

    TextView vendor;
    TextView price;
    TextView color;
    TextView mpn;
    TextView upc;
    TextView mfr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetailfrag);

        // Initialize context
        mContext = this;

        Resources res = getResources();
        String[] detailStrings = res.getStringArray(R.array.detail_categories_array);

        // URI for ContentProvider
        Uri productUri = null;

        // Initialize layout variables and array containers for each column
        productName = (TextView) findViewById(R.id.productDetailName);

        row2 = (TextView) findViewById(R.id.rowVendor);
        row3 = (TextView) findViewById(R.id.rowPrice);
        row4 = (TextView) findViewById(R.id.rowColor);
        row5 = (TextView) findViewById(R.id.rowMpn);
        row6 = (TextView) findViewById(R.id.rowUpc);
        row7 = (TextView) findViewById(R.id.rowMfr);

        vendor = (TextView) findViewById(R.id.productDetailVendor);
        price = (TextView) findViewById(R.id.productDetailPrice);
        color = (TextView) findViewById(R.id.productDetailColor);
        mpn = (TextView) findViewById(R.id.productDetailMpn);
        upc = (TextView) findViewById(R.id.productDetailUpc);
        mfr = (TextView) findViewById(R.id.productDetailMfr);

        row2.setText(detailStrings[0]);
        row3.setText(detailStrings[1]);
        row4.setText(detailStrings[2]);
        row5.setText(detailStrings[3]);
        row6.setText(detailStrings[4]);
        row7.setText(detailStrings[5]);

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

        // Check for valid cursor or saved data
        if (productUri != null && savedInstanceState == null) {
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

                productName.setText(productInfo.get("productName"));
                vendor.setText(productInfo.get("vendor"));
                price.setText(productInfo.get("productPrice"));
                color.setText(productInfo.get("productColor"));
                mpn.setText(productInfo.get("productMpn"));
                upc.setText(productInfo.get("productUpc"));
                mfr.setText(productInfo.get("productManufacturer"));

            }

        } else {
            Log.i("PRODUCT_LIST_DETAIL", "Using Saved Data");
            productInfo = (HashMap<String, String>) savedInstanceState.getSerializable("savedList");

            productName.setText(productInfo.get("productName"));
            vendor.setText(productInfo.get("vendor"));
            price.setText(productInfo.get("productPrice"));
            color.setText(productInfo.get("productColor"));
            mpn.setText(productInfo.get("productMpn"));
            upc.setText(productInfo.get("productUpc"));
            mfr.setText(productInfo.get("productManufacturer"));
        }



    }
    // Method to pass data back to API BROWSER page
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("FINISHED", "PASS VALUE TEST");
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.product_detail);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        if(productInfo != null && !productInfo.isEmpty()) {
            savedInstanceState.putSerializable("savedList", (Serializable) productInfo);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onWebLaunchClick() {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        Uri webUri = Uri.parse(productInfo.get("productUrl"));
        intent.setData(webUri);
        startActivity(intent);
    }
}
