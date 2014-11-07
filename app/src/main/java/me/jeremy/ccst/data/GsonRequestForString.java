package me.jeremy.ccst.data;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiugang on 14-9-13.
 */
public class GsonRequestForString<T> extends Request<T> {

    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Response.Listener<T> mListener;
    private final Map<String, String> mHeaders;
    private final String mString;

    public GsonRequestForString(String url, String string, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, string, clazz, null, listener, errorListener);
    }


    public GsonRequestForString(int method, String url, String string, Class<T> clazz, Map<String, String> headers,
                                Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mString = string;
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mString.getBytes();
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
                    HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }

    }
}
