package com.example.bruce.zhumeng.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.R;
import com.example.bruce.zhumeng.adapter.ProvinceAdapter;
import com.example.bruce.zhumeng.adapter.ScoreLineAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GKX100127 on 201listDataLen/1/13.
 */
public class ScoreLinesFragment extends BaseFragment
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG             = ScoreLinesFragment.class.getSimpleName();
    private final        int    listDataLen      = 6;
    private static final int    LOAD_SCORE_SUCC = 1;

    private final String[] databaseName   = {"ScoreLine11", "ScoreLine12", "ScoreLine13", "ScoreLine14", "ScoreLine15"};
    private final String[] firstBatchSci  = new String[listDataLen];
    private final String[] secondBatchSci = new String[listDataLen];
    private final String[] thirdBatchSci  = new String[listDataLen];
    private final String[] firstBatchArt  = new String[listDataLen];
    private final String[] secondBatchArt = new String[listDataLen];
    private final String[] thirdBatchArt  = new String[listDataLen];

    private List<String> province = new ArrayList<>();

    private final String[] years = {" ", "2011", "2012", "2013", "2014", "2015"};
    private ListView scienceListView;
    private ListView artListView;
    private ListView provinceListView;

    private ProvinceAdapter  provinceAdapter;
    private ScoreLineAdapter scienceAdapter;
    private ScoreLineAdapter artAdapter;
    private PopupWindow      provincePopupWindow;
    private Toolbar          toolbar;
    private LinearLayout     scoreProvinceSelected;
    private TextView         provinceName;

    private ScoreLinesHandler scoreLinesHandler;

    public static ScoreLinesFragment newInstance() {
        Log.d(TAG, "create BatchSciLinesFragment");
        return new ScoreLinesFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.score_line_mainview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        findView(mRootView);
        initialize();
        if (artAdapter == null)
            artAdapter = new ScoreLineAdapter(getActivity(), years, firstBatchArt, secondBatchArt, thirdBatchArt);
        if (scienceAdapter == null)
            scienceAdapter = new ScoreLineAdapter(getActivity(), years, firstBatchSci, secondBatchSci, thirdBatchSci);
        if (provinceAdapter == null)
            provinceAdapter = new ProvinceAdapter(getActivity(), province);
        scienceListView.setAdapter(scienceAdapter);
        artListView.setAdapter(artAdapter);
    }

    private void findView(View rootView) {
        scienceListView = (ListView) rootView.findViewById(R.id.science_listview);
        artListView = (ListView) rootView.findViewById(R.id.art_human_science_listView);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        scoreProvinceSelected = (LinearLayout) toolbar.findViewById(R.id.score_province_select);
        provinceName = (TextView) toolbar.findViewById(R.id.score_province);
        scoreProvinceSelected.setOnClickListener(this);
    }

    private void initialize() {
        scoreLinesHandler = new ScoreLinesHandler(this);
        firstBatchSci[0] = getResources().getString(R.string.first_batch);
        secondBatchSci[0] = getResources().getString(R.string.second_batch);
        thirdBatchSci[0] = getResources().getString(R.string.third_batch);
        initializeData();
    }

    private void initializeData() {
        loadProvinceData();
        loadBatchSciData(1);
    }

    private void loadProvinceData() {
        AVQuery<AVObject> provinceQuery = new AVQuery<>("Province");
        provinceQuery.whereGreaterThan("provinceId", 0);
        provinceQuery.whereLessThan("provinceId", 32);
        provinceQuery.orderByAscending("provinceId");
        provinceQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> provinceList, AVException e) {
                if (e == null) {
                    province.clear();
                    for (AVObject pro : provinceList) {
                        province.add(pro.getString("provinceName"));
                    }
                } else {
                    Log.d("zhang", "query fail");
                }
            }
        });
    }

    private void loadBatchSciData(int provinceId) {

        for (int i = 0; i < databaseName.length; i++) {
            final int position = i + 1;
            AVQuery<AVObject> scoreQuery = new AVQuery<>(databaseName[i]);
            scoreQuery.whereEqualTo("provinceId", provinceId);

            scoreQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> scoreList, AVException e) {
                    if (e == null) {
                        Log.d("zhang", "scoreList size=" + scoreList.size());
                        for (AVObject score : scoreList) {
                            firstBatchSci[position] = score.getString("firstBatchSci");
                            secondBatchSci[position] = score.getString("secondBatchSci");
                            thirdBatchSci[position] = score.getString("thirdBatchSci");
                            firstBatchArt[position] = score.getString("firstBatchArt");
                            secondBatchArt[position] = score.getString("secondBatchArt");
                            thirdBatchArt[position] = score.getString("thirdBatchArt");
                        }
                        if (firstBatchSci.length == listDataLen) {
                            scoreLinesHandler.sendEmptyMessage(LOAD_SCORE_SUCC);
                        }
                    } else {
                        Log.d("zhang", "load socre exception" + e);
                    }
                }
            });
        }
    }

    private void showPopupWindow() {
        if (provincePopupWindow == null) {

            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.simple_popupwindow_view, null);
            provinceListView = (ListView) view.findViewById(R.id.popupwindow_listView);
            provinceListView.setAdapter(provinceAdapter);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    provincePopupWindow.dismiss();
                }
            });
            view.startAnimation(AnimationUtils.
                    loadAnimation(getContext().getApplicationContext(), R.anim.fade_in));


            provincePopupWindow = new PopupWindow(getActivity());
            provincePopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            provincePopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            ColorDrawable cd = new ColorDrawable(0x7dc0c0c0);
            provincePopupWindow.setBackgroundDrawable(cd);

            provincePopupWindow.setOutsideTouchable(true);
            provincePopupWindow.setFocusable(true);
            provincePopupWindow.setContentView(view);
            provinceListView.setOnItemClickListener(this);
        }
        provincePopupWindow.showAsDropDown(toolbar, 0, 0);
        //provincePopupWindow.update();
    }

    @Override
    public void onClick(View view) {
        if (view == scoreProvinceSelected) {
            //31 provinces
            if (province.size() == 31) {
                showPopupWindow();
            } else {
                Toast.makeText(getActivity(), "network error", Toast.LENGTH_SHORT).show();
                loadProvinceData();
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("zhang", "position=" + position);

        provinceName.setText(province.get(position));
        int provinceId = position + 1;
        loadBatchSciData(provinceId);
        provinceAdapter.setSelectedItem(position);
        provinceAdapter.notifyDataSetChanged();
        if (provincePopupWindow != null) {
            provincePopupWindow.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static class ScoreLinesHandler extends Handler {
        WeakReference<ScoreLinesFragment> mScoreLinesFragment;

        public ScoreLinesHandler(ScoreLinesFragment scoreLinesFragment) {
            mScoreLinesFragment = new WeakReference<>(scoreLinesFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final ScoreLinesFragment scoreLinesFragment = mScoreLinesFragment.get();
            if (scoreLinesFragment != null) {
                if (msg.what == LOAD_SCORE_SUCC) {
                    scoreLinesFragment.artAdapter.notifyDataSetChanged();
                    scoreLinesFragment.scienceAdapter.notifyDataSetChanged();
                }

            }
        }
    }
}
