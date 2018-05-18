package rnf.taxiad.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.BuildConfig;
import io.fabric.sdk.android.Fabric;
import rnf.taxiad.controllers.WelcomeStart;
import rnf.taxiad.models.DriverSession;

/**
 * Created by Rahil on 15/12/15.
 */
public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME = 3000;
    private static final int SPLASH_FINISH = 0;
    private SplashRunnable runnable = new SplashRunnable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        handler.postDelayed(runnable, SPLASH_TIME);
        Fabric.with(this, new Crashlytics());
        
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SPLASH_FINISH) {
                checkWitePermission();
                //startActivity(new Intent(SplashActivity.this, RidePlayGifActivity.class));
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeMessages(SPLASH_FINISH);
        handler.removeCallbacks(runnable);
    }


    private class SplashRunnable implements Runnable {

        @Override
        public void run() {

            handler.sendEmptyMessage(SPLASH_FINISH);
        }
    }

    private boolean checkWitePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x12);
            return false;
        } else {
            if (DriverSession.get().isHasSession())
                startActivityClearAll(HomeActivity.class);

            else {
                if (WelcomeStart.getInstance(this).isShow()) {
                    startActivityClearAll(WelcomeActivity.class);
                } else {
                    startActivityClearAll(LoginActivity.class);
                }

            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0x12) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (DriverSession.get().isHasSession())
                    startActivityClearAll(HomeActivity.class);
                else {
                    if (WelcomeStart.getInstance(this).isShow()) {
                        startActivityClearAll(WelcomeActivity.class);
                    } else {
                        startActivityClearAll(LoginActivity.class);
                    }
                }
            }
        }
    }
}
