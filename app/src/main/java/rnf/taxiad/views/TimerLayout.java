package rnf.taxiad.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import rnf.taxiad.Utility.MyCountDownTimer;


/**
 * Created by rnf-new on 18/11/16.
 */

public class TimerLayout extends RelativeLayout {

    private MyCountDownTimer myCountDownTimer;

    public TimerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerLayout(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMyCountDownTimer();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setMyCountDownTimer();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setMyCountDownTimer();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setMyCountDownTimer() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }
        myCountDownTimer = new MyCountDownTimer(20 * 1000, 1 * 1000, ((Activity) getContext()));
        myCountDownTimer.start();
    }

    public void setVideoDemoMyCountDownTimer() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }
        myCountDownTimer = new MyCountDownTimer(5*60*1000, 1 * 1000, ((Activity) getContext()));
        myCountDownTimer.start();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
    }

    public void stopTimer(){
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }
    }




}
