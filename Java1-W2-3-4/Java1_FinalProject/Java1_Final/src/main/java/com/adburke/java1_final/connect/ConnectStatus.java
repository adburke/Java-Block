package com.adburke.java1_final.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by aaronburke on 11/7/13.
 * Java1 Week 3
 */
public class ConnectStatus {

    public static Boolean getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get network info object
        NetworkInfo currentNet = cm.getActiveNetworkInfo();
        Boolean status = false;
        // Check the network info object for connection
        if(currentNet != null) {
            if(currentNet.isConnectedOrConnecting()) {
                status = true;
            }
        }
        return status;
    }

    public static String getNetworkStatusType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get network info object
        NetworkInfo currentNet = cm.getActiveNetworkInfo();
        String connType = null;
        if(currentNet != null) {
            if (currentNet.getType() == ConnectivityManager.TYPE_WIFI) {
                connType = "Wifi enabled";
            } else if (currentNet.getType() == ConnectivityManager.TYPE_MOBILE) {
                connType = "Mobile data enabled";
            }
        }
        return connType;
    }
}
