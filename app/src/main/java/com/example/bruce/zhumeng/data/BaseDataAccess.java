package com.example.bruce.zhumeng.data;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by bruce on 2015/12/30.
 */
public interface BaseDataAccess<T> {

    T load(String tableName) ;

    T parseAVObject(List<AVObject> list);
}
