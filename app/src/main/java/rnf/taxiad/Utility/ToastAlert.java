package rnf.taxiad.Utility;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Rahil on 13/1/16.
 */
public final class ToastAlert {

    public static void alertShort(View view, CharSequence charSequence) {
        ColoredSnackbar.alert(Snackbar.make(view, charSequence, Snackbar.LENGTH_SHORT)).show();
    }

    public static void alertShort(View view, int resId) {
        ColoredSnackbar.alert(Snackbar.make(view, resId, Snackbar.LENGTH_SHORT)).show();
    }

    public static void alertLong(View view, CharSequence charSequence) {
        ColoredSnackbar.alert(Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG)).show();
    }

    public static void alertLong(View view, int resId) {
        ColoredSnackbar.alert(Snackbar.make(view, resId, Snackbar.LENGTH_LONG)).show();
    }


    public static void infoShort(View view, CharSequence charSequence) {
        ColoredSnackbar.info(Snackbar.make(view, charSequence, Snackbar.LENGTH_SHORT)).show();
    }

    public static void infoShort(View view, int resId) {
        ColoredSnackbar.info(Snackbar.make(view, resId, Snackbar.LENGTH_SHORT)).show();
    }

    public static void infoLong(View view, CharSequence charSequence) {
        ColoredSnackbar.info(Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG)).show();
    }

    public static void infoLong(View view, int resId) {
        ColoredSnackbar.info(Snackbar.make(view, resId, Snackbar.LENGTH_LONG)).show();
    }

    public static void warningShort(View view, CharSequence charSequence) {
        ColoredSnackbar.warning(Snackbar.make(view, charSequence, Snackbar.LENGTH_SHORT)).show();
    }

    public static void warningShort(View view, int resId) {
        ColoredSnackbar.warning(Snackbar.make(view, resId, Snackbar.LENGTH_SHORT)).show();
    }

    public static void warningLong(View view, CharSequence charSequence) {
        ColoredSnackbar.warning(Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG)).show();
    }

    public static void warningLong(View view, int resId) {
        ColoredSnackbar.warning(Snackbar.make(view, resId, Snackbar.LENGTH_LONG)).show();
    }

}
