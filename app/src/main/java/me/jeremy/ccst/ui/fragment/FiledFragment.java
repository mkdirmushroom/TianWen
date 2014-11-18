package me.jeremy.ccst.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import me.jeremy.ccst.R;
import me.jeremy.ccst.data.center.Records;
import me.jeremy.ccst.model.question.QuestionResponse;
import me.jeremy.ccst.utils.ParamsUtils;

/**
 * Created by qiugang on 2014/9/28.
 */
public class FiledFragment extends Fragment {

    private TextView mTitle;

    private TextView mDisplayView;

    private QuestionResponse questionResponse;

    private EditText content;

    private ImageButton mButton;

    private String filedData;

    private Integer questionId;

    private int number;

    private Dialog mDialog;

    public FiledFragment(){}

    public FiledFragment(QuestionResponse response, int num) {
        this.questionResponse = response;
        this.number = num;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filed, container, false);
        mTitle = (TextView) rootView.findViewById(R.id.field_title);
        mButton = (ImageButton) rootView.findViewById(R.id.field_button);



        content = new EditText(getActivity());
        content.setBackground(getResources().getDrawable(R.drawable.edit_text));
        content.setLines(5);
        mDisplayView = (TextView) rootView.findViewById(R.id.field_textView);
        mTitle.setText(number+"." + questionResponse.getTitle() +
                ParamsUtils.getQuestionTypeParams(questionResponse.getQuestionType()));

        questionId = questionResponse.getId();
        filedData = Records.getStringDataCenter().get(questionId);
        if ( filedData != null){
            mDisplayView.setVisibility(View.VISIBLE);
            mDisplayView.setText(filedData);
            content.setText(filedData);
        }

        mDialog = new AlertDialog.Builder(getActivity())
                .setTitle("输入吧少年")
                .setView(content)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentData = content.getText().toString().trim();
                        if (!TextUtils.isEmpty(currentData)) {
                            filedData = currentData;
                            if (Records.queryInt(questionId)) {
                                Records.getStringDataCenter().remove(questionId);
                                Records.getStringDataCenter().put(questionId, filedData);
                            } else {
                                Records.getStringDataCenter().put(questionId, filedData);
                            }
                        }
                        YoYo.with(Techniques.BounceInDown).playOn(mDisplayView);
                        mDisplayView.setText(currentData);
                        //when submit ,hide the keyboard
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                    }
                })
                .create();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mDialog.show();
                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(content, 0);
            }
        });

        return rootView;
    }
}
