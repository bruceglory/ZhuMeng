package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GKX100127 on 2016/1/11.
 */
public class ScoreLineAdapter extends BaseAdapter {
    private Context context;
    private List<String> years = new ArrayList<>();
    private List<String> firstScore = new ArrayList<>();
    private List<String> secondScore = new ArrayList<>();
    private List<String> thirdScore = new ArrayList<>();

    public ScoreLineAdapter(Context context, List<String> years, List<String> firstScore,
                            List<String> secondScore,List<String> thirdScore) {
        this.context = context;
        this.years = years;
        this.firstScore = firstScore;
        this.secondScore = secondScore;
        this.thirdScore = thirdScore;
    }

    @Override
    public int getCount() {
        return years.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScoreHolder scoreHolder = null;
        if(convertView == null) {
            scoreHolder = new ScoreHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.score_line_detail,null);
            scoreHolder.year = (TextView) convertView.findViewById(R.id.year);
            scoreHolder.firstScore = (TextView) convertView.findViewById(R.id.first_batch);
            scoreHolder.secondScore = (TextView) convertView.findViewById(R.id.second_batch);
            scoreHolder.thirdScore = (TextView) convertView.findViewById(R.id.third_batch);
            convertView.setTag(scoreHolder);
        } else {
            scoreHolder = (ScoreHolder)convertView.getTag();

        }
        scoreHolder.year.setText(years.get(position));
        scoreHolder.firstScore.setText(firstScore.get(position));
        scoreHolder.secondScore.setText(secondScore.get(position));
        scoreHolder.thirdScore.setText(thirdScore.get(position));

        return convertView;
    }

    class ScoreHolder {
        TextView year;
        TextView firstScore;
        TextView secondScore;
        TextView thirdScore;
    }
}
