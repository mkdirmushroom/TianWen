package me.jeremy.ccst.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import me.jeremy.ccst.data.RequestManager;
import me.jeremy.ccst.utils.ToastUtils;

/**
 * Created by qiugang on 2014/9/21.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showShort("网络错误，请检查你的网络连接");
            }
        };
    }
}
