/*
 * Project:		Java2_p1
 *
 * Package:		Java2_p1
 *
 * Author:		Aaron Burke
 *
 * Date:		12/2/2013
 */

package com.adburke.java2_p1;

import android.app.IntentService;
import android.content.Intent;

public class JsonDataService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public JsonDataService() {
        super("JsonDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
