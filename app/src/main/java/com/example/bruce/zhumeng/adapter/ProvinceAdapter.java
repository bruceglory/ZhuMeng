package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bruce.zhumeng.R;

import java.util.List;

/**
 * Created by bruce on 2016/1/18.
 */
public class ProvinceAdapter extends BaseAdapter {
    private Context context;
    private List<String> province;
    private int currentSelectedView = 0;
    public ProvinceAdapter(Context context,List<String> province) {
        this.context = context;
        this.province = province;
    }

    @Override
    public int getCount() {
        return province.size();
    }

    @Override
    public Object getItem(int position) {
        return province.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Log.d("zhang","getView position="+position);
        ProvinceHolder provinceHolder ;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.province_list,null);
            provinceHolder = new ProvinceHolder(convertView);
            convertView.setTag(provinceHolder);
        } else {
            provinceHolder = (ProvinceHolder)convertView.getTag();
        }
        provinceHolder.provinceTV.setText(province.get(position));
        provinceHolder.provinceTV.setTextColor(context.getResources()
                .getColor(R.color.colorPrimary));
        if(currentSelectedView == position) {
            Log.d("zhang","currentselected="+currentSelectedView);
            provinceHolder.provinceTV.setTextColor(context.getResources()
                    .getColor(R.color.ColorPrimary));
        }
        return convertView;
    }

    public void setSelectedItem(int currentView) {
        currentSelectedView = currentView;

    }
    public class ProvinceHolder {
        private TextView provinceTV;

        public ProvinceHolder(View view) {
            provinceTV = (TextView) view.findViewById(R.id.province_name_tv);
        }
    }
}
