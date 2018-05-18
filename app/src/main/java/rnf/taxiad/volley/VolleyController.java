package rnf.taxiad.volley;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

/**
 * A Singleton class to handle request mechanism using Volley.
 * Created by Rahil
 */
public class VolleyController {

    public static final String TAG = VolleyController.class
            .getSimpleName();
    private static VolleyController instance;
    private Context context;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyController(Context context) {
        this.context = context;
    }

    public static synchronized VolleyController getInstance(Context context) {
        if (instance == null)
            instance = new VolleyController(context);

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public String getCacheResponse(String url) {
        final Cache cache = getRequestQueue().getCache();
        final Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                return data;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void invalidateCache(String url, boolean flag) {
        getRequestQueue().getCache().invalidate(url, flag);
    }

    public void deleteCache(String url) {
        getRequestQueue().getCache().remove(url);
    }

    public void clearCache() {
        getRequestQueue().getCache().clear();
    }
}
