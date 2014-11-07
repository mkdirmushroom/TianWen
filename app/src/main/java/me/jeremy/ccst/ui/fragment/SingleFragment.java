package me.jeremy.ccst.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jeremy.ccst.App;
import me.jeremy.ccst.R;
import me.jeremy.ccst.data.center.Records;
import me.jeremy.ccst.model.question.ChooseResponse;
import me.jeremy.ccst.model.question.QuestionResponse;
import me.jeremy.ccst.utils.ParamsUtils;

/**
 * Created by qiugang on 2014/9/27.
 */
public class SingleFragment extends Fragment {

    private QuestionResponse questionResponse;

    private List<ChooseResponse> chooseResponses;

    private ListView listView;

    private TextView title;

    private List<Integer> records = new ArrayList<Integer>();

    private int number;

    public SingleFragment() {}

    public SingleFragment(QuestionResponse response, int num) {
        this.questionResponse = response;
        this.number = num;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_single, container, false);
        chooseResponses = questionResponse.getChoose();
        listView = (ListView) rootView.findViewById(R.id.single_listview);

        listView.setAdapter(new singleAdapter());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        title = (TextView) rootView.findViewById(R.id.single_title);
        title.setText(number+"." + questionResponse.getTitle() +
                ParamsUtils.getQuestionTypeParams(questionResponse.getQuestionType()));
        return rootView;
    }

    public class singleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chooseResponses.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Integer chooseId = chooseResponses.get(position).getId();
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.item_choose, null);
            final CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(R.id.item_choose_text);
            checkedTextView.setText(chooseResponses.get(position).getDetail());
            if (null != Records.getDataCenter().get(questionResponse.getId())) {
                records = Records.getDataCenter().get(questionResponse.getId());
            }

           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   saveData();
                   Log.d("the" + questionResponse.getId() + "'s choose id", Records.getDataCenter().get(questionResponse.getId()).toString());
               }
           });
            if (records != null ) {
                if (Records.queryInt(records, chooseId)) {
                    listView.setItemChecked(position, true);
                } else {
                    listView.setItemChecked(position, false);
                }
            }
            return convertView;
        }
    }

    public  void saveData() {
        int position = listView.getCheckedItemPosition();
        int chooseId = 0;
        Log.d("被选择的选项位置", position + "");
        if (position >= 0) {
           chooseId = chooseResponses.get(position).getId();
        }
        if (!Records.queryInt(records, chooseId)) {
            if (records != null) {
                records.clear();
            }
            Records.addInt(records, chooseId);
        } else if (Records.queryInt(records, chooseId)) {
            Records.deleteInt(records, chooseId);
        }
        Records.getDataCenter().put(questionResponse.getId(), records);
    }
}
