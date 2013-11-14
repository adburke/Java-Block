package com.adburke.java1_final.json;

import android.os.AsyncTask;
import android.util.Log;

import com.adburke.java1_final.DataPull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by aaronburke on 11/7/13.
 */
public class JsonRequest {
    static final String TAG = "JsonRequest";

    public static JSONObject buildJSON() {

            // Overall data object
            JSONObject dataObject = new JSONObject();

            // Array of results within reults object
            JSONArray resultsArray = new JSONArray();

        try {
            for (Games game : Games.values()) {
                // Game Object
                JSONObject gameData = new JSONObject();

                // JSONArray to hold platforms string array
                JSONArray platforms = new JSONArray();

                gameData.put("name", game.getName());

                // Convert incoming string array to JSONArray
                for (int i = 0, j = game.getPlatforms().length; i < j; i++) {
                    platforms.put(game.getPlatforms()[i]);
                }
                gameData.put("platforms", platforms);

                gameData.put("original_release_date", game.getReleaseDate());

                // Add gameData object to resultsArray
                resultsArray.put(gameData);
            }
                // Place resultArray in dataObject
                dataObject.put("results",resultsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, dataObject.toString());

        return dataObject;


    }

    public static String readJSON(String selected) {
        String name = null, releaseDate = null, resultString = null, platformStr = "";
        JSONArray platforms = null;

        JSONObject dataResults = buildJSON();

        try {
            JSONArray results = dataResults.getJSONArray("results");
            // Filter out results to only use what matches selected
            int j = results.length();
            for (int i = 0; i < j; i++) {
                if (results.getJSONObject(i).getString("name").equals(selected)) {
                    name = results.getJSONObject(i).getString("name");
                    platforms = results.getJSONObject(i).getJSONArray("platforms");
                    releaseDate = results.getJSONObject(i).getString("original_release_date");

                    break;
                }
            }

            // Create string from platforms array
            if (platforms != null) {
                int x = platforms.length();
                for (int i = 0; i < x; i++) {
                    if (i == 0){
                        try {
                            platformStr = platforms.getString(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            platformStr = platformStr + ", " +platforms.getString(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }


            resultString = "Name: " + name + "\r\n"
                    + "Platforms: " + platformStr + "\r\n"
                    + "Release Year: " + releaseDate + "\r\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultString;

    }

    public static JSONObject getJSONResponse(URL url) {
        String result = "";
        JSONObject response = null;

        // Read JSON response from the API
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            //Log.i(TAG, result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create JSON object from string result
        try {
            response = new JSONObject(result);
            //Log.i(TAG, response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static class getData extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            //String responseString = "";
            JSONObject response = null;
            for (URL url : urls) {
                response = getJSONResponse(url);
            }

            return response;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            Log.i(TAG, jsonObject.toString());
            super.onPostExecute(jsonObject);
        }
    }
}
