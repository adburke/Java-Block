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

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.util.HashMap;


public class ProductListDetail extends Activity implements ProductDetailFragment.ProductDetailListener {

    // Passed in values from Product List selection
    int productIndex;
    int filterIndex;
    String filterString;

    Uri productUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetailfrag);


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
                Log.i("ProductListDetail", "NULL CURSOR AT: " + productUri.toString());
            }

            ProductDetailFragment fragment = (ProductDetailFragment) getFragmentManager().findFragmentById(R.id.productdetail_fragment);

            if (fragment != null) {
                fragment.updateProductDetails(productUri);
            }
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

//        if(productInfo != null && !productInfo.isEmpty()) {
//            savedInstanceState.putSerializable("savedList", (Serializable) productInfo);
//        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onWebLaunchClick(HashMap<String, String> productInfo) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        Uri webUri = Uri.parse(productInfo.get("productUrl"));
        intent.setData(webUri);
        startActivity(intent);
    }


}
