package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class BuyAtBean {
    @Id(autoincrement = true)
    public Long id;
    public String FID;//名称
    public String FName;//数量
    public String FNum;//数量
    public String FAddrID;//
    public String FAddrName;
    public String FBuyID;
    public String FBuyName;
    public String FSum;//数量
    public String FPrice;//数量
    public String FColorName;
    public String FModelName;
    public String FStuffName;
    public String FUnitName;


    public String FCreateData;//创建日期
    public String FIsCloud;//是否备份云端
    public String FUseNum;//使用次数
    public String FMapID;//地图经纬

    public void setBuyBean(BuyBean bean){
        if (null == bean)return;
        FBuyID = bean.FID;
        FBuyName = bean.FName;
    }
    public void setAddrBean(AddrBean bean){
        if (null == bean)return;
        FAddrID = bean.FID;
        FAddrName = bean.FName;
    }




    @Generated(hash = 964875290)
    public BuyAtBean(Long id, String FID, String FName, String FNum, String FAddrID,
            String FAddrName, String FBuyID, String FBuyName, String FSum,
            String FPrice, String FColorName, String FModelName, String FStuffName,
            String FUnitName, String FCreateData, String FIsCloud, String FUseNum,
            String FMapID) {
        this.id = id;
        this.FID = FID;
        this.FName = FName;
        this.FNum = FNum;
        this.FAddrID = FAddrID;
        this.FAddrName = FAddrName;
        this.FBuyID = FBuyID;
        this.FBuyName = FBuyName;
        this.FSum = FSum;
        this.FPrice = FPrice;
        this.FColorName = FColorName;
        this.FModelName = FModelName;
        this.FStuffName = FStuffName;
        this.FUnitName = FUnitName;
        this.FCreateData = FCreateData;
        this.FIsCloud = FIsCloud;
        this.FUseNum = FUseNum;
        this.FMapID = FMapID;
    }
    @Generated(hash = 633929490)
    public BuyAtBean() {
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
    public String getFAddrID() {
        return this.FAddrID;
    }
    public void setFAddrID(String FAddrID) {
        this.FAddrID = FAddrID;
    }
    public String getFAddrName() {
        return this.FAddrName;
    }
    public void setFAddrName(String FAddrName) {
        this.FAddrName = FAddrName;
    }
    public String getFBuyID() {
        return this.FBuyID;
    }
    public void setFBuyID(String FBuyID) {
        this.FBuyID = FBuyID;
    }
    public String getFBuyName() {
        return this.FBuyName;
    }
    public void setFBuyName(String FBuyName) {
        this.FBuyName = FBuyName;
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
    public String getFNum() {
        return this.FNum;
    }
    public void setFNum(String FNum) {
        this.FNum = FNum;
    }
    public String getFColorName() {
        return this.FColorName;
    }
    public void setFColorName(String FColorName) {
        this.FColorName = FColorName;
    }
    public String getFModelName() {
        return this.FModelName;
    }
    public void setFModelName(String FModelName) {
        this.FModelName = FModelName;
    }
    public String getFStuffName() {
        return this.FStuffName;
    }
    public void setFStuffName(String FStuffName) {
        this.FStuffName = FStuffName;
    }
    public String getFUnitName() {
        return this.FUnitName;
    }
    public void setFUnitName(String FUnitName) {
        this.FUnitName = FUnitName;
    }
    public String getFPrice() {
        return this.FPrice;
    }
    public void setFPrice(String FPrice) {
        this.FPrice = FPrice;
    }
    public String getFSum() {
        return this.FSum;
    }
    public void setFSum(String FSum) {
        this.FSum = FSum;
    }


}
