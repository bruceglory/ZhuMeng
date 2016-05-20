package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.model.entities.Comment;
import com.example.bruce.zhumeng.model.entities.CommentInfo;
import com.example.bruce.zhumeng.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhang on 2016/5/12.
 */
public class CommentAdapter extends BaseAdapter {

    private List<CommentInfo> commentList ;
    private Context context;

    public CommentAdapter(Context context, List<CommentInfo> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();

        }
        Comment comment = commentList.get(position).getComment();
        viewHolder.comment.setText(comment.getComment());
        String time = TimeUtils.TimeDiff(context,comment.getUpdateTime());
        viewHolder.userName.setText(commentList.get(position).getUser().getUsername());
        viewHolder.time.setText(time);
        return convertView;
    }

    class ViewHolder {

        public ViewHolder(View view) {
            userName = (TextView) view.findViewById(R.id.username);
            comment = (TextView) view.findViewById(R.id.comment_mes);
            time = (TextView) view.findViewById(R.id.time);
        }
        private TextView userName;
        private TextView comment;
        private TextView time;
    }
}
