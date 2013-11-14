package com.adburke.java1_final;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adburke.java1_final.json.JsonRequest;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


public class DataPull extends Activity {
    static final String TAG = "DataPull";
    Context mContext;
    String[] mListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mListItems = getResources().getStringArray(R.array.games_array);

        // Create Layout in code
        LinearLayout projectLayout = new LinearLayout(this);
        projectLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Main Info TextView
        TextView testText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        testText.setLayoutParams(lp);
        testText.setText(R.string.main_text);
        testText.setGravity(Gravity.LEFT);
        projectLayout.addView(testText);

        // Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mListItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner gameSpinner = new Spinner(mContext);
        gameSpinner.setAdapter(spinnerAdapter);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        projectLayout.addView(gameSpinner);

        final TextView resultsText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        resultsText.setLayoutParams(lp);
        resultsText.setSingleLine(false);
        projectLayout.addView(resultsText);

        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Toast.makeText(mContext, "You have selected " + mListItems[position], Toast.LENGTH_LONG).show();

                    String gameInfo = JsonRequest.readJSON(mListItems[position]);

                    URL testUrl = null;
                    String search = "http://www.giantbomb.com/api/search/?api_key=28bce6b74edc15b89a33b0bb61d7434c3a11f6bb&format=json";
                    try {
                        testUrl = new URL(search + "&query=" + '"' + mListItems[position] + '"' + "&resources=franchise");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    new JsonRequest.getData().execute(testUrl);


                    resultsText.setText(gameInfo);
                } else {
                    resultsText.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setContentView(projectLayout);


    }


}
