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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


public class Api_browser extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_browser);


        Handler jsonServiceHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //TODO: Handle different types of messages

                if (msg.arg1 == RESULT_OK) {
                    Log.i("JSON HANDLER", "Service Completed");


                }

            }
        };

        Messenger jsonServiceMessenger = new Messenger(jsonServiceHandler);

        Intent startJsonDataIntent = new Intent(this, JsonDataService.class);
        startJsonDataIntent.putExtra(JsonDataService.MESSENGER_KEY, jsonServiceMessenger);
        startService(startJsonDataIntent);
    }


}
