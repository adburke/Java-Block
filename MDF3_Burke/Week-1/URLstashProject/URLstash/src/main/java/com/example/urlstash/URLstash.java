package com.example.urlstash;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;


public class URLstash extends Activity {

    private WebView mainWebView;
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

        // Create and configure WebView
        mainWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mainWebView.setWebViewClient(new WebViewClient());

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
            String url = incomingUrl.toString().replaceAll("(http://)","");
            urlEditText.setText(url);
        }
    }

    @Override
    // Method to use the android back button for the WebView
    public void onBackPressed() {
        if(mainWebView.canGoBack()) {
            mainWebView.goBack();
        } else {
            // System back instead of WebView back
            super.onBackPressed();
        }
    }


}
