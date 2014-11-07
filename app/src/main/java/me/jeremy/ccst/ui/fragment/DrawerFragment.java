package me.jeremy.ccst.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import me.jeremy.ccst.R;
import me.jeremy.ccst.adapter.DrawerAdapter;
import me.jeremy.ccst.model.Category;
import me.jeremy.ccst.ui.LoginActivity;
import me.jeremy.ccst.ui.MainActivity;
import me.jeremy.ccst.ui.UserInfoActivity;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 2014/9/21.
 */
public class DrawerFragment extends Fragment {

    private ListView mListView;

    private TextView mTextView;

    private DrawerAdapter mAdapter;

    private MainActivity mainActivity;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_drawer, null);
        mainActivity = (MainActivity)getActivity();
        mListView = (ListView) rootView.findViewById(R.id.drawer_listView);
        mTextView = (TextView) rootView.findViewById(R.id.drawer_textView);
        mAdapter = new DrawerAdapter(mListView);
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.setItemChecked(position, true);
                mainActivity.setCategory(Category.values()[position]);
            }
        });
        if (UserUtils.logined(mainActivity)) {
            mTextView.setText(UserUtils.getUserName().toUpperCase());
        } else {
            mTextView.setText(R.string.title_login);
        }
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                if (UserUtils.logined(getActivity())) {
                    mIntent.setClass(mainActivity, UserInfoActivity.class);
                } else {
                    mIntent.setClass(mainActivity, LoginActivity.class);
                }
                startActivity(mIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
