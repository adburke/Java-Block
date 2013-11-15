package com.adburke.java1_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adburke.java1_final.json.JsonRequest;
import com.adburke.java1_final.connect.ConnectStatus;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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

        mContext = this;
        mListItems = getResources().getStringArray(R.array.games_array);

        // Check network connectivity
        Boolean status = ConnectStatus.getNetworkStatus(mContext);
        if (status) {
            URL franchiseUrl = null;
            try {
                franchiseUrl = new URL("http://www.giantbomb.com/api/franchises/?api_key=28bce6b74edc15b89a33b0bb61d7434c3a11f6bb&format=json&limit=10");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            new JsonRequest.getFranchises().execute(franchiseUrl);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.no_conn).setTitle(R.string.warning);
            // Create the AlertDialog object and return it
            builder.show();
        }

        // Create Layout in code
        final LinearLayout projectLayout = new LinearLayout(this);
        projectLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Main Info TextView
        TextView testText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        testText.setLayoutParams(lp);
        testText.setText(R.string.main_text);
        testText.setGravity(Gravity.LEFT);
        projectLayout.addView(testText);

        // ProgressBar
        pb = new ProgressBar(mContext);
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);
        projectLayout.addView(pb);

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mListItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gameSpinner = new Spinner(mContext);
        gameSpinner.setAdapter(spinnerAdapter);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        projectLayout.addView(gameSpinner);

        // ListView
        gamesList = new ListView(this);
        String[] listStr = new String[]{"Choose a franchise"};
        ArrayAdapter listAdap = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, listStr);
        gamesList.setAdapter(listAdap);
        projectLayout.addView(gamesList);

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


                    new JsonRequest.getGames().execute(gamesUrl);
                    Log.i(TAG, gamesUrl.toString());

                    cycle = 1;
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setContentView(projectLayout);


    }
    // Sets the spinner with the api data
    public static void onSpinnerUpdate (ArrayList<String> results) {
        // Set new spinner data from api call
        ArrayAdapter<String> updSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, results);
        updSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(updSpinnerAdapter);
        // Remove Progress bar
        pb.setVisibility(View.GONE);


    }
    // Update list with api data
    public static void onListUpdate (ArrayList<String> results) {
        // Set new list data from api call
        Log.i(TAG, results.toString());
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, results);
        gamesList.setAdapter(newAdapter);

    }

}
