package rnf.taxiad.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.sql.Time;

import rnf.taxiad.R;
import rnf.taxiad.Utility.ColoredSnackbar;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.Validator;
import rnf.taxiad.helpers.CheckOutClient;
import rnf.taxiad.helpers.TextFeatureClient;
import rnf.taxiad.views.TimerChangeListener;
import rnf.taxiad.views.TimerLayout;

/**
 * Created by rnf-new on 30/11/16.
 */

public class TextToFeatureActivity extends BaseActivity implements View.OnClickListener {

    private TextView btnSend;
    private TextView tvFeatureText;
    private String adsId;
    private EditText edtPhone;
    private ViewAnimator animator;
    private TimerLayout timerLayout;
   // private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_info);
        getIntentData();
        setView();
    }

    private void getIntentData() {
        if (getIntent().hasExtra("adsId"))
            adsId = getIntent().getStringExtra("adsId");
    }

    private void setView() {
        findViewById(R.id.relativeRefrenceDriver).setVisibility(View.GONE);
        timerLayout = (TimerLayout) findViewById(R.id.timerLayout);
        animator = (ViewAnimator) findViewById(R.id.animator);
        btnSend = (TextView) findViewById(R.id.btnSend);
        tvFeatureText = (TextView) findViewById(R.id.tvFeatureText);
        tvFeatureText.setText("Enter your cell phone no and we'll text you a link with more information and a way to get started with us.");
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPhone.setHint("Enter Your Phone Number");
        btnSend.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        edtPhone.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtPhone.setOnEditorActionListener(editorActionListener);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (adsId != null && !adsId.isEmpty())
                sendTextToFeature();
            else
                sendCheckOutLink();
        } else if (v.getId() == R.id.btnBack) {
            onBackPressed();
        }
    }


    private void sendTextToFeature() {
        if (edtPhone.getText().length() == 0) {
            ColoredSnackbar.alert(Snackbar.make(TextToFeatureActivity.this.findViewById(android.R.id.content),
                    "Please Enter the Phone No", Snackbar.LENGTH_LONG)).show();
            return;
        }

        GlobalUtils.hideKeyboard(edtPhone);
        if (Validator.isValid(edtPhone, Validator.Type.PHONE)
                ) {
            textFeatureClient.setParams(edtPhone.getText().toString(), adsId);
            textFeatureClient.makeRequest();
        }
    }


    private void sendCheckOutLink() {
        if (edtPhone.getText().length() == 0) {
            ColoredSnackbar.alert(Snackbar.make(TextToFeatureActivity.this.findViewById(android.R.id.content),
                    "Please Enter the Phone No", Snackbar.LENGTH_LONG)).show();
            return;
        }

        GlobalUtils.hideKeyboard(edtPhone);
        if (Validator.isValid(edtPhone, Validator.Type.PHONE)
                ) {
            checkOutClient.setParams(edtPhone.getText().toString());
            checkOutClient.makeRequest();
        }
    }

    private TextFeatureClient textFeatureClient = new TextFeatureClient(this) {
        @Override
        public void onStarted() {
            timerLayout.stopTimer();
            animator.setDisplayedChild(1);
        }

        @Override
        public void onError() {
            timerLayout.setMyCountDownTimer();
            animator.setDisplayedChild(0);
        }

        @Override
        public void onSuccess(String message) {
            timerLayout.setMyCountDownTimer();
            animator.setDisplayedChild(0);
           /* if (alertDialog == null)
                createAlert();
            alertDialog.setMessage(message);
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext()
                    , R.color.color_button));*/
        }
    };

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (adsId != null && !adsId.isEmpty())
                    sendTextToFeature();
                else
                    sendCheckOutLink();
                return true;
            }
            return false;
        }
    };

    private CheckOutClient checkOutClient = new CheckOutClient(TextToFeatureActivity.this) {
        @Override
        public void onStarted() {
            timerLayout.stopTimer();
            animator.setDisplayedChild(1);
        }

        @Override
        public void onError() {
            timerLayout.setMyCountDownTimer();
            animator.setDisplayedChild(0);
        }

        @Override
        public void onSuccess(String message) {
            timerLayout.setMyCountDownTimer();
            animator.setDisplayedChild(0);
        }
    };


    /*private void createAlert() {
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setPositiveButton("OK", dialogClick);
        alertDialog = b.create();
    }

    private DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
            onBackPressed();
        }
    };*/
}
