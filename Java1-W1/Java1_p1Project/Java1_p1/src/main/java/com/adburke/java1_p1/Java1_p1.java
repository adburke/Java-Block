package com.adburke.java1_p1;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Java1_p1 extends Activity {

    // Testing out logging
    final String TAG = "GuessGame";

    // Holds results of tries in a 2D List format: [[guess,actual,win/loss(0 or 1)],[guess,actual,win/loss(0 or 1)]]
    final List<List<Integer>> gameResults = new ArrayList<List<Integer>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Layout in code
        LinearLayout projectLayout = new LinearLayout(this);
        projectLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        // Game Info TextView
        TextView testText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        testText.setLayoutParams(lp);
        testText.setText(R.string.main_text);
        testText.setGravity(Gravity.CENTER);
        projectLayout.addView(testText);

        // Input Message TextView
        final TextView msgText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        msgText.setLayoutParams(lp);
        // Retrieve string array from strings.xml
        Resources res = getResources();
        final String[] messages = res.getStringArray(R.array.msg_array);
        msgText.setText(messages[0]);
        projectLayout.addView(msgText);

        // EditText
        final EditText inputTxt = new EditText(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inputTxt.setLayoutParams(lp);
        // Allows only Numbers in the EditText
        inputTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        // Limits the number of characters to 2
        inputTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        projectLayout.addView(inputTxt);

        // Submit Button
        final Button btn = new Button(this);
        btn.setLayoutParams(lp);
        btn.setText(R.string.btn_text);
        btn.setEnabled(false);
        projectLayout.addView(btn);

        // Game results TextView
        // Game results textView which needs to be modified by displayResults()
        final TextView resultsText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        resultsText.setLayoutParams(lp);
        resultsText.setText(R.string.result_header);
        resultsText.setSingleLine(false);
        projectLayout.addView(resultsText);

        // Button onClick event
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = Integer.parseInt(inputTxt.getText().toString());
                boolean result = checkInput(val);

                // Check return value of comparison and set msgText accordingly
                if (result) {
                    msgText.setText(messages[4]);

                } else {
                    msgText.setText(messages[5]);

                }
                // Show results below button
                displayResults(resultsText);
            }
        });


        // Event listener for the EditText field
        inputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check to see if s is a blank string
                // Disable the submit button for a blank value
                if (s.toString().equals("")) {
                    msgText.setText(messages[0]);
                    btn.setEnabled(false);

                } else {
                    // Check to see if the input is larger than 10 or 0
                    // Enable or disable the submit button accordingly
                    int val = Integer.parseInt(s.toString());
                    if (val > 10) {
                        msgText.setText(messages[2]);
                        btn.setEnabled(false);
                    } else if (val < 1) {
                        msgText.setText(messages[1]);
                        btn.setEnabled(false);

                    } else if (val >= 1 || val <= 10) {
                        msgText.setText(messages[3]);
                        btn.setEnabled(true);
                    }

                }
            }
        });


        setContentView(projectLayout);
    }

    // Compare input function
    public boolean checkInput(int x) {
        Log.i(TAG, "x = " + x);

        // Create our random number on each try
        Random randomNumber = new Random();
        int actualNum = randomNumber.nextInt((10 - 1) + 1) + 1;
        Log.i(TAG, "actual = " + actualNum);

        // Check the arrayList size to increment the 2D row with new guess and actual values
        Log.i(TAG, "gameResults rows = " + gameResults.size());
        int size = gameResults.size();
        gameResults.add(new ArrayList<Integer>());
        gameResults.get(size).add(x);
        gameResults.get(size).add(actualNum);
        if (x == actualNum) {
            gameResults.get(size).add(1);
        } else {
            gameResults.get(size).add(0);
        }


        // Simplified if else for the return
        return (x == actualNum);

    }

    // Method to display results
    public void displayResults(TextView txtView) {
        int size = gameResults.size();

        for (int i = size - 1, j = size; i < j; i++) {
            String newstring =  " \n" + "Guess: " + gameResults.get(i).get(0) + " Actual: " + gameResults.get(i).get(1);
            if (gameResults.get(i).get(2) == 0) {
                appendColoredText(txtView, newstring, Color.RED);
            } else {
                appendColoredText(txtView, newstring, Color.GREEN);
            }
        }
    }

    // Method to color results
    public static void appendColoredText(TextView txtView, String text, int color) {
        int start = txtView.getText().length();
        txtView.append(text);
        int end = txtView.getText().length();

        Spannable spannableText = (Spannable) txtView.getText();
        spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
    }

}
