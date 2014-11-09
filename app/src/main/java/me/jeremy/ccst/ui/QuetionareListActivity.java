package me.jeremy.ccst.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import me.jeremy.ccst.model.question.PageModel;
import me.jeremy.ccst.model.question.QuestionnaireResponse;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;
import me.jeremy.ccst.view.CompleteView;
import me.jeremy.ccst.view.LoadingFooter;

/**
 * Created by qiugang on 2014/10/17.
 */
public class QuetionareListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView mListView;

    private LoadingFooter mLoadingFooter;

    private QuestionnairesAdapter mAdapter;

    private int categoryId;

    private int page = 1;

    private boolean hasMore = true;

    private Gson gson = new Gson();

    private Type listType = new TypeToken<PageModel>() {
    }.getType();

    private List<QuestionnaireResponse> questionnaires = new ArrayList<QuestionnaireResponse>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questionnaires);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        getActionBar().setTitle(getIntent().getStringExtra("categoryName"));
        Log.d("CategoryId======>", categoryId + "");
        mListView = (ListView) findViewById(R.id.questionnaires_listView);

        mLoadingFooter = new LoadingFooter(this);
        mListView.addFooterView(mLoadingFooter.getView());
        mAdapter = new QuestionnairesAdapter(this, questionnaires);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!questionnaires.get(i).getDone()) {
                    ToastUtils.showShort("已经做过了哦");
                } else {
                    Intent intent = new Intent(QuetionareListActivity.this, QuestionPager.class);
                    intent.putExtra("QuestionnaireId", questionnaires.get(i).getId() + "");
                    startActivity(intent);
                    if (!UserUtils.isHaveToast()) {
                        ToastUtils.showShort("左右滑动切换问题哦");
                        App.getContext().getSharedPreferences("user", 0).edit().putInt("haveOpen", 1).commit();
                    }
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.questionnaires_swipeRefresh);
        swipeRefreshLayout.setColorScheme(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3,
                R.color.swipe_color_4);
        swipeRefreshLayout.setOnRefreshListener(this);
        AnimationAdapter animationAdapter = new ItemAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        animationAdapter.getViewAnimator().setAnimationDelayMillis(30);
        animationAdapter.getViewAnimator().setAnimationDurationMillis(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        mListView.setAdapter(animationAdapter);
        bindListView();
        loadFirstData();
    }

    private void loadFirstData() {
        page = 1;
        loadData(page);
    }

    private void loadNextPage() {
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        page++;
        loadData(page);
    }

    private void loadData(int page) {
        if (ToolUtils.isConnectInternet()) {
            if (UserUtils.logined(this)) {
                executeRequest(new MyStringRequest(Request.Method.GET,
                        Api.Host_ALIYUN + "CategoryQnSearch/" + Api.getApiParams(UserUtils.getUserId(), categoryId, page, 10),
                        responseListener(), errorListener()));
            } else {
                ToastUtils.showShort("没有登录哦");
            }
        } else {
            ToastUtils.showShort("网络未连接，不能捡肥皂");
        }

    }

    private void bindListView() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (mLoadingFooter.getState() == LoadingFooter.State.Loading
                        || mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
                    return;
                }
                if (firstVisibleItem + visibleItemCount >= totalItemCount
                        && totalItemCount != 0
                        && totalItemCount != mListView.getHeaderViewsCount()
                        + mListView.getFooterViewsCount() && mAdapter.getCount() > 0
                        && hasMore) {
                    loadNextPage();
                }
            }
        });
    }

    private Response.Listener<String> responseListener() {
        return new Response.Listener<String>() {
            PageModel pageModel;
            List<QuestionnaireResponse> temple;

            @Override
            public void onResponse(final String response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override

                    protected Object doInBackground(Object... params) {
                        Log.d("String response data _->", response);
                        pageModel = gson.fromJson(response, listType);
                        temple = pageModel.getRecords();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        mLoadingFooter.setState(LoadingFooter.State.Idle, 5000);
                        if (page == 1) {
                            questionnaires.clear();
                        }
                        if (temple.size() >= 1) {
                            for (QuestionnaireResponse q : temple) {
                                questionnaires.add(q);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            hasMore = false;
                            mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                            mListView.addFooterView(new CompleteView(QuetionareListActivity.this).getmCompleteView());
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
            }
        };
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

    @Override
    protected void onRestart() {
        super.onRestart();
        questionnaires.clear();
        page = 1;
        loadData(page);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        return;
    }
}
