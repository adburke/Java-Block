package com.adburke.java1_p1;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Java1_p1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Layout in code
        LinearLayout projectLayout = new LinearLayout(this);
        projectLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        // TextView
        TextView testText = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        testText.setLayoutParams(lp);
        testText.setText(R.string.main_text);
        projectLayout.addView(testText);

        // Button
        Button btn = new Button(this);
        btn.setLayoutParams(lp);
        btn.setText(R.string.hello_world);
        projectLayout.addView(btn);

        



        setContentView(projectLayout);
    }

}
