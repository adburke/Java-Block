package com.adburke.java1_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.adburke.java1_final.json.JsonRequest.*;
import com.adburke.java1_final.json.JsonRequest;
import com.example.java1_final.ConnectStatus;

/**
 * Created by aaronburke on 11/7/13.
 * Java1 Week 3
 */

public class DataPull extends Activity {
    static final String TAG = "DataPull";
    public static Context mContext;
    public static String[] mListItems;
    private static int cycle = 0;

    public static Spinner gameSpinner;
    public static ProgressBar pb;
    public static ListView gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_pull_main);

        mContext = this;
        mListItems = getResources().getStringArray(R.array.games_array);

        // Check network connectivity
        Boolean status = ConnectStatus.getNetworkStatus(mContext);
        if (status) {
            // Proceed with API with valid connection
            URL franchiseUrl = null;
            try {
                franchiseUrl = new URL("http://www.giantbomb.com/api/franchises/?api_key=28bce6b74edc15b89a33b0bb61d7434c3a11f6bb&format=json&limit=10");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            new JsonRequest.getFranchises().execute(franchiseUrl);
        } else {
            // Create the AlertDialog object and return it
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.no_conn).setTitle(R.string.warning);
            builder.show();
        }

        // ProgressBar
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mListItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner = (Spinner)findViewById(R.id.gamesSpinner);
        gameSpinner.setAdapter(spinnerAdapter);

        // ListView
        gamesList = (ListView)findViewById(R.id.gamesList);
        String[] listStr = new String[]{""};
        ArrayAdapter listAdap = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, listStr);
        gamesList.setAdapter(listAdap);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 || cycle == 1) {
                    Toast.makeText(mContext, "You have selected " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();

                    // Creates a url String from the selected spinner option
                    String gamesStr = null;
                    String search = "http://www.giantbomb.com/api/search/?api_key=28bce6b74edc15b89a33b0bb61d7434c3a11f6bb&format=json";
                    String queryStr = parent.getItemAtPosition(position).toString();
                    URL gamesUrl = null;
                    try {
                        // Added encoding for franchises with spaces in them
                        gamesStr = search + "&query=" + '"' + URLEncoder.encode(queryStr, "UTF-8") + '"' + "&resources=franchise";
                        gamesUrl = new URL(gamesStr);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                    new getGames().execute(gamesUrl);
                    Log.i(TAG, gamesUrl.toString());

                    cycle = 1;
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Sets the spinner with the api data
    public static void onSpinnerUpdate(ArrayList<String> results) {
        // Set new spinner data from api call
        ArrayAdapter<String> updSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, results);
        updSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(updSpinnerAdapter);
        // Remove Progress bar
        pb.setVisibility(View.GONE);


    }
    // Update list with api data
    public static void onListUpdate(ArrayList<String> results) {
        // Set new list data from api call
        Log.i(TAG, results.toString());
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, results);
        gamesList.setAdapter(newAdapter);

    }

}
