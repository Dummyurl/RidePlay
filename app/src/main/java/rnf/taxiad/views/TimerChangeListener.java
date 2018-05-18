package rnf.taxiad.views;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by rnf-new on 30/11/16.
 */

public class TimerChangeListener implements TextWatcher {

    private TimerLayout mTimerLayout;

    public TimerChangeListener(TimerLayout mTimerLayout) {
        this.mTimerLayout = mTimerLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTimerLayout != null)
            mTimerLayout.setMyCountDownTimer();
    }
}
