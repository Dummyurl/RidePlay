package rnf.taxiad.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rnf.taxiad.R;

/**
 * Created by php-android02 on 20/10/16.
 */
public class TermsAndConditionActivitiy extends BaseActivity implements View.OnClickListener {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        setUpViews();
    }

    private void setUpViews() {
        wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebClient());
        wv.loadUrl("file:///android_asset/t&c.html");
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack)
            onBackPressed();
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack())
            wv.goBack();
        else
            super.onBackPressed();
    }
}
