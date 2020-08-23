package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class ColorBean {
    @Id(autoincrement = true)
    public Long id;
    public String FID;//名称
    public String FName;//名称
    public String FCreateData;//创建日期
    public ColorBean(String FName,  String FCreateData,String FID) {
        this.FID = FID;
        this.FName = FName;
        this.FCreateData = FCreateData;
    }

    @Generated(hash = 764159308)
    public ColorBean(Long id, String FID, String FName, String FCreateData) {
        this.id = id;
        this.FID = FID;
        this.FName = FName;
        this.FCreateData = FCreateData;
    }
    @Generated(hash = 521532773)
    public ColorBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFCreateData() {
        return this.FCreateData;
    }
    public void setFCreateData(String FCreateData) {
        this.FCreateData = FCreateData;
    }

}
