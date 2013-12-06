/*
 * Project:		Java2_p1
 *
 * Package:		Java2_p1
 *
 * Author:		Aaron Burke
 *
 * Date:		11/26/2013
 */

package com.adburke.java2_p1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Api_browser extends Activity {
    static Context mContext;
    FileManager mFile;
    static String mJsonFile = "json_data.txt";

    // Can be used to tell if the file has already been created on the device
    public static Boolean writeStatus;

    // Query All Button
    public static Button queryAllBtn;

    // Spinner variables
    public static String[] mListItems;
    public static Spinner selectionSpinner;
    public static Boolean spinSelect;

    // List View
    public static ListView resultsList;

    // ArrayList of hashmaps for listview
    public ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_browser);

        // Initialize context
        mContext = this;

        writeStatus = false;

        Boolean status = ConnectionStatus.getNetworkStatus(mContext);

        mListItems = getResources().getStringArray(R.array.selection_array);

        // Button
        queryAllBtn = (Button)findViewById(R.id.showAllBtn);
        queryAllBtn.setEnabled(false);
        queryAllBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                onListUpdate(CollectionProvider.JsonData.CONTENT_URI);
            }
        });

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mListItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectionSpinner = (Spinner)findViewById(R.id.filterSpinner);
        selectionSpinner.setAdapter(spinnerAdapter);
        selectionSpinner.setEnabled(false);
        selectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ) {

                    Log.i("SPINNER SELECTION", parent.getItemAtPosition(position).toString());
                    String queryStr = parent.getItemAtPosition(position).toString();
                    Uri uriFilter = null;

                    // Create uri to pass to onListUpdate based on the selection
                    uriFilter = Uri.parse("content://" + CollectionProvider.AUTHORITY + "/items/type/" + queryStr);
                    onListUpdate(uriFilter);

                } else {
                    spinSelect = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ListView
        resultsList = (ListView)findViewById(R.id.resultsList);

        // Use saved instance data if available
        if (savedInstanceState != null) {
            Log.i("API BROWSER", "Using Saved Instance");

            writeStatus = (savedInstanceState.getBoolean("status"));
            productList = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("savedList");

            if (productList != null) {
                Log.i("SAVED INSTANCE", "Product list recreation");
                ProductListAdapter adapter = new ProductListAdapter(this, productList, R.layout.list_row,
                        new String[]{"productName", "vendor", "productPrice"}, new int[]{R.id.productName, R.id.vendor, R.id.productPrice});
                // Add the adapter to the ListView
                resultsList.setAdapter(adapter);
                selectionSpinner.setEnabled(true);
                queryAllBtn.setEnabled(true);
            }
        }

        Handler jsonServiceHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //TODO: Handle different types of messages
                String results = null;
                Uri initialUri = null;

                if (msg.arg1 == RESULT_OK && msg.obj != null) {
                    Log.i("JSON HANDLER", "Service Completed Successfully");
                    Log.i("JSON HANDLER", (String) msg.obj);
                    // Obtain json string from service
                    results = (String) msg.obj;

                    // Instantiate FileManager singleton
                    mFile = FileManager.getMinstance();
                    writeStatus = mFile.writeFile(mContext, mJsonFile, results);

                    if (writeStatus) {
                        initialUri = CollectionProvider.JsonData.CONTENT_URI;
                        onListUpdate(initialUri);

                    }

                }

            }
        };

        // Check for file and network status
        if (!writeStatus && status) {

            Messenger jsonServiceMessenger = new Messenger(jsonServiceHandler);
            Intent startJsonDataIntent = new Intent(this, JsonDataService.class);
            startJsonDataIntent.putExtra(JsonDataService.MESSENGER_KEY, jsonServiceMessenger);
            startService(startJsonDataIntent);
        } else if (writeStatus && savedInstanceState == null) {
            onListUpdate(CollectionProvider.JsonData.CONTENT_URI);
        } else if (!status) {
            Toast.makeText(this, "No network connection found and no local data to display.", Toast.LENGTH_LONG).show();
        }


    }

    // Update list with api data
    public void onListUpdate(Uri uri) {

        productList = new ArrayList<HashMap<String, String>>();

        // Get cursor and URI
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        // Check if the cursor returned values
        if (cursor == null) {
            Toast.makeText(this, "Cursor value is null", Toast.LENGTH_LONG).show();
            Log.i("onListUpdate", "NULL CURSOR AT: " + uri.toString());
        }
        // Clear the product list of values if already created
        if (productList != null) {
            productList.clear();
        }

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (int i = 0, j = cursor.getCount(); i < j; i++) {
                    HashMap<String, String> listMap = new HashMap<String, String>();

                    listMap.put("productName", cursor.getString(1));
                    listMap.put("vendor", cursor.getString(2));
                    listMap.put("productPrice", cursor.getString(3));

                    cursor.moveToNext();

                    productList.add(listMap);

                }
            }
        }
        // Create the adapter from the ArrayList of HashMaps and map to the list_row xml layout
        ProductListAdapter adapter = new ProductListAdapter(this, productList, R.layout.list_row,
                new String[]{"productName", "vendor", "productPrice"}, new int[]{R.id.productName, R.id.vendor, R.id.productPrice});
        // Add the adapter to the ListView
        resultsList.setAdapter(adapter);
        selectionSpinner.setEnabled(true);
        queryAllBtn.setEnabled(true);
    }
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
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            }
            else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return view;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.api_browser);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("status", writeStatus);
        savedInstanceState.putBoolean("spinStatus", true);

        if(productList != null && !productList.isEmpty()) {
            savedInstanceState.putSerializable("savedList", (Serializable) productList);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}
