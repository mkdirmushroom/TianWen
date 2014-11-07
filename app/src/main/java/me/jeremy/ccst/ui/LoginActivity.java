package me.jeremy.ccst.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import me.jeremy.ccst.R;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.data.GsonRequest;
import me.jeremy.ccst.model.user.UserInfoResponse;
import me.jeremy.ccst.model.user.UserLoginRequest;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;


/**
 * Created by qiugang on 14-9-21.
 */

public class LoginActivity extends BaseActivity {

    private EditText etUserName;

    private EditText etPassWord;

    private TextView notice;

    private String userName;

    private String passWord;

    private ProgressDialog progressDialog;

    private UserLoginRequest userLoginRequest = new UserLoginRequest();

    private Gson gson = new Gson();

    private  JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        notice = (TextView) findViewById(R.id.login_tv_notice);
        etUserName = (EditText) findViewById(R.id.login_et_username);
        etPassWord = (EditText) findViewById(R.id.login_et_password);

        YoYo.with(Techniques.BounceInUp).duration(1000).playOn(findViewById(R.id.login_logo));

        if (UserUtils.logined(LoginActivity.this)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            findViewById(R.id.login_bu_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName = etUserName.getText().toString().trim();
                    passWord = etPassWord.getText().toString().trim();
                    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.login_edit_area));
                        notice.setText("用户名或密码不能为空哦");
                        YoYo.with(Techniques.BounceInDown).playOn(notice);
                    } else {
                        userLoginRequest.setUserName(userName);
                        userLoginRequest.setPassWord(passWord);
                        String params = gson.toJson(userLoginRequest);
                        Log.d("data", "request -> " + params);
                     jsonObject = null;
                        try {
                            jsonObject = new JSONObject(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (ToolUtils.isConnectInternet()) {
                            progressDialog = ProgressDialog.show(LoginActivity.this, null, "Login...", true, true);
                            executeRequest(new GsonRequest<UserInfoResponse>(Api.Host_ALIYUN_SLAVE + "login",
                                    jsonObject, UserInfoResponse.class, responseListener(), errorListener()));
                        } else {
                            ToastUtils.showShort("网络未连接，不能捡肥皂");
                        }
                    }
                }
            });
//            findViewById(R.id.login_tv_loginError).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ToastUtils.showShort("login  error");
//
//                }
//            });
            findViewById(R.id.login_tv_signup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                    finish();
                }
            });
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
                        progressDialog.dismiss();
                        Log.d("data", response.getUserName() + "");
                        if (response.getId() != null) {
                            UserUtils.saveUserInfo(response);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            ToastUtils.showShort("Hello," + response.getUserName());
                            finish();
                        } else {
                            ToastUtils.showShort("登录失败哦,用户名或密码不正确");
                        }
                    }
                });
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                ToastUtils.showShort("wait for a moment");
                if (error.networkResponse.statusCode == 500) {
                    Log.d("切换到备用服务器", "");
                    executeRequest(new GsonRequest<UserInfoResponse>(Api.Host_ALIYUN + "login",
                            jsonObject, UserInfoResponse.class, responseListener(), errorListener()));
                }
               // ToastUtils.showShort("网络错误，请检查你的网络连接");
            }
        };
    }
}





