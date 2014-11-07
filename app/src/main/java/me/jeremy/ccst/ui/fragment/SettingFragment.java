package me.jeremy.ccst.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;
import me.jeremy.ccst.R;
import me.jeremy.ccst.ui.AboutActivity;
import me.jeremy.ccst.ui.LoginActivity;
import me.jeremy.ccst.ui.UserInfoActivity;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 2014/9/29.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    public static final String PREF_LOGIN = "pref_login";

    public static final String PREF_DELETE_CACHE = "pref_deleteCache";

    public static final String PREF_ABOUT = "pref_about";

    public static final String PREF_SIGNOUT = "pref_signOut";

    private boolean logined = false;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (logined = UserUtils.getUserSharedPreference().contains("username")) {
            String username = UserUtils.getUserName();
            if (username != null) {
                findPreference(PREF_LOGIN).setTitle(username);
                findPreference(PREF_SIGNOUT).setEnabled(true);
                logined = true;
            }
        }
        findPreference(PREF_LOGIN).setOnPreferenceClickListener(this);
        findPreference(PREF_DELETE_CACHE).setOnPreferenceClickListener(this);
        findPreference(PREF_ABOUT).setOnPreferenceClickListener(this);
        findPreference(PREF_SIGNOUT).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(PREF_LOGIN)) {
            if (logined) {
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
            return true;
        } else if (preference.getKey().equals(PREF_DELETE_CACHE)) {
            ToolUtils.deleteFilesByDirectory(getActivity().getCacheDir());
            ToastUtils.showShort("已经清理啦，么么哒");
            return true;
        } else if (preference.getKey().equals(PREF_ABOUT)) {
            startActivity(new Intent(getActivity(), AboutActivity.class));
            return true;
        } else if (preference.getKey().equals(PREF_SIGNOUT)) {
            if (logined) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
                mMaterialDialog.setTitle("注销");
                mMaterialDialog.setMessage("你真的忍心就这么离开？");
                mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        mMaterialDialog.dismiss();
                    }
                });

                mMaterialDialog.setNegativeButton("CANCLE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.show();
            }
            return true;
        } else {
            return false;
        }
    }

    private void logout() {
        UserUtils.getUserSharedPreference().edit()
                .remove("id")
                .remove("username")
                .remove("userInfo")
                .remove("haveOpen")
                .commit();
        logined = false;
        findPreference(PREF_LOGIN).setTitle(getString(R.string.title_login));
        findPreference(PREF_SIGNOUT).setEnabled(false);
        logined = false;
    }
}
