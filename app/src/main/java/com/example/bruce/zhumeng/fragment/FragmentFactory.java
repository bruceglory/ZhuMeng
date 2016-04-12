package com.example.bruce.zhumeng.fragment;

import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

/**
 * Created by bruce on 2015/12/29.
 */
public class FragmentFactory {

    private static final String TAG = FragmentFactory.class.getSimpleName();
    private static SparseArray<BaseFragment> fragmentArrayMap= new SparseArray<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment;
        fragment = fragmentArrayMap.get(position);
        if(fragment == null) {
            Log.e(TAG,"create Fragment" );
            switch (position){
                case 1:
                    fragment = SchoolsFragment.newInstance();
                    break;
                case 2:
                    fragment = MajorsFragment.newInstance();
                    break;
                case 3:
                    fragment = ScoreLinesFragment.newInstance();
                    break;
                case 4:
                    fragment = PsysFragment.newInstance();
                    break;
            }

            if(fragment != null) {
                fragmentArrayMap.put(position,fragment);
            }

        }
        return fragment;
    }
}
