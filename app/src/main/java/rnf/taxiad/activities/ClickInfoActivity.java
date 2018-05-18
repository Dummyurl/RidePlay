package rnf.taxiad.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import rnf.taxiad.R;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.Validator;
import rnf.taxiad.adapters.DialCodeAdapter;
import rnf.taxiad.helpers.PhoneNumberUtils;
import rnf.taxiad.helpers.SendMailToAdmin;
import rnf.taxiad.views.TimerChangeListener;
import rnf.taxiad.views.TimerLayout;

/**
 * Created by Rahil on 7/3/16.
 */
public class ClickInfoActivity extends BaseActivity implements View.OnClickListener {

    private ViewAnimator ViewAnimator;
    private EditText edtEmail;
    private AlertDialog alertDialog;
    private AppCompatSpinner dialCodeSpinner;
    private TimerLayout timerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_info);
        setupViews();
    }

    private void setupViews() {
        final PhoneNumberUtils phoneNumberUtils = new PhoneNumberUtils(this);
        timerLayout=(TimerLayout)findViewById(R.id.timerLayout);
        dialCodeSpinner = (AppCompatSpinner) findViewById(R.id.dialCodeSpinner);
        DialCodeAdapter codeAdapter = new DialCodeAdapter(this, phoneNumberUtils.phoneNumbers);
        dialCodeSpinner.setAdapter(codeAdapter);
        edtEmail = (EditText) findViewById(R.id.edtPhone);
        ViewAnimator = (ViewAnimator) findViewById(R.id.animator);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnSend).setOnClickListener(this);
        edtEmail.setOnEditorActionListener(editorActionListener);
        edtEmail.addTextChangedListener(new TimerChangeListener(timerLayout));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack)
            onBackPressed();
        else if (v.getId() == R.id.btnSend)
            sendEmail();
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendEmail();
                return true;
            }
            return false;
        }
    };

    private void sendEmail() {
        String reference = "No";
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        reference = radioGroup.getCheckedRadioButtonId() == R.id.radioYes ? "Yes" : "No";
        int referenceoption=reference.equalsIgnoreCase("Yes") ? 1 : 0;
        GlobalUtils.hideKeyboard(edtEmail);
        if (Validator.isValid(edtEmail, Validator.Type.EMAIL)
                ) {
//            PhoneNumberUtils.PhoneNumbers phoneNumbers = (PhoneNumberUtils.PhoneNumbers) dialCodeSpinner.getSelectedItem();
//            client.setParams(phoneNumbers.dial_code + edtPhone.getText().toString(), reference);
            client.setParams(edtEmail.getText().toString(), String.valueOf(referenceoption));
            client.makeRequest();
        }
    }

    private SendMailToAdmin client = new SendMailToAdmin(this) {
        @Override
        public void onStarted() {
            if (ViewAnimator != null)
                ViewAnimator.setDisplayedChild(1);
        }

        @Override
        public void onError() {
            if (ViewAnimator != null)
                ViewAnimator.setDisplayedChild(0);
        }

        @Override
        public void onSuccess(String message) {
            if (ViewAnimator == null)
                return;
            ViewAnimator.setDisplayedChild(0);
            if (alertDialog == null)
                createAlert();
            alertDialog.setMessage(message);
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext()
                    , R.color.color_button));
        }
    };

    @Override
    public void onBackPressed() {
        if (client != null)
            client.cancel();
        super.onBackPressed();
    }

    private void createAlert() {
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
    };

}
