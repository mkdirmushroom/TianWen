package me.jeremy.ccst.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.jeremy.ccst.R;
import me.jeremy.ccst.adapter.AllCategorysAdapter;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.data.MyStringRequest;
import me.jeremy.ccst.model.CategoryResponse;
import me.jeremy.ccst.ui.QuetionareListActivity;
import me.jeremy.ccst.utils.TaskUtils;


/**
 * Created by qiugang on 2014/9/28.
 */
public class AllCategoryFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private StaggeredGridView mGridView;
    private View mEmptyView;

    private AllCategorysAdapter mAdapter;
    private List<CategoryResponse> categories  = new ArrayList<CategoryResponse>();
    private Type listType = new TypeToken<ArrayList<CategoryResponse>>(){}.getType();

    private Gson mGson = new Gson();

    public AllCategoryFragment() {

    }

    public static AllCategoryFragment newInstance() {
        AllCategoryFragment fragment = new AllCategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_category, container, false);
        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_fragment_all_node);
        mEmptyView = rootView.findViewById(R.id.progress_fragment_all_node);
        mGridView.setEmptyView(mEmptyView);
        mGridView.setOnItemClickListener(this);
        mAdapter = new AllCategorysAdapter(categories, getActivity());
        mGridView.setAdapter(mAdapter);
        executeRequest(new MyStringRequest(Api.Host_ALIYUN + "/categories", myListener(), errorListener()));
        return rootView;
    }

    private Response.Listener<String> myListener() {
        return new Response.Listener<String>() {
            private List<CategoryResponse> temple;
            @Override
            public void onResponse(final String response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {

                    @Override
                    protected Object doInBackground(Object... params) {
                        temple = mGson.fromJson(response, listType);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        categories.clear();
                        for (CategoryResponse c:temple) {
                            categories.add(c);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), QuetionareListActivity.class);
        intent.putExtra("categoryId",categories.get(position).getId());
        intent.putExtra("categoryName",categories.get(position).getName());
        Log.d("put categoryId",categories.get(position).getId()+"");
        startActivity(intent);
    }
}
