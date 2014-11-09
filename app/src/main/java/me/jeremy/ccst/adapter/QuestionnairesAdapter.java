package me.jeremy.ccst.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.jeremy.ccst.App;
import me.jeremy.ccst.R;
import me.jeremy.ccst.model.question.QuestionnaireResponse;

/**
 * Created by qiugang on 2014/9/22.
 */
public class QuestionnairesAdapter extends BaseAdapter {

    private Context mContext;

    private List<QuestionnaireResponse> data;

    private Resources res = App.getContext().getResources();

    public QuestionnairesAdapter(Context context, List<QuestionnaireResponse> questionnaires) {
        this.mContext = context;

        this.data = questionnaires;
    }

    public class ViewHolder {
        private TextView topic;
        private TextView date;
        private TextView done;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.topic = (TextView) convertView.findViewById(R.id.listrow_textview_topic);
            viewHolder.date = (TextView) convertView.findViewById(R.id.listrow_textview_date);
            viewHolder.done = (TextView) convertView.findViewById(R.id.listrow_imageViwe_done);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder.topic.getText() != null) {
            if (data.get(position).getDone()) {
                viewHolder.done.setBackground(res.getDrawable(R.drawable.todo));
            } else {
                viewHolder.done.setBackground(res.getDrawable(R.drawable.check));
            }
        }
        viewHolder.topic.setText(data.get(position).getTopic());
        viewHolder.date.setText(data.get(position).getDate().toString());
        return convertView;
    }
}
