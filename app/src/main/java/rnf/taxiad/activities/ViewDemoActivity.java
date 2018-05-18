package rnf.taxiad.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rnf.taxiad.R;
import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.views.TimerLayout;

/**
 * Created by rnf-new on 31/3/17.
 */

public class ViewDemoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout authView;
    private EditText edtPassword;
    private TextView tvPwdAlert;
    private ProgressBar progressBar;
    private TimerLayout timerLayout;
    String md5Pwd;
    String demourl = "https://www.youtube.com/watch?v=dx6gRLPXnHc";
    WebView webView;
    private String videoTitle;
    private int loadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demo);
        timerLayout = (TimerLayout) findViewById(R.id.timerLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView);
        authView = (RelativeLayout) findViewById(R.id.authView);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tvPwdAlert = (TextView) findViewById(R.id.tvPwdAlert);
        findViewById(R.id.btnOk).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        AppPreferenceManager manager = AppPreferenceManager.getInstance(this);
        md5Pwd = manager.getString("md5Pwd", "");
        edtPassword.addTextChangedListener(edtPasswordWatcher);
    }

    private void loadView() {
        progressBar.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(demourl);
        if (timerLayout != null)
            timerLayout.stopTimer();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel) {
            finish();
        }
        if (v.getId() == R.id.btnOk) {
            verifyPassword();
        }


    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, demourl);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, demourl, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.equalsIgnoreCase(demourl))
                super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

    };


    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            loadProgress = newProgress;
            if (newProgress == 100 && videoTitle.equalsIgnoreCase("RIDEPLAY tv | Advertisers Dashboard - YouTube")) {

            }

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            videoTitle = title;
            if (loadProgress == 100 && !videoTitle.equalsIgnoreCase("RIDEPLAY tv | Advertisers Dashboard - YouTube")) {
                webView.loadUrl(demourl);
            }
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
        }

        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            super.getVisitedHistory(callback);
        }
    };

    private void verifyPassword() {
        if (edtPassword.getText().toString().trim().length() == 0) {
            tvPwdAlert.setVisibility(View.VISIBLE);
            tvPwdAlert.setText("Enter the password.");
            return;
        }

        if (md5Pwd.equals(GlobalUtils.convertStringIntoMd5(edtPassword.getText().toString()))) {
            GlobalUtils.hideKeyboard(edtPassword);
            authView.setVisibility(View.GONE);
            loadView();
        } else {
            tvPwdAlert.setVisibility(View.VISIBLE);
            tvPwdAlert.setText("Password is incorrect.");
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private TextWatcher edtPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (timerLayout != null)
                timerLayout.setMyCountDownTimer();

            if (tvPwdAlert.getVisibility() == View.VISIBLE) {
                tvPwdAlert.setVisibility(View.GONE);
                tvPwdAlert.setText("");
            }
        }
    };
}
