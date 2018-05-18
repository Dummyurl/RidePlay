package rnf.taxiad.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

import rnf.taxiad.R;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.Validator;
import rnf.taxiad.helpers.RegistrationClient;
import rnf.taxiad.models.Driver;
import rnf.taxiad.models.LocationModule;
import rnf.taxiad.views.CabServicesView;
import rnf.taxiad.views.CommentBox;

/**
 * Created by Rahil on 24/12/15.
 */
public class SignupActivity extends LocationServicesActivity implements View.OnClickListener,
        DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private AppCompatSpinner spinnerTrips, tabletTypeSpinner, tabletSizeSpinner, citySpinner, spinnerInterested;
    private ViewAnimator animator;
    private AlertDialog alertDialog, alert;
    private TextView btnSignup;
    private LocationModule locationModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupViews();
    }

    private void setupViews() {
        setupSpinners();
        animator = (ViewAnimator) findViewById(R.id.animator);
        btnSignup = (TextView) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        final AppCompatCheckBox driverTerms = (AppCompatCheckBox) findViewById(R.id.driverTerms);
        driverTerms.setOnCheckedChangeListener(this);
        setClickabkeSpan();

        final WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/t&ctrans.html");
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    private void setClickabkeSpan() {
        final TextView tvUserTerms = (TextView) findViewById(R.id.tvUserTerms);
        SpannableString ss = new SpannableString(tvUserTerms.getText().toString());
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#FFFF00"));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(TermsAndConditionActivitiy.class);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 15, tvUserTerms.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvUserTerms.setMovementMethod(LinkMovementMethod.getInstance());
        ss.setSpan(fcs, 15, tvUserTerms.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvUserTerms.setText(ss);
    }

    @Override
    public void onLocationChanged(LocationModule locationModule) {
        super.onLocationChanged(locationModule);
        this.locationModule = locationModule;
    }

    private void setupSpinners() {
        spinnerTrips = (AppCompatSpinner) findViewById(R.id.spinnerTrips);
        tabletTypeSpinner = (AppCompatSpinner) findViewById(R.id.tabletTypeSpinner);
        tabletSizeSpinner = (AppCompatSpinner) findViewById(R.id.tabletSizeSpinner);
        citySpinner = (AppCompatSpinner) findViewById(R.id.citySpinner);
        spinnerInterested = (AppCompatSpinner) findViewById(R.id.spinnerInterested);
        final ArrayAdapter<String> tripsAdapter = new ArrayAdapter<String>(this, R.layout.view_simple_spinner_item,
                getResources().getStringArray(R.array.trips_per_week));
        final ArrayAdapter<String> tabletTypeAdapter = new ArrayAdapter<String>(this, R.layout.view_simple_spinner_item,
                getResources().getStringArray(R.array.type_of_tablet));
        final ArrayAdapter<String> tabletSizeAdapter = new ArrayAdapter<String>(this, R.layout.view_simple_spinner_item,
                getResources().getStringArray(R.array.size_of_tablet));
        final ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.view_simple_spinner_item,
                getResources().getStringArray(R.array.cities));
        final ArrayAdapter<String> interestedAdapter = new ArrayAdapter<String>(this, R.layout.view_simple_spinner_item,
                getResources().getStringArray(R.array.interested));
        tripsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tabletTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tabletSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrips.setAdapter(tripsAdapter);
        tabletTypeSpinner.setAdapter(tabletTypeAdapter);
        tabletSizeSpinner.setAdapter(tabletSizeAdapter);
        citySpinner.setAdapter(cityAdapter);
        spinnerInterested.setAdapter(interestedAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignup)
            signup();
        else if (v.getId() == R.id.btnBack)
            onBackPressed();
    }

    private void signup() {
        final EditText edtName = (EditText) findViewById(R.id.edtName);
        final EditText edtLastName = (EditText) findViewById(R.id.edtLastName);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtMobile = (EditText) findViewById(R.id.edtMobile);
        final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
        final EditText edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        final EditText edtreferral = (EditText) findViewById(R.id.edtreferral);
        final EditText edtPayPal = (EditText) findViewById(R.id.edtPayPal);
        final EditText edtCity = (EditText) findViewById(R.id.edtCity);
        final CommentBox commentBox = (CommentBox) findViewById(R.id.commentBox);
        final CabServicesView cabServicesView = (CabServicesView) findViewById(R.id.cabServices);
        String lat = locationModule.getCurrentLatLong() == null ? ""
                : locationModule.getCurrentLatLong()[0] + "";
        String lng = locationModule.getCurrentLatLong() == null ? ""
                : locationModule.getCurrentLatLong()[1] + "";
        GlobalUtils.hideKeyboard(edtName);
        if (Validator.isValid(edtName, Validator.Type.NAME) && Validator.isValid(edtLastName, Validator.Type.LASTNAME)
                && Validator.isValid(edtEmail, Validator.Type.EMAIL)
                && Validator.isValid(edtPassword, Validator.Type.PASSWORD)
                && Validator.isValidConfirmPassword(edtPassword, edtConfirmPassword)
                && Validator.isValid(edtMobile, Validator.Type.MOBILE)
                && Validator.isValid(edtCity, Validator.Type.CITY)
                ) {
            if (cabServicesView.getSelectedServices().size() == 0) {
                showError("Please a select a cab service to proceed.");
                return;
            }
            final Driver driver = new Driver();
            driver.fname = edtName.getText().toString();
            driver.lname = edtLastName.getText().toString();
            driver.email = edtEmail.getText().toString();
            driver.password = edtPassword.getText().toString();
            driver.phone = edtMobile.getText().toString();
            driver.referralCode = edtreferral.getText().toString();
            driver.paypalId = edtPayPal.getText().toString();
            driver.tablet = (String) tabletTypeSpinner.getSelectedItem();
            driver.tabletSize = (String) tabletSizeSpinner.getSelectedItem();
            driver.state = (String) citySpinner.getSelectedItem();
            driver.city = edtCity.getText().toString();
            driver.interest = spinnerInterested.getSelectedItemPosition();
            driver.comments = commentBox.getText();
            driver.services = TextUtils.join(",", cabServicesView.getSelectedServices());
            driver.trips = (String) spinnerTrips.getSelectedItem();
            driver.lat = lat;
            driver.lng = lng;
            client.setParams(driver);
            client.makeRequest();
        }
    }

    private RegistrationClient client = new RegistrationClient(this) {
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
            if (alertDialog == null)
                createDialog();
            alertDialog.setMessage("Congratulations, you have been added to the RIDEPLAY tv driver Q. " +
                    "You will receive a text prompting you to qualify as a driver when a position opens up. " +
                    "After you register, please refer to our FAQ to learn more about the qualification process at: www.rideplay.tv/partners");
            alertDialog.show();
        }
    };

    private void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.btn_go_to_login, this);
        builder.setNegativeButton(R.string.btn_cancel, this);
        alertDialog = builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE)
            onBackPressed();
        else
            alertDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (client != null)
            client.cancel();
    }

    private void createAlert() {
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setPositiveButton("OK", null);
        alert = b.create();
    }

    private void showError(String message) {
        if (alert == null)
            createAlert();
        alert.setMessage(message);
        alert.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.color_button));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            findViewById(R.id.btnSignup).setEnabled(true);
        } else {
            findViewById(R.id.btnSignup).setEnabled(false);
        }
    }
}
