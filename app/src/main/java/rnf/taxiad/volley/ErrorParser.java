package rnf.taxiad.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import rnf.taxiad.R;

/**
 * Created by Rahil on 28/12/15.
 */
public class ErrorParser {

    private String errorMessage;

    public ErrorParser(Context context, VolleyError volleyError) {
        if (volleyError instanceof ServerError)
            errorMessage = context.getString(R.string.server_error);
        else if (volleyError instanceof NoConnectionError)
            errorMessage = context.getString(R.string.no_connection_error);
        else if (volleyError instanceof NetworkError)
            errorMessage = context.getString(R.string.network_error);
        else if (volleyError instanceof ParseError)
            errorMessage = context.getString(R.string.parse_error);
        else if (volleyError instanceof AuthFailureError)
            errorMessage = context.getString(R.string.auth_failure_error);
        else if (volleyError instanceof TimeoutError)
            errorMessage = context.getString(R.string.timeout_error);
        else
            errorMessage = context.getString(R.string.unknown_error);
    }

    public String getMessage() {
        return errorMessage;
    }
}
