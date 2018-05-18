package rnf.taxiad.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

import rnf.taxiad.R;
import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.Validator;
import rnf.taxiad.helpers.LoginClient;
import rnf.taxiad.locations.LocationService;
import rnf.taxiad.views.StretchVideoView;

/**
 * Created by Rahil on 24/12/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtPassword;
    private ViewAnimator animator;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        onReceived(getIntent());

    }

    private void onReceived(Intent intent) {
        stopService(new Intent(this, LocationService.class));
        if (!intent.hasExtra("message"))
            return;
        String message = intent.getStringExtra("message");
        if (message != null) {
            if (alertDialog == null)
                createAlert();
            alertDialog.setMessage(message);
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).
                    setTextColor(ContextCompat.getColor(this, R.color.color_button));
        }
    }

    private void createAlert() {
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setPositiveButton("OK", null);
        b.setCancelable(false);
        alertDialog = b.create();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        onReceived(intent);
    }

    private void setupViews() {
        animator = (ViewAnimator) findViewById(R.id.animator);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        final TextView tvGoToSignup = (TextView) findViewById(R.id.tvGoToSignup);
        tvGoToSignup.setText(Html.fromHtml(getString(R.string.go_to_signup)));
        tvGoToSignup.setOnClickListener(this);
        final TextView tvGoToForgotPassword = (TextView) findViewById(R.id.tvGoToForgotPassword);
        tvGoToForgotPassword.setText(Html.fromHtml(getString(R.string.go_to_forgot_password)));
        tvGoToForgotPassword.setOnClickListener(this);
        findViewById(R.id.tvLogin).setOnClickListener(this);
        edtPassword.setOnEditorActionListener(editorActionListener);
    }

    private void login() {
        GlobalUtils.hideKeyboard(edtPassword);
        final EditText edtUsername = (EditText) findViewById(R.id.edtUsername);
        if (Validator.isValid(edtUsername, Validator.Type.EMAIL)
                && Validator.isValid(edtPassword, Validator.Type.PASSWORD)) {
            client.setParams(edtUsername.getText().toString().trim(), edtPassword.getText().toString());
            client.makeRequest();
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvLogin)
            login();
        else if (v.getId() == R.id.tvGoToSignup)
            startActivity(SignupActivity.class);
        else if (v.getId() == R.id.tvGoToForgotPassword)
            startActivity(ForgotPasswordActivity.class);
    }

    private LoginClient client = new LoginClient(this) {

        @Override
        public void onStarted() {

            if (animator != null)
                animator.setDisplayedChild(1);
        }

        @Override
        public void onError(String error) {
            if (animator != null)
                animator.setDisplayedChild(0);

        }

        @Override
        public void onSuccess() {
            if (animator == null)
                return;
            String md5String = GlobalUtils.convertStringIntoMd5(edtPassword.getText().toString());
            AppPreferenceManager manager = AppPreferenceManager.getInstance(LoginActivity.this);
            manager.saveString("md5Pwd", md5String);
            startActivityClearAll(HomeActivity.class);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (client != null)
            client.cancel();
    }
}
