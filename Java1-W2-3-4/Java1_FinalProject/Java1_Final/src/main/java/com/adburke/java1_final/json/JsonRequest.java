package com.adburke.java1_final.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by aaronburke on 11/7/13.
 */
public class JsonRequest {


    public static JSONObject buildJSON() {
        final String TAG = "GameLog";
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
        JSONObject response;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create JSON object from string result
        try {
            response = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}
