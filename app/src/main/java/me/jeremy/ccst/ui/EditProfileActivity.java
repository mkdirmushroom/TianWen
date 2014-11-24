package me.jeremy.ccst.ui;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import me.jeremy.ccst.App;
import me.jeremy.ccst.R;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.data.GsonRequest;
import me.jeremy.ccst.model.user.UserInfoResponse;
import me.jeremy.ccst.model.user.UserModifyRequest;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 14/10/29.
 */
public class EditProfileActivity extends BaseActivity {

    private static final String EMAIL = "email";

    private static final String QQ = "qq";

    private String type = null;

    private EditText mEditText;

    private String editData;

    private Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        mEditText = (EditText) findViewById(R.id.et_profile);
        args = getIntent().getExtras();
        type = args.getString("type");
        mEditText.setText(args.getString("value"));
        initActionbar();

    }

    private void initActionbar() {
        ActionBar mActionBar = getActionBar();
        if (EMAIL.equals(type)) {
            mActionBar.setTitle("修改邮箱");
        } else if (QQ.equals(type)) {
            mActionBar.setTitle("修改QQ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editprofile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_commit_profile) {
            editData = mEditText.getText().toString();
            if (!TextUtils.isEmpty(editData)) {
                if (!editData.equals(args.getString("value"))) {
                    commitData();
                } else {
                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void commitData() {
        UserInfoResponse userInfoResponse = UserUtils.getUserInfoResponse();
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        JSONObject jsonObject = null;
        if (EMAIL.equals(type)) {
            userInfoResponse.setEmail(editData);
        } else if (QQ.equals(type)) {
            userInfoResponse.setQq(editData);
        }
        userModifyRequest.setQq(userInfoResponse.getQq());
        userModifyRequest.setEmail(userInfoResponse.getEmail());
        userModifyRequest.setId(userInfoResponse.getId());
        userModifyRequest.setPhone("");

        String params = new Gson().toJson(userModifyRequest);
        Log.d("update userinfo data", params);
        try {
            jsonObject = new JSONObject(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        App.getContext().getSharedPreferences("user", 0).edit().remove("userInfo");
        App.getContext().getSharedPreferences("user", 0).edit().putString("userInfo",
                new Gson().toJson(userInfoResponse)).commit();
        Log.d("saved data", new Gson().toJson(userInfoResponse));
        if (ToolUtils.isConnectInternet()) {
            executeRequest(new GsonRequest<UserInfoResponse>(Request.Method.PUT, Api.Host_ALIYUN + Api.UPDATEUSER,
                    jsonObject, UserInfoResponse.class, null, responseListener(), errorListener()));
        } else {
            ToastUtils.showShort("网络未连接，不能捡肥皂");
        }
    }

    private Response.Listener<UserInfoResponse> responseListener() {
        return new Response.Listener<UserInfoResponse>() {
            @Override
            public void onResponse(final UserInfoResponse response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Log.d("update userinfomation", new Gson().toJson(response));
                        if (response != null) {
                            ToastUtils.showShort("同步资料成功");
                            finish();
                        } else {
                            ToastUtils.showShort("同步资料失败");
                        }
                    }
                });
            }
        };
    }
}
