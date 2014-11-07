package me.jeremy.ccst.data;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiugang on 2014/9/27.
 */
public class MyStringRequest extends StringRequest {

    private JSONObject mJsonObject;

    public MyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }
    public MyStringRequest(int method, String url,JSONObject jsonObject, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.mJsonObject = jsonObject;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }
    @Override
    protected void deliverResponse(String response) {
        super.deliverResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String json =
                    new String(response.data, "utf-8");
            return Response.success(json,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
