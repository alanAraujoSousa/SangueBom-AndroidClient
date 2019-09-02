package com.bom.sangue.sanguebom.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by alan on 29/11/15.
 */
public class HttpManager {
        private static HttpManager mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;

        private HttpManager(Context context) {
            mCtx = context;
            mRequestQueue = getRequestQueue();
        }

        public static synchronized HttpManager getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new HttpManager(context);
            }
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }
}
