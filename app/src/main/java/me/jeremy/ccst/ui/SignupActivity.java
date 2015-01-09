package me.jeremy.ccst.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import me.jeremy.ccst.R;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.data.GsonRequest;
import me.jeremy.ccst.data.GsonRequestForString;
import me.jeremy.ccst.model.user.UserInfoResponse;
import me.jeremy.ccst.model.user.UserRegisterRequest;
import me.jeremy.ccst.utils.StringUtils;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 2014/9/21.
 */
public class SignupActivity extends BaseActivity {

    private EditText etName;

    private EditText etStudentId;

    private EditText etPassWord;

    private String name = "";

    private String studentId;

    private String passWord;

    private UserRegisterRequest userRegisterRequest = new UserRegisterRequest();

    private Gson gson = new Gson();

    private TextView noticeName;

    private TextView noticeId;

    private TextView noticePassWd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (EditText) findViewById(R.id.signup_et_username);
        etStudentId = (EditText) findViewById(R.id.signup_et_studentid);
        etPassWord = (EditText) findViewById(R.id.signup_et_password);

        noticeName = (TextView) findViewById(R.id.signup_tv_notice_username);
        noticeId = (TextView) findViewById(R.id.signup_tv_notice_studentid);
        noticePassWd = (TextView) findViewById(R.id.signup_tv_notice_passWord);


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!etName.hasFocus()) {
                    name = etName.getText().toString();
                    name = name.replaceAll("\\s*","");
                    Log.d("name", name);
                    if (null != name) {
                        if (StringUtils.checkUserName(name)) {
                            executeRequest(new GsonRequestForString<Boolean>(Api.Host_ALIYUN + "checkuser",
                                    name, Boolean.class, nameResponseListener(), errorListener()));
                        } else {
                            noticeName.setText("用户名6-11位哦");
                        }
                    }
                }
            }
        });

        etStudentId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!etStudentId.hasFocus()) {
                    studentId = etStudentId.getText().toString().trim();
                    Log.d("studentID", studentId);
                    if (null != studentId) {
                        executeRequest(new GsonRequestForString<Boolean>(Api.Host_ALIYUN + "checkcode",
                                studentId, Boolean.class, idResponseListener(), errorListener()));
                    }
                }
            }
        });


        findViewById(R.id.signup_bu_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                studentId = etStudentId.getText().toString().trim();
                passWord = etPassWord.getText().toString().trim();

                if (null == name || null == studentId || null == passWord) {
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.signup_edit_area));
                    ToastUtils.showShort("信息还没有填完哦");
                } else if (passWord.length() < 6 || passWord.length() > 16) {
                    noticePassWd.setText("密码6-16位哦");
                } else {
                    userRegisterRequest.setUserName(name);
                    userRegisterRequest.setPassWord(passWord);
                    userRegisterRequest.setStudentCode(studentId);

                    String params = gson.toJson(userRegisterRequest);
                    Log.d("data", "request -> " + params);
                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    executeRequest(new GsonRequest<UserInfoResponse>(Api.Host_ALIYUN + "register",
                            jsonObject, UserInfoResponse.class, responseListener(), errorListener()));
                }
            }

        });

    }

    private Response.Listener<Boolean> nameResponseListener() {
        return new Response.Listener<Boolean>() {
            @Override
            public void onResponse(final Boolean response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        Log.d("name test ->", response.toString());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (false == response) {
                            noticeName.setText("用户名不可用哦");
                        } else {
                            noticeName.setText("用户名可用");
                        }
                    }
                });
            }
        };
    }

    private Response.Listener<Boolean> idResponseListener() {
        return new Response.Listener<Boolean>() {
            @Override
            public void onResponse(final Boolean response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        Log.d("id test ->", response.toString());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (false == response) {
                            noticeId.setText("学号不可用哦");
                        } else {
                            noticeId.setText("学号可用");
                        }
                    }
                });
            }
        };
    }

    private Response.Listener<UserInfoResponse> responseListener() {
        return new Response.Listener<UserInfoResponse>() {
            @Override
            public void onResponse(final UserInfoResponse response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        Log.d("user xxxxx", response.toString());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (null == response.getUserName()) {
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(findViewById(R.id.signup_edit_area));
                        } else {
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            UserUtils.saveUserInfo(response);
                            ToastUtils.showShort("Hello ," + UserUtils.getUserName());
                            finish();
                        }
                    }
                });
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
