package me.jeremy.ccst.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import me.jeremy.ccst.R;

/**
 * Created by qiugang on 2014/9/24.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        FadingActionBarHelper helper = new FadingActionBarHelper()
//                .actionBarBackground(R.drawable.ic_launcher)
//                .headerLayout(R.layout.header)
//                .contentLayout(R.layout.activity_about);
//        setContentView(helper.createView(this));
//        helper.initActionBar(this);
        getActionBar().setDisplayShowHomeEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
