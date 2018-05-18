package rnf.taxiad.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

/**
 * Created by Rahil on 13/1/16.
 */
public class AppSpinner extends AppCompatSpinner {
    public AppSpinner(Context context) {
        super(context);
    }

    public AppSpinner(Context context, int mode) {
        super(context, mode);
    }

    public AppSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public AppSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
