package me.jeremy.ccst.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.jeremy.ccst.R;
import me.jeremy.ccst.adapter.QuestionnairesAdapter;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.data.GsonRequest;
import me.jeremy.ccst.model.question.PageModel;
import me.jeremy.ccst.model.question.QuestionnaireResponse;
import me.jeremy.ccst.model.question.SearchQuestionnaireRequest;
import me.jeremy.ccst.ui.BaseActivity;
import me.jeremy.ccst.ui.QuestionPager;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.view.LoadingFooter;

/**
 * Created by qiugang on 14/10/30.
 */
public class SearchActivity extends BaseActivity {

    private ListView mListView;
    private LoadingFooter mLoadingFooter;
    private TextView mEmptyView;
    private String searchType = "C";
    private SearchQuestionnaireRequest searchRequest = new SearchQuestionnaireRequest();
    private Gson gson = new Gson();
    private List<QuestionnaireResponse> questionnaireResponses = new ArrayList<QuestionnaireResponse>();
    private QuestionnairesAdapter mAdapter;
    private JSONObject jsonObject;

    /*
    Page flags
     */
    private int page = 1;
    private boolean hasMore = true;
    private boolean hasRecords = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mListView = (ListView) findViewById(R.id.search_listView);
        mEmptyView = (TextView) findViewById(R.id.search_emptyview);
        mAdapter = new QuestionnairesAdapter(this, questionnaireResponses);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, QuestionPager.class);
                intent.putExtra("QuestionnaireId", questionnaireResponses.get(i).getId() + "");
                startActivity(intent);
            }
        });
        mLoadingFooter = new LoadingFooter(this);
        mListView.addFooterView(mLoadingFooter.getView());
//        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View mTitleView = mInflater.inflate(R.layout.search_action_bar_layout,
//                null);
//        getActionBar().setCustomView(
//                mTitleView,
//                new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        final SearchView searchView = (SearchView) mTitleView.findViewById(R.id.menu_searchview);
//
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        bindListView();
    }

    private void bindListView() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
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

    private void initSearchParams() {
        page = 1;
        hasMore = true;
        hasRecords = false;
        questionnaireResponses.clear();
        mAdapter.notifyDataSetChanged();
        mLoadingFooter.setState(LoadingFooter.State.Idle);
    }

    private void loadFirstPage() {
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        page = 1;
        loadData(page);
    }

    private void loadNextPage() {
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        page++;
        loadData(page);

    }

    private void loadData(final int page) {
        executeRequest(new GsonRequest<PageModel>(Api.Host_ALIYUN + "questionnaires/" +
                Api.getApiParams(page, 10), jsonObject, PageModel.class, responseListener(),
                searchErrorListener()));
    }

    private Response.Listener<PageModel> responseListener() {
        return new Response.Listener<PageModel>() {
            List<QuestionnaireResponse> questionnaires;

            @Override
            public void onResponse(final PageModel response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {

                    @Override
                    protected Object doInBackground(Object... objects) {
                        questionnaires = response.getRecords();
                        Log.d("返回数据", gson.toJson(response));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
                        if (questionnaires.size() != 0)
                            if (page == 1) {
                                questionnaireResponses.clear();
                                hasRecords = true;
                            }
                        if (questionnaires.size() >= 1) {
                            for (QuestionnaireResponse q : questionnaires) {
                                questionnaireResponses.add(q);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            hasMore = false;
                            mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                            if (!hasRecords) {
                                Log.d("hasRecords", hasRecords + "");
                                ToastUtils.showShort("没有搜索到内容哦，输入有效关键字或编号");
                            }
                        }
                    }
                });
            }
        };
    }

    protected Response.ErrorListener searchErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 500) {
                    ToastUtils.showShort("搜索条件选错了哦");
                } else {
                    ToastUtils.showShort("网络错误，请检查你的网络连接");
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search_view);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type something...");
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.DKGRAY);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText != null) {
                searchText.setTextColor(Color.WHITE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchRequest.setSearchType(searchType);
                if ("C".equals(searchType)) {
                    searchRequest.setCode(s);
                    searchRequest.setKeyWord("");
                } else {
                    searchRequest.setKeyWord(s);
                    searchRequest.setCode("");
                }
                String params = gson.toJson(searchRequest);
                Log.d("data", "request -> " + params);
                try {
                    jsonObject = new JSONObject(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initSearchParams();
                loadFirstPage();

                //when submit ,hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
