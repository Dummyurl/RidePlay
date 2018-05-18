package rnf.taxiad.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import rnf.taxiad.R;
import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.activities.ManageAccountActivity;

/**
 * Created by Rahul Agrahari on 15/12/16.
 */

public class AuthDialog extends Dialog implements View.OnClickListener {

    Context mContext;
    EditText edtPassword;
    String md5Pwd;
    TextView tvPwdAlert;
    TimerLayout timerLayout;

    public AuthDialog(Context context, TimerLayout timerLayout) {
        super(context);
        this.mContext = context;
        this.timerLayout = timerLayout;
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_dialog);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#30000000")));
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        AppPreferenceManager manager = AppPreferenceManager.getInstance(mContext);
        md5Pwd = manager.getString("md5Pwd", "");
        findView();
    }


    private void findView() {
        findViewById(R.id.btnOk).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tvPwdAlert = (TextView) findViewById(R.id.tvPwdAlert);
        edtPassword.addTextChangedListener(edtPasswordWatcher);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOk) {
            verifyPassword();
        }

        if (v.getId() == R.id.btnCancel) {
            ((ManageAccountActivity) mContext).finish();

        }

    }

    private void verifyPassword() {
        if (edtPassword.getText().toString().trim().length() == 0) {
            tvPwdAlert.setVisibility(View.VISIBLE);
            tvPwdAlert.setText("Enter the password.");
            return;
        }

        if (md5Pwd.equals(GlobalUtils.convertStringIntoMd5(edtPassword.getText().toString())))
            dismiss();
        else {
            tvPwdAlert.setVisibility(View.VISIBLE);
            tvPwdAlert.setText("Password is incorrect.");
        }


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
            if (timerLayout != null) {
                timerLayout.setMyCountDownTimer();
            }
            if (tvPwdAlert.getVisibility() == View.VISIBLE) {
                tvPwdAlert.setVisibility(View.GONE);
                tvPwdAlert.setText("");
            }
        }
    };
}
