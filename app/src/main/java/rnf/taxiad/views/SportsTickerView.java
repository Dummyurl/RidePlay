package rnf.taxiad.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Rahil on 11/1/16.
 */
public class SportsTickerView extends LinearLayout {
    public SportsTickerView(Context context) {
        super(context);
    }

    public SportsTickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SportsTickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SportsTickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
