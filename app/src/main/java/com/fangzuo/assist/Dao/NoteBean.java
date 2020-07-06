package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class NoteBean {

    @Id(autoincrement = true)
    public Long id;
    public String FID;
    public String NBuyName;
    public String Ntime;//时间
    public String NCreateTime;
    public String NAddrName;
    @Generated(hash = 979808244)
    public NoteBean(Long id, String FID, String NBuyName, String Ntime,
            String NCreateTime, String NAddrName) {
        this.id = id;
        this.FID = FID;
        this.NBuyName = NBuyName;
        this.Ntime = Ntime;
        this.NCreateTime = NCreateTime;
        this.NAddrName = NAddrName;
    }
    @Generated(hash = 451626881)
    public NoteBean() {
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
    public String getNBuyName() {
        return this.NBuyName;
    }
    public void setNBuyName(String NBuyName) {
        this.NBuyName = NBuyName;
    }
    public String getNtime() {
        return this.Ntime;
    }
    public void setNtime(String Ntime) {
        this.Ntime = Ntime;
    }
    public String getNCreateTime() {
        return this.NCreateTime;
    }
    public void setNCreateTime(String NCreateTime) {
        this.NCreateTime = NCreateTime;
    }
    public String getNAddrName() {
        return this.NAddrName;
    }
    public void setNAddrName(String NAddrName) {
        this.NAddrName = NAddrName;
    }




    
}
