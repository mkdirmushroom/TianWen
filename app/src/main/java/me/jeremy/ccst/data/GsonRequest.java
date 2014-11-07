package me.jeremy.ccst.data;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *  powered by stormzhang
 */
public class GsonRequest<T> extends Request<T>{

    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Response.Listener<T> mListener;
    private final Map<String, String> mHeaders;
    private final JSONObject mJsonObject;

    public GsonRequest(String url, JSONObject jsonObject, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener){
        this(Request.Method.POST, url, jsonObject, clazz, null, listener, errorListener);
    }


    public GsonRequest(int method, String url, JSONObject jsonObject, Class<T> clazz, Map<String, String> headers,
                                Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mJsonObject = jsonObject;
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
    }



    @Override
    public byte[] getBody() throws AuthFailureError {
        return mJsonObject.toString().getBytes();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);

    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data,
                    "utf-8");
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }

    }
}
