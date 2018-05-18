package rnf.taxiad.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

import rnf.taxiad.R;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.Validator;
import rnf.taxiad.helpers.ForgotPasswordClient;

/**
 * Created by Rahil on 19/1/16.
 */
public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener,
        DialogInterface.OnClickListener {

    private EditText edtEmail;
    private ViewAnimator animator;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setupViews();
    }

    private void setupViews() {
        animator = (ViewAnimator) findViewById(R.id.animator);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.setOnEditorActionListener(editorActionListener);
        final TextView tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                recover();
                return true;
            }
            return false;
        }
    };

    private void recover() {
        GlobalUtils.hideKeyboard(edtEmail);
        if (Validator.isValid(edtEmail, Validator.Type.EMAIL)
                ) {
            client.setParams(edtEmail.getText().toString().trim());
            client.makeRequest();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSubmit)
            recover();
        else if (v.getId() == R.id.btnBack)
            onBackPressed();
    }

    private ForgotPasswordClient client = new ForgotPasswordClient(this) {
        @Override
        public void onStarted() {
            if (animator != null)
                animator.setDisplayedChild(1);
        }

        @Override
        public void onError() {
            if (animator != null)
                animator.setDisplayedChild(0);
        }

        @Override
        public void onSuccess(String message) {
            if (animator == null)
                return;
            animator.setDisplayedChild(0);
            if (message == null)
                return;
            if (alertDialog == null)
                createDialog();
            alertDialog.setMessage(message);
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).
                    setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_button));
//            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (client != null)
            client.cancel();
    }

    private void createDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setPositiveButton("OK", this);
        alertDialog = b.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        onBackPressed();
    }
}
