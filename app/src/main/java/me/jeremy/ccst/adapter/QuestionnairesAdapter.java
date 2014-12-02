package me.jeremy.ccst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.jeremy.ccst.App;
import me.jeremy.ccst.R;
import me.jeremy.ccst.model.question.QuestionnaireResponse;
import me.jeremy.ccst.utils.ParamsUtils;

/**
 * Created by qiugang on 2014/9/22.
 */
public class QuestionnairesAdapter extends BaseAdapter {

    private Context mContext;

    private List<QuestionnaireResponse> data;

//  private Resources res = App.getContext().getResources();

    public QuestionnairesAdapter(Context context, List<QuestionnaireResponse> questionnaires) {
        this.mContext = context;

        this.data = questionnaires;
    }

    public class ViewHolder {
        private TextView shot;
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
        String topic = data.get(position).getTopic();
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.shot = (TextView) convertView.findViewById(R.id.listrow_textview_shot);
            viewHolder.topic = (TextView) convertView.findViewById(R.id.listrow_textview_topic);
            viewHolder.date = (TextView) convertView.findViewById(R.id.listrow_textview_date);
            viewHolder.done = (TextView) convertView.findViewById(R.id.listrow_imageViwe_done);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder.topic.getText() != null) {
            if (data.get(position).getDone()) {
                viewHolder.done.setVisibility(View.GONE);
            } else {
                viewHolder.done.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.shot.setText(topic.substring(0,1) +"");
        viewHolder.shot.setBackgroundColor(mContext.getResources().getColor(
                ParamsUtils.colors[data.get(position).getId()%10]));
        if (topic.length() > 18) {
            viewHolder.topic.setText(topic.substring(0,18) + "...");
        } else {
            viewHolder.topic.setText(topic);
        }


        viewHolder.date.setText(ParamsUtils.getCreateTime(data.get(position).getDate().toString()));
        return convertView;
    }
}
