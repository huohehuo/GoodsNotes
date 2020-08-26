package com.fangzuo.assist.Dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class CheckBeforeBean4List implements PropertyConverter<List<CheckBeforeBean.MaterielBean>, String> {
    @Override
    public List<CheckBeforeBean.MaterielBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<CheckBeforeBean.MaterielBean>> typeToken = new TypeToken<List<CheckBeforeBean.MaterielBean>>(){};
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<CheckBeforeBean.MaterielBean> arrays) {
        if (arrays == null||arrays.size()==0) {
            return null;
        } else {
            String sb = new Gson().toJson(arrays);
            return sb;

        }
    }
}
