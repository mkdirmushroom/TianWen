package me.jeremy.ccst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.jeremy.ccst.R;
import me.jeremy.ccst.model.user.UserInfoResponse;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 14/10/28.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout nameLayout;
    private RelativeLayout idLayout;
    private RelativeLayout qqLayout;
    private RelativeLayout emailLayout;

    private TextView userName;
    private TextView studentId;
    private TextView qq;
    private TextView eMail;

    private UserInfoResponse userInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfrerence_user);
        initView();
        initViewData();

    }

    private void initView() {
        nameLayout = (RelativeLayout) findViewById(R.id.layout_name);
        idLayout = (RelativeLayout) findViewById(R.id.layout_id);
        qqLayout = (RelativeLayout) findViewById(R.id.layout_qq);
        emailLayout = (RelativeLayout) findViewById(R.id.layout_email);

        userName = (TextView) findViewById(R.id.tv_center_userName);
        studentId = (TextView) findViewById(R.id.tv_center_studentId);
        qq = (TextView) findViewById(R.id.tv_center_qq);
        eMail = (TextView) findViewById(R.id.tv_center_email);

        idLayout.setClickable(false);
        qqLayout.setOnClickListener(this);
        emailLayout.setOnClickListener(this);
    }

    private void initViewData() {
        userInfoResponse = UserUtils.getUserInfoResponse();
        userName.setText(userInfoResponse.getUserName());
        studentId.setText(userInfoResponse.getId() + "");
        if (!TextUtils.isEmpty(userInfoResponse.getQq())) {
            qq.setText(userInfoResponse.getQq());
        } else {
            qq.setText("未填写");
        }

        if (!TextUtils.isEmpty(userInfoResponse.getQq())) {
            eMail.setText(userInfoResponse.getEmail());
        } else {
            eMail.setText("未填写");
        }
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent(this, EditProfileActivity.class);
        Bundle args = new Bundle();
        switch (v.getId()) {
            case R.id.layout_email:
                args.putString("type","email");
                args.putString("value", userInfoResponse.getEmail());
                break;
            case R.id.layout_qq:
                args.putString("type", "qq");
                args.putString("value", userInfoResponse.getQq());
                break;
        }
        mIntent.putExtras(args);
        startActivity(mIntent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewData();
    }

}
