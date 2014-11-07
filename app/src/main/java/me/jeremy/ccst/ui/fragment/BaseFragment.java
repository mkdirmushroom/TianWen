package me.jeremy.ccst.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import me.jeremy.ccst.data.RequestManager;

/**
 * Created by qiugang on 2014/9/21.
 */
public class BaseFragment extends Fragment {
    protected Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }
    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request,this);
    }
    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
    }


}
