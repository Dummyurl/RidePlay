package rnf.taxiad.activities;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import rnf.taxiad.R;
import rnf.taxiad.Utility.MyCountDownTimer;
import rnf.taxiad.controllers.WelcomeStart;
import rnf.taxiad.locations.LocationService;
import rnf.taxiad.models.LocationModule;
import rnf.taxiad.providers.ItemsContract;

/**
 * Created by rnf-new on 20/3/17.
 */

public class WelcomeActivity extends LocationServicesActivity implements View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    private final int LOADER_ID = 0101;
    public MyCountDownTimer myCountDownTimer;
    public LocationModule locationModule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.tvSignup).setOnClickListener(this);
        findViewById(R.id.tvLogin).setOnClickListener(this);
        myCountDownTimer = new MyCountDownTimer(15 * 1000, 1 * 1000, WelcomeActivity.this);
        getContentResolver().registerContentObserver(ItemsContract.Videos.CONTENT_URI, true, observer);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        startService(new Intent(this, LocationService.class).putExtra("fromHome","no"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tvLogin){
            startActivityClearAll(LoginActivity.class);
        }

        if (v.getId()==R.id.tvSignup){
            startActivityClearAll(SignupActivity.class);
        }

        WelcomeStart.getInstance(WelcomeActivity.this).setIsShow(false);
    }

    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, WelcomeActivity.this);
        }
    };
    @Override
    public void onLocationChanged(LocationModule locationModule) {
        super.onLocationChanged(locationModule);
        this.locationModule = locationModule;
        try {

            Log.d("RIDE","inside WelcomeActivity onLocationChanged --> ");
            if (locationModule.getLastAddress() == null || locationModule.getCurrentAddress() == null){
                Log.d("RIDE","loactionModule is --> "+locationModule.getLastAddress()+" ---> "+locationModule.getCurrentAddress());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
