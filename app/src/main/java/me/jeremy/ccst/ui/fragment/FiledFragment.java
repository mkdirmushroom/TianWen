package me.jeremy.ccst.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import me.drakeet.materialdialog.MaterialDialog;
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

    public FiledFragment() {}

    @SuppressLint("ValidFragment")
    public FiledFragment(QuestionResponse response, int num) {
        this.questionResponse = response;
        this.number = num;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filed, container, false);
        mTitle = (TextView) rootView.findViewById(R.id.field_title);
        mButton = (ImageButton) rootView.findViewById(R.id.field_button);
        //Dialog input EditText
        content = new EditText(getActivity());
        content.setBackground(getResources().getDrawable(R.drawable.edit_text));
        content.setLines(4);

        mDisplayView = (TextView) rootView.findViewById(R.id.field_textView);
        mTitle.setText(number + "." + questionResponse.getTitle() +
                ParamsUtils.getQuestionTypeParams(questionResponse.getQuestionType()));

        questionId = questionResponse.getId();
        filedData = Records.getStringDataCenter().get(questionId);
        if (filedData != null) {
            mDisplayView.setVisibility(View.VISIBLE);
            mDisplayView.setText(filedData);
            content.setText(filedData);
        }
        final MaterialDialog mDialog = new MaterialDialog(getActivity());
        mDialog.setTitle("输入答案吧");
        mDialog.setContentView(content);
        mDialog.setPositiveButton("确定",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentData = content.getText().toString().trim();
                filedData = currentData;
                if (Records.queryInt(questionId)) {
                    Records.getStringDataCenter().remove(questionId);
                    Records.getStringDataCenter().put(questionId, filedData);
                } else {
                    Records.getStringDataCenter().put(questionId, filedData);
                }

                YoYo.with(Techniques.BounceInDown).playOn(mDisplayView);
                mDisplayView.setText(currentData);
                mDialog.dismiss();
                //when submit ,hide the keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
                content.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(content.getWindowToken(), 1);
            }
        });

        return rootView;
    }
}
