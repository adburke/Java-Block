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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class Api_browser extends Activity {
    static Context mContext;
    FileManager mFile;
    static String mJsonFile = "json_data.txt";
    // Can be used to tell if the file has already been created on the device
    Boolean writeStatus;

    // Spinner variables
    public static String[] mListItems;
    public static Spinner selectionSpinner;

    // List View
    public static ListView resultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_browser);

        // Initialize context
        mContext = this;

        mListItems = getResources().getStringArray(R.array.selection_array);

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mListItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectionSpinner = (Spinner)findViewById(R.id.filterSpinner);
        selectionSpinner.setAdapter(spinnerAdapter);

        // ListView
        resultsList = (ListView)findViewById(R.id.resultsList);

        Handler jsonServiceHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //TODO: Handle different types of messages
                String results = null;
                Uri initialUri = null;
                writeStatus = false;

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

                    // Used to test FileManager readFile
//                    if (writeStatus) {
//                        m_file.readFile(m_context, m_json_file);
//                    }
                }

            }
        };

        Messenger jsonServiceMessenger = new Messenger(jsonServiceHandler);

        Intent startJsonDataIntent = new Intent(this, JsonDataService.class);
        startJsonDataIntent.putExtra(JsonDataService.MESSENGER_KEY, jsonServiceMessenger);
        startService(startJsonDataIntent);
    }

    // Update list with api data
    public void onListUpdate(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

    }

}
