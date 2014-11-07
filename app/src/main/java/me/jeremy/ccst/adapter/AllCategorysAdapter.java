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
import me.jeremy.ccst.model.CategoryResponse;

/**
 * Created by qiugang on 14-10-12.
 */
public class AllCategorysAdapter  extends BaseAdapter {

    private List<CategoryResponse> mData;

    private Context mContext;

    public AllCategorysAdapter (List<CategoryResponse> categories, Context context) {
        this.mData = categories;
        this.mContext = context;
    }

    public class ViewHolder {
        TextView title;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
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
        if (null == convertView) {
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.view_category, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.view_category_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mData.get(position).getName());
        return convertView;
    }

}
