package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;

import java.util.List;

/**
 * Created by zhang on 2016/4/15.
 */
public class ListDropDownMenuAdapter extends BaseAdapter {

    private Context      context;
    private List<String> list;
    private int checkItemPosition = 0;

    public ListDropDownMenuAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setCheckItemPosition(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_default_drop_down, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.textView.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.textView.setTextColor(ContextCompat.getColor(context, R.color.drop_down_selected));
                viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable
                        (context, R.mipmap.drop_down_checked), null);
            } else {
                viewHolder.textView.setTextColor(ContextCompat.getColor(context, R.color.drop_down_unselected));
                viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    static class ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
