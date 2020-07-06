package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class AddrBean {
    @Id(autoincrement = true)
    public Long id;
    public String FID;//名称
    public String FName;//名称
    public String FCreateData;//创建日期
    public String FIsCloud;//是否备份云端
    public String FUseNum;//使用次数
    public String FMapID;//地图经纬
    public AddrBean(String name, String data){
        FName = name;
        FCreateData = data;
    }
    public AddrBean(String name, String data,String fid){
        FName = name;
        FCreateData = data;
        FID = fid;
    }
    @Generated(hash = 181084517)
    public AddrBean(Long id, String FID, String FName, String FCreateData,
            String FIsCloud, String FUseNum, String FMapID) {
        this.id = id;
        this.FID = FID;
        this.FName = FName;
        this.FCreateData = FCreateData;
        this.FIsCloud = FIsCloud;
        this.FUseNum = FUseNum;
        this.FMapID = FMapID;
    }
    @Generated(hash = 306476614)
    public AddrBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getFIsCloud() {
        return this.FIsCloud;
    }
    public void setFIsCloud(String FIsCloud) {
        this.FIsCloud = FIsCloud;
    }
    public String getFUseNum() {
        return this.FUseNum;
    }
    public void setFUseNum(String FUseNum) {
        this.FUseNum = FUseNum;
    }
    public String getFMapID() {
        return this.FMapID;
    }
    public void setFMapID(String FMapID) {
        this.FMapID = FMapID;
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }

}
