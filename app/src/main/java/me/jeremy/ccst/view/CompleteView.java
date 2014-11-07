package me.jeremy.ccst.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import me.jeremy.ccst.R;

/**
 * Created by qiugang on 14-10-19.
 */
public class CompleteView {
    protected View mCompleteView;

    public CompleteView(Context context) {
        mCompleteView = LayoutInflater.from(context).inflate(R.layout.complete_view, null);
        mCompleteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });
    }

    public View getmCompleteView() {
       return mCompleteView;
    }
}
