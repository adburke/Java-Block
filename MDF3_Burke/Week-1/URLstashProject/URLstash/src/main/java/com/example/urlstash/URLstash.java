package com.example.urlstash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class URLstash extends Activity {

    private CustomWebView mainWebView;
    private URL incomingUrl;
    private EditText urlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set incoming url to null for checking valid data
        incomingUrl = null;

        // Get ref to editText
        urlEditText = (EditText) findViewById(R.id.urlEditText);

        // Create and configure Custom WebView to provide swipe capabilities
        LinearLayout layout = (LinearLayout) findViewById(R.id.container);
        mainWebView = new CustomWebView(this);
        mainWebView.getSettings().setBuiltInZoomControls(true); // Forced zoom control to super
        layout.addView(mainWebView);
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mainWebView.setWebViewClient(new WebViewClient() {
            // Listen for webview page changes and update the edit text to show the correct url
            public void onPageFinished(WebView view, String url) {
                String formatedUrl = url.replaceAll("(http://|https://)","");
                urlEditText.setText(formatedUrl);
            }
        });

        // Get the intent that started this activity
        Intent intent = getIntent();

        // Pull out the URL data from the intent
        Uri data = intent.getData();
        try {
            if (data != null) {
                incomingUrl = new URL(data.getScheme(), data.getHost(), data.getPath());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Load the url in the WebView and set the EditText to display the url
        if (incomingUrl != null) {
            mainWebView.loadUrl(incomingUrl.toString());
        }

        // Wire up functionality to all of the buttons
        Button webFwdBtn = (Button) findViewById(R.id.webFwdBtn);
        webFwdBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (mainWebView.canGoForward()) {
                    mainWebView.goForward();
                }
            }
        });
        Button webBackBtn = (Button) findViewById(R.id.webBackBtn);
        webBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (mainWebView.canGoBack()) {
                    mainWebView.goBack();
                }
            }
        });
        Button viewStashBtn = (Button) findViewById(R.id.viewStashBtn);
        viewStashBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

            }
        });
        Button addStashBtn = (Button) findViewById(R.id.addStashBtn);
        addStashBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

            }
        });
        Button goBtn = (Button) findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                mainWebView.loadUrl("http://" + urlEditText.getText().toString());
            }
        });

    }

    class CustomWebView extends WebView {
        Context context;
        GestureDetector gestureDetect;

        // Keep up with fling state to check if we need to override or use the webview touch event
        private boolean swipped;

        // Some static variables to calculate gestures in onFling
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public CustomWebView(Context context) {
            super(context);

            this.context = context;
            gestureDetect = new GestureDetector(context, gestureListener);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            gestureDetect.onTouchEvent(event);
            if (swipped) {
                swipped = false;
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            public boolean onDown(MotionEvent event) {
                return true;
            }

            public boolean onFling(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
                // Catch other gesture motions and let the webview handle them
                if (Math.abs(event1.getY() - event1.getY()) > SWIPE_MAX_OFF_PATH) {
                    return false;
                }
                // Catch the left and right swipes so we can override the onTouchEvent with our custom GestureDetector
                if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(distanceX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (mainWebView.canGoForward()) {
                        mainWebView.goForward();
                    }
                    Log.i("Swiped","swipe left");
                    swipped = true;
                } else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(distanceX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (mainWebView.canGoBack()) {
                        mainWebView.goBack();
                    }
                    Log.i("Swiped","swipe right");
                    swipped = true;
                }
                return true;
            }

        };

    }

}
