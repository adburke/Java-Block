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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


public class Api_browser extends Activity {
    Context m_context;
    FileManager m_file;
    String m_json_file = "json_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_browser);

        // Initialize context
        m_context = this;

        Handler jsonServiceHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //TODO: Handle different types of messages
                String results = null;

                Boolean writeStatus = false;

                if (msg.arg1 == RESULT_OK && msg.obj != null) {
                    Log.i("JSON HANDLER", "Service Completed Successfully");
                    Log.i("JSON HANDLER", (String) msg.obj);
                    // Obtain json string from service
                    results = (String) msg.obj;

                    // Instantiate FileManager singleton
                    m_file = FileManager.getMinstance();
                    writeStatus = m_file.writeFile(m_context, m_json_file, results);
                }

            }
        };

        Messenger jsonServiceMessenger = new Messenger(jsonServiceHandler);

        Intent startJsonDataIntent = new Intent(this, JsonDataService.class);
        startJsonDataIntent.putExtra(JsonDataService.MESSENGER_KEY, jsonServiceMessenger);
        startService(startJsonDataIntent);
    }


}
