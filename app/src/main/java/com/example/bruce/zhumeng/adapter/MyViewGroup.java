package com.example.bruce.zhumeng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bruce on 2016/3/22.
 */
public class MyViewGroup extends ViewGroup {

    public MyViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount()>0){
            View childView = getChildAt(0);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int count = getChildCount();
        if(count>0) {
            for(int j = 0;i<count;j++) {
                View child =  getChildAt(j);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
    }
}
