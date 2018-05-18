package rnf.taxiad.Utility;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rahil on 15/12/15.
 */
public class ColoredSnackbar {

    private static final int red = 0xfff44336;
    private static final int green = 0xff4caf50;
    private static final int blue = 0xff36b7a9;
    private static final int orange = 0xffffc107;

    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            snackBarView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            snackBarView.setBackgroundColor(colorId);
            TextView tv = null;
            View v = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            if (v instanceof TextView)
                tv = (TextView) v;
            if (tv != null) {
                tv.setMaxLines(3);
            }
        }

        return snackbar;
    }

    public static Snackbar info(Snackbar snackbar) {
        return colorSnackBar(snackbar, blue);
    }

    public static Snackbar warning(Snackbar snackbar) {
        return colorSnackBar(snackbar, orange);
    }

    public static Snackbar alert(Snackbar snackbar) {
        return colorSnackBar(snackbar, red);
    }

    public static Snackbar confirm(Snackbar snackbar) {
        return colorSnackBar(snackbar, green);
    }
}
