package me.jeremy.ccst.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.jeremy.ccst.R;
import me.jeremy.ccst.api.Api;
import me.jeremy.ccst.api.TypeParams;
import me.jeremy.ccst.data.GsonRequest;
import me.jeremy.ccst.data.MyStringRequest;
import me.jeremy.ccst.data.RequestManager;
import me.jeremy.ccst.data.center.Records;
import me.jeremy.ccst.model.question.CreateAnswerDetailRequest;
import me.jeremy.ccst.model.question.CreateAnswerSheetRequest;
import me.jeremy.ccst.model.question.CreateQuestionAnswer;
import me.jeremy.ccst.model.question.QuestionResponse;
import me.jeremy.ccst.model.question.QuestionnaireDetailResponse;
import me.jeremy.ccst.ui.fragment.FiledFragment;
import me.jeremy.ccst.ui.fragment.MultiFragment;
import me.jeremy.ccst.ui.fragment.SingleFragment;
import me.jeremy.ccst.utils.ParamsUtils;
import me.jeremy.ccst.utils.TaskUtils;
import me.jeremy.ccst.utils.ToastUtils;
import me.jeremy.ccst.utils.ToolUtils;
import me.jeremy.ccst.utils.UserUtils;

/**
 * Created by qiugang on 2014/9/27.
 */
public class QuestionPager extends FragmentActivity {

    private Gson gson = new Gson();

    private Type type = new TypeToken<QuestionnaireDetailResponse>() {
    }.getType();

    private List<QuestionResponse> questions = new ArrayList<QuestionResponse>();

    private QuestionnaireDetailResponse questionnaireDetailResponse;

    private String id = null;

    private ActionBar mActionBar;

    private Menu mMenu;

    private ProgressDialog progressDialog ;

    private int maxPosition = -1;

    private boolean commitData = false;

    private boolean firstGet = true;

    private boolean firstPost = true;

    /**
     * Answer params
     */

    private CreateAnswerSheetRequest answerSheet = new CreateAnswerSheetRequest();

    private List<CreateQuestionAnswer> answers = new ArrayList<CreateQuestionAnswer>();

    private JSONObject postJson;


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_pager);
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);

        //Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        progressDialog = new ProgressDialog(this);
        loadInitialData();
    }

    private void loadInitialData() {
        progressDialog.setMessage("玩儿命获取中");
        progressDialog.show();
        id = getIntent().getStringExtra("QuestionnaireId");
        if (ToolUtils.isConnectInternet()) {
            executeRequest(new MyStringRequest(Request.Method.GET,Api.HOSTS[Api.HOST_POSITION] + Api.DETAIL + id,
                    responseListener(), errorListener()));
        } else {
            progressDialog.dismiss();
            ToastUtils.showShort("网络未连接，不能捡肥皂");
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String questionType = questions.get(position).getQuestionType();
            if (position == questions.size() - 1) {
                mMenu.findItem(R.id.action_save).setVisible(true);
                commitData = true;
            }
            if (TypeParams.QUESTION_FIELD.equals(questionType)) {
                return new FiledFragment(questions.get(position), position + 1);
            } else if (TypeParams.QUESTION_CHOOSE_SINGEL.equals(questionType)) {
                return new SingleFragment(questions.get(position),
                        position + 1);
            } else {
                return new MultiFragment(questions.get(position),
                        position + 1);
            }
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }

    /**
     * Post the answer data
     */
    private void postData() {
        if (UserUtils.getUserId() != 0) {
            executeRequest(new GsonRequest<Boolean>(Api.HOSTS[Api.HOST_POSITION] + Api.POSTDATA, postJson,
                    Boolean.class, postDataResponseListener(), postErrorListener()));
        } else {
            ToastUtils.showShort("没有登录哦,别想碰我");
        }
    }

    /**
     * Initial the post data
     */
    private void initPostData() {
        int position = 0;
        Integer questionId = 0;
        String questionType = null;
        for (; position < questions.size(); position++) {
            CreateQuestionAnswer questionAnswer = new CreateQuestionAnswer();
            List<CreateAnswerDetailRequest> answerDetails = new ArrayList<CreateAnswerDetailRequest>();
            //get question id
            questionId = questions.get(position).getId();
            questionType = questions.get(position).getQuestionType();
            questionAnswer.setQuestionId(questionId);
            questionAnswer.setQuestionType(questionType);

            if (questionType.equals(TypeParams.QUESTION_FIELD)) {
                if (Records.getStringDataCenter().get(questionId) != null) {
                    CreateAnswerDetailRequest detail = new CreateAnswerDetailRequest();
                    detail.setContent(Records.getStringDataCenter().get(questionId));
                    answerDetails.add(detail);
                }
            } else {
                if (Records.getDataCenter().get(questionId) != null) {
                    for (Integer integer : Records.getDataCenter().get(questionId)) {
                        CreateAnswerDetailRequest detail = new CreateAnswerDetailRequest();
                        detail.setAnswerId(integer);
                        answerDetails.add(detail);
                    }
                }
                Log.d(questionId + "添加了", answerDetails.toString());
            }
            questionAnswer.setAnswers(answerDetails);
            answers.add(questionAnswer);
        }
        answerSheet.setQuestions(answers);
        answerSheet.setQuestionnaireId(Integer.parseInt(id));
        answerSheet.setUserId(UserUtils.getUserId());
        if (Records.getDataCenter() != null) {

            String params = new Gson().toJson(answerSheet);
            Log.d("提交的数据=======》", params);
            try {
                postJson = new JSONObject(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean haveDoneAllQuestion() {
        int questionId;
        for (int position = 0; position < questions.size() ; position++) {
            questionId = questions.get(position).getId();
            if (TypeParams.QUESTION_FIELD.equals(questions.get(position).getQuestionType())) {
                if (Records.getStringDataCenter().get(questionId) == null
                       || "".equals(Records.getStringDataCenter().get(questionId))) {
                    Log.d("未填充完填空数据","");
                    return false;
                }
            } else {
                if (Records.getDataCenter().get(questionId) != null ) {
                    if ( Records.getDataCenter().get(questionId).size() == 0) {
                        Log.d("答案数目为空", Records.getDataCenter().get(questionId).size()+ "");
                        return false;
                    }
                } else {
                    Log.d("未填充完选择题目数据","");
                    return false;
                }
            }
        }
        return true;
    }


    private Response.Listener<String> responseListener() {
        return new Response.Listener<String>() {
            QuestionnaireDetailResponse temple;

            @Override
            public void onResponse(final String response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {

                    @Override
                    protected Object doInBackground(Object... params) {
                        temple = gson.fromJson(response, type);
                        questionnaireDetailResponse = temple;
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        progressDialog.dismiss();
                        questions.clear();
                        for (QuestionResponse q : temple.getQuestions()) {
                            questions.add(q);
                        }
                        maxPosition = questions.size() - 1;
                        mPagerAdapter.notifyDataSetChanged();
                        mActionBar.setTitle(ParamsUtils.getQuestionNums(questions.size()));
                    }
                });
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
//                    Log.d("get数据切换到备用服务器", "true");
//                    progressDialog.dismiss();
//                    if (firstGet) {
//                        loadInitialData(Api.Host_ALIYUN_SLAVE);
//                        firstGet = false;
//                    } else {
                        progressDialog.dismiss();
                        ToastUtils.showShort("当前网络信号不好哦");
//                    }
                }
            }
        };
    }

    protected Response.ErrorListener postErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
//                    Log.d("post数据切换到熬备用服务器","");
//                    progressDialog.dismiss();
//                    if (firstPost) {
//                        postData(Api.Host_ALIYUN_SLAVE);
//                        firstPost = false;
//                    } else {
                        progressDialog.dismiss();
                        ToastUtils.showShort("当前网络信号不好哦");
//                    }
                }
            }
        };
    }

    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    private Response.Listener<Boolean> postDataResponseListener() {
        return new Response.Listener<Boolean>() {
            @Override
            public void onResponse(final Boolean response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        progressDialog.dismiss();
                        Log.d("提交数据结果=======》", response.toString());
                        if (true == response) {
                            Records.getDataCenter().clear();
                            Records.getStringDataCenter().clear();
                            ToastUtils.showShort("提交成功");
                            QuestionPager.this.finish();
                        } else if (false == response) {
                            ToastUtils.showShort("提交失败");
                        }
                    }
                });
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question, menu);
        mMenu = menu;
        menu.findItem(R.id.action_save).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            if (commitData && haveDoneAllQuestion()) {
                if (ToolUtils.isConnectInternet()) {
                    progressDialog.setMessage("玩儿命提交中");
                    progressDialog.show();
                    initPostData();
                    postData();
                } else {
                    ToastUtils.showShort("网络未连接，不能扔肥皂");
                }
            } else {
                ToastUtils.showShort("没做完不能提交哦");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Records.getDataCenter().clear();
        Records.getStringDataCenter().clear();
    }
}
