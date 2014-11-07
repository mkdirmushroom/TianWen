package me.jeremy.ccst.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
* Powered by stormzhang
*/
public class RequestManager {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private RequestManager() {
        // no instances
    }

    public static void init(Context context) {

        mRequestQueue = Volley.newRequestQueue(context);

//        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
//                .getMemoryClass();
//        // Use 1/8th of the available memory for this memory cache.
//        int cacheSize = 1024 * 1024 * memClass / 8;
//        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        if (mRequestQueue == null) {
            Log.e("mRequestQueue Error", " is null");
        } else {
            mRequestQueue.add(request);
        }
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}
