/*
 * Project:		Java2_p1
 *
 * Package:		Java2_p1
 *
 * Author:		Aaron Burke
 *
 * Date:		12/3/2013
 */

package com.adburke.java2_p1;

import android.content.Context;

import org.json.JSONObject;

public class FileManager {

    // Singleton Creation
    private static FileManager m_instance;
    private FileManager(){
        // Constructor empty for singleton
    }
    // Check if m_instance is null, if so create the singleton, otherwise it is created already
    public static FileManager getMinstance() {
        if (m_instance == null) {
            m_instance = new FileManager();
        }
        return m_instance;
    }


    public boolean WriteFile(Context context, String filename, String content) {
        boolean result = false;



        return result;
    }

    public String ReadFile() {
        String result = null;


        
        return result;
    }


}
