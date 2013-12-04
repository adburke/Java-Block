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
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;

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


    public Boolean writeFile(Context context, String filename, String content) {
        Log.i("FILEMANAGER", "Starting writeFile");
        Log.i("FILEMANAGER", "filename: " + filename);

        Boolean result = false;
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            result = true;
            Log.i("FILEMANAGER", "Success");
        } catch (Exception e) {
            Log.i("FILEMANAGER", "Write error: " + e.toString());
            e.printStackTrace();
        }


        return result;
    }

    public String readFile() {
        String result = null;



        return result;
    }


}
