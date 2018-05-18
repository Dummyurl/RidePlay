package rnf.taxiad.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import rnf.taxiad.interfaces.RequestEntity;


/**
 * Created by Rahil on 28/12/15.
 */
public class ITJsonRequest extends JsonObjectRequest {

    private RequestEntity requestEntity;

    public ITJsonRequest(RequestEntity requestEntity) {
        super(requestEntity.getMethod(), requestEntity.getUrl(), requestEntity.getJSONObject()
                , requestEntity.getResponseListener(), requestEntity.getErrorListener());
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
        return requestEntity.getParams() != null ? requestEntity.getParams() : super.getParams();
    }

    @Override
    public Priority getPriority() {
        return requestEntity.getPriority() != null ? requestEntity.getPriority() : super.getPriority();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            requestEntity.parse(jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
    @Override
    protected void deliverResponse(JSONObject response) {
        if (requestEntity.getResponseListener() != null)
            requestEntity.getResponseListener().onResponse(response);
    }
}
