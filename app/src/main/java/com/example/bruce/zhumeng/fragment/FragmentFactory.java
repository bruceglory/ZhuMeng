package com.example.bruce.zhumeng.fragment;

import android.util.ArrayMap;
import android.util.Log;

/**
 * Created by bruce on 2015/12/29.
 */
public class FragmentFactory {

    private static final String TAG = FragmentFactory.class.getSimpleName();
    private static ArrayMap<Integer,BaseFragment> fragmentArrayMap= new ArrayMap<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment;
        fragment = fragmentArrayMap.get(position);
        if(fragment == null) {
            Log.e(TAG,"create Fragment" );
            switch (position){
                case 0:

            }

            if(fragment != null) {
                fragmentArrayMap.put(position,fragment);
            }

        }
        return fragment;
    }
}
