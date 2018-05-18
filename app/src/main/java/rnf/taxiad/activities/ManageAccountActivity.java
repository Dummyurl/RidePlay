package rnf.taxiad.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ViewAnimator;

import java.util.Arrays;
import java.util.List;

import rnf.taxiad.R;
import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.Validator;
import rnf.taxiad.helpers.GetProfileDataClient;
import rnf.taxiad.helpers.UpdateProfileClient;
import rnf.taxiad.models.Driver;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.views.AuthDialog;
import rnf.taxiad.views.CabServicesView;
import rnf.taxiad.views.CommentBox;
import rnf.taxiad.views.TimerChangeListener;
import rnf.taxiad.views.TimerLayout;

/**
 * Created by Rahil on 12/1/16.
 */
public class ManageAccountActivity extends BaseActivity implements View.OnClickListener {

    private AppCompatSpinner spinnerTrips, tabletTypeSpinner, tabletSizeSpinner, citySpinner, spinnerInterested;
    private ViewAnimator animator;
    private AlertDialog alertDialog;
    private TimerLayout timerLayout;

    AuthDialog authDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        setupViews();
        showAuthDialog();
        getProfileDataClient.makeRequest();
    }

    private void setupViews() {
        setupSpinners();
        animator = (ViewAnimator) findViewById(R.id.animator);
        timerLayout = (TimerLayout) findViewById(R.id.timerLayout);
        findViewById(R.id.btnSignup).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);

        setTimerChangeListener();
    }


    private void showAuthDialog() {
        authDialog = new AuthDialog(this, timerLayout);
        authDialog.show();
        authDialog.setCancelable(false);
    }

    private void renderItems() {
        View v = findViewById(android.R.id.content);
        if (v == null)
            return;
        final Driver driver = DriverSession.get().getDriver();
        final EditText edtName = (EditText) findViewById(R.id.edtName);
        final EditText edtLastName = (EditText) findViewById(R.id.edtLastName);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtMobile = (EditText) findViewById(R.id.edtMobile);
        final EditText edtreferral = (EditText) findViewById(R.id.edtreferral);
        final EditText edtPayPal = (EditText) findViewById(R.id.edtPayPal);
        final EditText edtCity = (EditText) findViewById(R.id.edtCity);
        final CommentBox commentBox = (CommentBox) findViewById(R.id.commentBox);

        final EditText edtNewPass = (EditText) findViewById(R.id.edt_new_pass);
        final EditText edtConPass = (EditText) findViewById(R.id.edt_con_pass);

        final CabServicesView cabServicesView = (CabServicesView) findViewById(R.id.cabServices);
        final List<String> trips = Arrays.asList(getResources().getStringArray(R.array.trips_per_week));
        final List<String> tabletType = Arrays.asList(getResources().getStringArray(R.array.type_of_tablet));
        final List<String> tabletSize = Arrays.asList(getResources().getStringArray(R.array.size_of_tablet));
        final List<String> city = Arrays.asList(getResources().getStringArray(R.array.cities));
        final List<String> interested = Arrays.asList(getResources().getStringArray(R.array.interested));
        edtEmail.setText(driver.email == null ? "" : driver.email);
        edtName.setText(driver.fname == null ? "" : driver.fname);
        edtLastName.setText(driver.lname == null ? "" : driver.lname);
        edtMobile.setText(driver.phone == null ? "" : driver.phone);
        edtreferral.setText(driver.referralCode == null ? "" : driver.referralCode);
        edtPayPal.setText(driver.paypalId == null ? "" : driver.paypalId);
        edtCity.setText(driver.city == null ? "" : driver.city);
        commentBox.getEditText().setText(driver.comments == null ? "" : driver.comments);

        edtNewPass.setText(driver.new_pass == null ? "" : driver.new_pass);

        spinnerTrips.setSelection(trips.indexOf(driver.trips == null ? "" : driver.trips));
        tabletTypeSpinner.setSelection(tabletType.indexOf(driver.tablet == null ? "" : driver.tablet));
        tabletSizeSpinner.setSelection(tabletSize.indexOf(driver.tabletSize == null ? "" : driver.tabletSize));
        citySpinner.setSelection(city.indexOf(driver.state == null ? "" : driver.state));
        spinnerInterested.setSelection(driver.interest == 0 ? 1 : 0);
        cabServicesView.setSelected(driver.services == null ? "" : driver.services);
    }


    private void setTimerChangeListener() {
        final EditText edtName = (EditText) findViewById(R.id.edtName);
        final EditText edtLastName = (EditText) findViewById(R.id.edtLastName);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtMobile = (EditText) findViewById(R.id.edtMobile);
        final EditText edtreferral = (EditText) findViewById(R.id.edtreferral);
        final EditText edtPayPal = (EditText) findViewById(R.id.edtPayPal);
        final EditText edtCity = (EditText) findViewById(R.id.edtCity);
        final CommentBox commentBox = (CommentBox) findViewById(R.id.commentBox);

        final EditText edtNewPass = (EditText) findViewById(R.id.edt_new_pass);
        final EditText edtConPass = (EditText) findViewById(R.id.edt_con_pass);

        edtName.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtLastName.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtEmail.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtMobile.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtreferral.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtPayPal.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtCity.addTextChangedListener(new TimerChangeListener(timerLayout));
        commentBox.getEditText().addTextChangedListener(new TimerChangeListener(timerLayout));

        edtNewPass.addTextChangedListener(new TimerChangeListener(timerLayout));
        edtConPass.addTextChangedListener(new TimerChangeListener(timerLayout));
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

    private void update() {
        final EditText edtName = (EditText) findViewById(R.id.edtName);
        final EditText edtLastName = (EditText) findViewById(R.id.edtLastName);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtMobile = (EditText) findViewById(R.id.edtMobile);
        final EditText edtreferral = (EditText) findViewById(R.id.edtreferral);
        final EditText edtPayPal = (EditText) findViewById(R.id.edtPayPal);
        final EditText edtCity = (EditText) findViewById(R.id.edtCity);
        final CommentBox commentBox = (CommentBox) findViewById(R.id.commentBox);

        final EditText edtNewPass = (EditText) findViewById(R.id.edt_new_pass);
        final EditText edtConPass = (EditText) findViewById(R.id.edt_con_pass);
        final CabServicesView cabServicesView = (CabServicesView) findViewById(R.id.cabServices);
        if (Validator.isValid(edtName, Validator.Type.NAME)
                && Validator.isValid(edtLastName, Validator.Type.LASTNAME)
                && Validator.isValid(edtMobile, Validator.Type.MOBILE)
                && Validator.isValid(edtCity, Validator.Type.CITY)
                && Validator.isValidPass(edtNewPass,edtConPass, Validator.Type.CONPASS)) {
            if (cabServicesView.getSelectedServices().size() == 0) {
                showError("Please a select a cab service to proceed.");
                return;
            }
            final Driver driver = new Driver();
            driver.fname = edtName.getText().toString();
            driver.lname = edtLastName.getText().toString();
            driver.phone = edtMobile.getText().toString();
            driver.referralCode = edtreferral.getText().toString();
            driver.paypalId = edtPayPal.getText().toString();
            driver.tablet = (String) tabletTypeSpinner.getSelectedItem();
            driver.tabletSize = (String) tabletSizeSpinner.getSelectedItem();
            driver.state = (String) citySpinner.getSelectedItem();
            driver.city = edtCity.getText().toString();
            driver.interest = spinnerInterested.getSelectedItemPosition();
            driver.trips = (String) spinnerTrips.getSelectedItem();
            driver.comments = commentBox.getText();

            driver.new_pass = edtNewPass.getText().toString();
//            driver.con_pass = edtConPass.getText().toString();

            driver.services = TextUtils.join(",", cabServicesView.getSelectedServices());
            client.setParams(driver);
            client.makeRequest();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignup)
            update();
        else if (v.getId() == R.id.btnBack)
            onBackPressed();
    }

    private UpdateProfileClient client = new UpdateProfileClient(this) {
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
        public void onSuccess(Driver driver) {
            DriverSession.get().update(driver);
            if (animator == null)
                return;
            if(!driver.new_pass.equalsIgnoreCase("")) {
                String md5String = GlobalUtils.convertStringIntoMd5(driver.new_pass);
                AppPreferenceManager manager = AppPreferenceManager.getInstance(ManageAccountActivity.this);
                manager.saveString("md5Pwd", md5String);
            }
            animator.setDisplayedChild(0);
        }
    };

    private GetProfileDataClient getProfileDataClient = new GetProfileDataClient(this) {
        @Override
        public void onStarted() {

        }

        @Override
        public void onError() {
            renderItems();
        }

        @Override
        public void onSuccess() {
            renderItems();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (client != null) {
            client.cancel();
        }
        if (authDialog != null && authDialog.isShowing())
            authDialog.dismiss();
    }

    private void createDialog() {
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setPositiveButton("OK", null);
        alertDialog = b.create();
    }

    private void showError(String message) {
        if (alertDialog == null)
            createDialog();
        alertDialog.setMessage(message);
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.color_button));
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (authDialog != null && authDialog.isShowing())
            authDialog.dismiss();
    }
}
