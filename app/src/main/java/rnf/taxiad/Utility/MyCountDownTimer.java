package rnf.taxiad.Utility;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;

import rnf.taxiad.R;
import rnf.taxiad.activities.HomeActivity;

/**
 * Created by rnf-new on 18/11/16.
 */

public class MyCountDownTimer extends CountDownTimer {

    long mCurrentTime;
    long mCountDownInterval;
    Activity mActivity;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, Activity activity) {
        super(millisInFuture, countDownInterval);
        this.mCurrentTime = millisInFuture;
        this.mCountDownInterval = countDownInterval;
        this.mActivity = activity;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long time = millisUntilFinished / 1000l;
    }

    @Override
    public void onFinish() {
        if (mActivity instanceof HomeActivity) {
            DrawerLayout drawerLayout = (DrawerLayout) ((HomeActivity) mActivity).findViewById(R.id.drawerLayout);
            if (drawerLayout != null) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                ((HomeActivity) mActivity).startRefeshAnimation(false);
            }
        } else {
            mActivity.finish();

        }
    }
}