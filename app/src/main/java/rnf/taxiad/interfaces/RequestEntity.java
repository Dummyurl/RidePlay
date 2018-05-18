package rnf.taxiad.interfaces;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Rahil on 10/12/15.
 */
public interface RequestEntity {

    /**
     * Volley Request Method {@link com.android.volley.Request.Method}
     *
     * @return {@link com.android.volley.Request.Method}
     */
    public int getMethod();

    /**
     * Server request url
     *
     * @return {@link String}
     */
    public String getUrl();

    /**
     * Volley Error Listener {@link com.android.volley.Response.ErrorListener}
     *
     * @return {@link com.android.volley.Response.ErrorListener}
     */
    public Response.ErrorListener getErrorListener();

    /**
     * Volley Response Listener {@link com.android.volley.Response.Listener}
     *
     * @return {@link com.android.volley.Response.Listener}
     */
    public Response.Listener getResponseListener();

    /**
     * @return
     */
    public String getContentType();

    /**
     * Volley Reuest Header Parameters
     *
     * @return {@link Map}
     */
    public Map<String, String> getHeaders();

    /**
     * Volley Request Priority {@link com.android.volley.Request.Priority}
     *
     * @return {@link com.android.volley.Request.Priority}
     */
    public Request.Priority getPriority();

    /**
     * Volley Post Request Parameters
     *
     * @return {@link Map}
     */
    public Map<String, String> getParams();

    public JSONObject getJSONObject();

    /**
     * Method to parse JSON Response
     *
     * @param {@link String}
     */
    public void parse(String json);


}
