package com.adburke.java1_final;

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

public class DataPull extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Layout in code
        LinearLayout projectLayout = new LinearLayout(this);
        projectLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Main Info TextView
        TextView testText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        testText.setLayoutParams(lp);
        testText.setText(R.string.main_text);

        testText.setGravity(Gravity.CENTER);
        projectLayout.addView(testText);

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




        setContentView(projectLayout);


    }


}
