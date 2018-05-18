package rnf.taxiad.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.interfaces.RequestEntity;


/**
 * Created by Rahil on 28/12/15.
 */
public class ITRequest extends Request<String> {

    private RequestEntity requestEntity;

    public ITRequest(RequestEntity requestEntity) {
        super(requestEntity.getMethod(), requestEntity.getUrl(), requestEntity.getErrorListener());
        this.requestEntity = requestEntity;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return requestEntity.getHeaders() != null ? requestEntity.getHeaders() : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return requestEntity.getContentType() != null ? requestEntity.getContentType() : super.getBodyContentType();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> localParams = requestEntity.getParams();
        if (localParams == null)
            return super.getParams();
        String encryptedMap = GlobalUtils.encrypt(localParams);
        final Map<String, String> params = new HashMap<>();
        params.put("Value", encryptedMap);
        return params;
    }

    @Override
    public Priority getPriority() {
        return requestEntity.getPriority() != null ? requestEntity.getPriority() : super.getPriority();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            jsonString = GlobalUtils.decrypt(jsonString);
            requestEntity.parse(jsonString);
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        if (requestEntity.getResponseListener() != null)
            requestEntity.getResponseListener().onResponse(response);
    }
}
