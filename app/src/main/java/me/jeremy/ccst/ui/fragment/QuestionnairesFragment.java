package me.jeremy.ccst.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.jeremy.ccst.App;
import me.jeremy.ccst.R;
import me.jeremy.ccst.adapter.ItemAnimationAdapter;
import me.jeremy.ccst.adapter.QuestionnairesAdapter;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.data.MyStringRequest;
import me.jeremy.ccst.model.question.QuestionnaireResponse;
import me.jeremy.ccst.ui.QuestionPager;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 2014/9/22.
 */
public class QuestionnairesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView listView;

    private QuestionnairesAdapter mAdapter;

    private Gson gson = new Gson();

    private Type listType = new TypeToken<List<QuestionnaireResponse>>(){}.getType();

    private List<QuestionnaireResponse> questionnaires = new ArrayList<QuestionnaireResponse>();

    public QuestionnairesFragment() {};

    public static QuestionnairesFragment newInstance() {
        QuestionnairesFragment fragment = new QuestionnairesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_questionnaires, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.questionnaires_swipeRefresh);
        listView = (ListView) rootView.findViewById(R.id.questionnaires_listView);
        swipeRefreshLayout.setColorScheme(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3,
                R.color.swipe_color_4);

        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new QuestionnairesAdapter(getActivity(), questionnaires);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!questionnaires.get(i).getDone()) {
                    ToastUtils.showShort("已经做过了哦");
                } else {
                    Intent intent = new Intent(getActivity(), QuestionPager.class);
                    intent.putExtra("QuestionnaireId", questionnaires.get(i).getId() + "");
                    startActivity(intent);
                    if (!UserUtils.isHaveToast()) {
                        ToastUtils.showShort("左右滑动切换问题哦");
                        App.getContext().getSharedPreferences("user", 0).edit().putInt("haveOpen",1).commit();
                    }
                }
            }
        });
        AnimationAdapter animationAdapter = new ItemAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(listView);
        animationAdapter.getViewAnimator().setAnimationDelayMillis(30);
        animationAdapter.getViewAnimator().setAnimationDurationMillis(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        listView.setAdapter(animationAdapter);
        loadData();
        return rootView;
    }


    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        if (ToolUtils.isConnectInternet()) {
            executeRequest(new MyStringRequest(Request.Method.GET, Api.Host_ALIYUN + "news/" + UserUtils
                    .getUserId(), responseListener(), errorListener()));
        } else {
            if (!TextUtils.isEmpty(UserUtils.getCache("news"))) {
                Log.d("news cache", UserUtils.getCache("news"));
                List<QuestionnaireResponse> caches;
                caches = gson.fromJson(UserUtils.getCache("news"), listType);
                questionnaires.clear();
                for (QuestionnaireResponse q : caches) {
                    questionnaires.add(q);
                }
                mAdapter.notifyDataSetChanged();

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
            ToastUtils.showShort("网络未连接，不能捡肥皂");
        }

    }


    private Response.Listener<String> responseListener() {
        return new Response.Listener<String>() {
            List<QuestionnaireResponse> temple ;
            @Override
            public void onResponse(final String response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        Log.d("String response data _->", response);
                        temple = gson.fromJson(response, listType);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        UserUtils.cacheData("news", response);
                        questionnaires.clear();
                        for (QuestionnaireResponse q : temple) {
                            questionnaires.add(q);
                        }
                        mAdapter.notifyDataSetChanged();

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        },3000);
                    }
                });
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        questionnaires.clear();
        loadData();
        mAdapter.notifyDataSetChanged();
    }
}
