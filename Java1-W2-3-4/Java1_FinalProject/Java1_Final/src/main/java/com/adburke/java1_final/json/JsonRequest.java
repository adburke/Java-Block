package com.adburke.java1_final.json;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.adburke.java1_final.DataPull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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



    public static ArrayList<String> readJSONList(JSONObject dataResults) {
        ArrayList<String> names = new ArrayList<String>();
        String name = null;
        JSONArray platforms = null;
        if (dataResults != null) {
            try {
                JSONArray results = dataResults.getJSONArray("results");
                // Filter out results to only use what matches selected
                int j = results.length();
                for (int i = 0; i < j; i++) {
                    name = results.getJSONObject(i).getString("name");
                    names.add(i, name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return names;
        }
        return names;
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
    // Async for franchise api call
    public static class getFranchises extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            //String responseString = "";
            JSONObject response = null;
            ArrayList<String> franchises = new ArrayList<String>();
            for (URL url : urls) {
                response = getJSONResponse(url);
            }
            franchises = readJSONList(response);

            return franchises;
        }

        @Override
        protected void onPostExecute(ArrayList<String> names) {
            DataPull.onSpinnerUpdate(names);
            Log.i(TAG, names.toString());
            super.onPostExecute(names);
        }


    }
    // Async for games in franchise call
    public static class getGames extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            //String responseString = "";
            JSONObject response = null;
            ArrayList<String> games = new ArrayList<String>();
            for (URL url : urls) {
                response = getJSONResponse(url);
            }
            games = readJSONList(response);

            return games;
        }

        @Override
        protected void onPostExecute(ArrayList<String> names) {

            DataPull.onListUpdate(names);
            Log.i(TAG, names.toString());
            super.onPostExecute(names);
        }
    }
}
