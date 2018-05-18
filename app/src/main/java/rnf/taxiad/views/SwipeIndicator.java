package rnf.taxiad.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import rnf.taxiad.R;

/**
 * Created by Rahil on 11/1/16.
 */
public class SwipeIndicator extends RelativeLayout {

    //    private AnimationDrawable leftAnimation, rightAnimation;
    private ImageView indicatorLeft, indicatorRight;

    public SwipeIndicator(Context context) {
        super(context);
    }

    public SwipeIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        indicatorLeft = (ImageView) findViewById(R.id.indicatorLeft);
        indicatorRight = (ImageView) findViewById(R.id.indicatorRight);
//        indicatorLeft.setBackgroundResource(R.drawable.left_indicator);
//        indicatorRight.setBackgroundResource(R.drawable.right_indicator);
//        leftAnimation = (AnimationDrawable) indicatorLeft.getBackground();
//        rightAnimation = (AnimationDrawable) indicatorRight.getBackground();
    }

    public void startAnimation() {
        startLeftAnimation();
        startRightAnimation();
    }

    public void stopAnimation() {
        stopLeftAnimation();
        stopRightAnimation();
    }

    public void startLeftAnimation() {
        indicatorLeft.setVisibility(View.VISIBLE);
//        leftAnimation.start();
    }

    public void startRightAnimation() {
        indicatorRight.setVisibility(View.VISIBLE);
//        rightAnimation.start();
    }

    public void stopLeftAnimation() {
        indicatorLeft.setVisibility(View.GONE);
//        leftAnimation.stop();
    }

    public void stopRightAnimation() {
        indicatorRight.setVisibility(View.GONE);
//        rightAnimation.stop();
    }
}
