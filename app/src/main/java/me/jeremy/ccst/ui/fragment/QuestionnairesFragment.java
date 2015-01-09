package me.jeremy.ccst.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import me.jeremy.ccst.data.RequestManager;
import me.jeremy.ccst.model.question.QuestionnaireResponse;
import me.jeremy.ccst.ui.QuestionPager;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;
import me.jeremy.ccst.view.CompleteView;

/**
 * Created by qiugang on 2014/9/22.
 */
public class QuestionnairesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView mListView;

    private View emptyView;

    private QuestionnairesAdapter mAdapter;

    private Gson gson = new Gson();

    private Type listType = new TypeToken<List<QuestionnaireResponse>>(){}.getType();

    private List<QuestionnaireResponse> questionnaires = new ArrayList<QuestionnaireResponse>();

    public int errorTime = 1;

    public static QuestionnairesFragment newInstance() {
        QuestionnairesFragment fragment = new QuestionnairesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_questionnaires, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.questionnaires_swipeRefresh);
        mListView = (ListView) rootView.findViewById(R.id.questionnaires_listView);
        emptyView = rootView.findViewById(R.id.progress_fragment_qustionnaires);
        swipeRefreshLayout.setColorScheme(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3,
                R.color.swipe_color_4);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        mAdapter = new QuestionnairesAdapter(getActivity(), questionnaires);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(emptyView);
        mListView.addFooterView(new CompleteView(getActivity()).getmCompleteView());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        //Set ListView animation
        AnimationAdapter animationAdapter = new ItemAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        animationAdapter.getViewAnimator().setAnimationDelayMillis(35);
        animationAdapter.getViewAnimator().setAnimationDurationMillis(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        mListView.setAdapter(animationAdapter);
        loadData(Api.HOSTS[Api.HOST_POSITION]);
        return rootView;
    }


    private void loadData(String host) {
        swipeRefreshLayout.setRefreshing(true);
        if (ToolUtils.isConnectInternet()) {
            executeRequest(new MyStringRequest(Request.Method.GET, host + Api.NEWS + UserUtils
                    .getUserId(), responseListener(), errorListener()));
        } else {
            loadCache();
        }

    }

    private void loadCache() {
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
            }, 3000);
        }
        ToastUtils.showShort("网络环境差，不能捡肥皂");
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
                        try {
                            //连接接联通wifi未登录产生异常
                            temple = gson.fromJson(response, listType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if (temple != null) {
                            if (temple.size() != 0) {
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
                                }, 3000);
                            } else {
                                loadCache();
                            }
                        } else {
                            loadCache();
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
                    if (errorTime <= Api.HOSTS.length) {
                        if (errorTime == Api.HOSTS.length) {
                            RequestManager.cancelAll(this);
                            swipeRefreshLayout.setRefreshing(false);
                            ToastUtils.showShort("oh my god,服务器都挂了");
                            loadCache();
                        } else {
                            if (Api.HOST_POSITION <= Api.HOSTS.length - 1 && questionnaires.size() == 0) {
                                Api.HOST_POSITION++;
                            }
                            if (Api.HOST_POSITION == Api.HOSTS.length) {
                                Api.HOST_POSITION = 0;
                            }
                            loadData(Api.HOSTS[Api.HOST_POSITION]);
                        }
                    }
                }
        };
    }

    @Override
    public void onRefresh() {
        loadData(Api.HOSTS[Api.HOST_POSITION]);
    }

    @Override
    public void onPause() {
        super.onPause();
//        errorTime = 1;
    }

    @Override
    public void onStart() {
        super.onStart();
        questionnaires.clear();
        loadData(Api.HOSTS[Api.HOST_POSITION]);
        mAdapter.notifyDataSetChanged();
    }
}
