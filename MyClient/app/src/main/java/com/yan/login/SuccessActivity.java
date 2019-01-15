package com.yan.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SuccessActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.success);

        WebView success = findViewById(R.id.success);      //bushi layout

        success.getSettings().setJavaScriptEnabled(true);
        success.getSettings().setAppCacheEnabled(true);
        success.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        success.getSettings().setDomStorageEnabled(true);

        success.loadUrl("http://www.yanblog.info/");


        success.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });



    }

}
