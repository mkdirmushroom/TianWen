package me.jeremy.ccst.ui;

import android.annotation.TargetApi;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import me.jeremy.ccst.R;
import me.jeremy.ccst.model.Category;
import me.jeremy.ccst.ui.fragment.AllCategoryFragment;
import me.jeremy.ccst.ui.fragment.DrawerFragment;
import me.jeremy.ccst.ui.fragment.QuestionnairesFragment;
import me.jeremy.ccst.ui.fragment.SettingFragment;
import me.jeremy.ccst.utils.ToastUtils;

/**
 * Created by qiugang on 2014/9/27.
 */

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private Menu mMenu;

    private Category mCategory;

    private Fragment mContentFragment;

    private ActionBar mActionBar;

    private DrawerArrowDrawable drawerArrow;

    private long exitTime = 0;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (mCategory.equals(Category.latest)) {
                    mMenu.findItem(R.id.action_search).setVisible(true);
                }
                mActionBar.setTitle(mCategory.getDisplayName());
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mMenu.findItem(R.id.action_search).setVisible(false);
                mActionBar.setTitle(R.string.app_name);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            setCategory(Category.latest);
        }
        getFragmentManager().beginTransaction().replace(R.id.left_drawer, new DrawerFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.invalidateOptionsMenu();
        getFragmentManager().beginTransaction().replace(R.id.left_drawer, new DrawerFragment()).commit();
    }

    public void setCategory(Category category) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (mCategory == category) {
            return;
        }
        mCategory = category;
        if (category != Category.Settings) {
            if (category == Category.latest) {
                mContentFragment = QuestionnairesFragment.newInstance();
            } else if (category == Category.Category) {
                mContentFragment = AllCategoryFragment.newInstance();
            }
            fragmentManager.beginTransaction().replace(R.id.container, mContentFragment).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, SettingFragment.newInstance()).commit();
        }
        mActionBar.setTitle(category.getDisplayName());
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showShort("再按一次退出哦");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
