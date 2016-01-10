package com.example.bruce.zhumeng.data;

import android.os.Handler;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.bruce.zhumeng.entities.SchoolInfo;
import com.example.bruce.zhumeng.fragment.SchoolFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 2015/12/30.
 */
public class SchoolDataAccess implements BaseDataAccess<List<SchoolInfo>> {

    private int index =0;
    private Handler handler;
    private List<SchoolInfo> schools = new ArrayList<>();
    private List<SchoolInfo> syncSchools = new ArrayList<>();
    private SchoolFragment schoolFragment;

    public SchoolDataAccess(SchoolFragment schoolFragment){
        this.schoolFragment = schoolFragment;
    }
    @Override
    public List<SchoolInfo> parseAVObject(List<AVObject> list) {
        List<SchoolInfo> schoolInfoList = new ArrayList<>();
        Log.d("zhang","parseAvobject");
        for(int i=0;i<list.size();i++) {

            int id = list.get(i).getInt("schoolId");
            String schoolName = list.get(i).getString("SchoolName");
            String pictureUrl = list.get(i).getAVFile("photo").getUrl();
            boolean _985 = list.get(i).getBoolean("985");
            boolean _211 = list.get(i).getBoolean("211");

            SchoolInfo schoolInfo = new SchoolInfo(id, schoolName, pictureUrl, _985, _211);
            schoolInfoList.add(schoolInfo);
        }
        return schoolInfoList;
    }


    @Override
    public  List<SchoolInfo> load(String tableName) {
        AVQuery<AVObject> query = new AVQuery<>(tableName);
        if(index+3 >= schools.size() && schools.size()<10) {
            index++;
            query.whereGreaterThan("schoolId", (index - 1) * 3);
            query.whereLessThan("schoolId", index * 3 + 1);
            query.orderByAscending("schoolId");

            synchronized(syncSchools) {
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> school, AVException e) {
                        if (e == null) {
                            syncSchools.addAll(parseAVObject(school));
                            Log.d("zhang","syncschools.size"+syncSchools.size());
                            Log.d("zhang", "schoolsize+" + school.size());

                        } else {
                            Log.d("zhang", "error:" + e.getMessage());
                        }
                    }
                });
            }
        }

        Log.d("zhang","schools+"+schools.size());
        return schools;
    }

    public List<SchoolInfo> loadData(int currentPostion,String tableName) {
        index = currentPostion;
        return load(tableName);
    }


}
