package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * Created by NB on 2017/7/26.
 */

@Entity
public class CheckBeforeBean {
    @Id(autoincrement = true)
    public Long id;
    public String IcCardNo;
    public String SerialNumber;
    public String ErpOrderNo;
    public String CustomerOrSupplier;
    public String OrderType;
    //用到了这个Convert注解，表明它们的转换类，这样就可以转换成String保存到数据库中了
    @Convert(columnType = String.class, converter = CheckBeforeBean4List.class)
    private List<MaterielBean> MaterielList; //实体类中list数据
    @Generated(hash = 614433917)
    public CheckBeforeBean(Long id, String IcCardNo, String SerialNumber,
            String ErpOrderNo, String CustomerOrSupplier, String OrderType,
            List<MaterielBean> MaterielList) {
        this.id = id;
        this.IcCardNo = IcCardNo;
        this.SerialNumber = SerialNumber;
        this.ErpOrderNo = ErpOrderNo;
        this.CustomerOrSupplier = CustomerOrSupplier;
        this.OrderType = OrderType;
        this.MaterielList = MaterielList;
    }
    @Generated(hash = 443701490)
    public CheckBeforeBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIcCardNo() {
        return this.IcCardNo;
    }
    public void setIcCardNo(String IcCardNo) {
        this.IcCardNo = IcCardNo;
    }
    public String getSerialNumber() {
        return this.SerialNumber;
    }
    public void setSerialNumber(String SerialNumber) {
        this.SerialNumber = SerialNumber;
    }
    public String getErpOrderNo() {
        return this.ErpOrderNo;
    }
    public void setErpOrderNo(String ErpOrderNo) {
        this.ErpOrderNo = ErpOrderNo;
    }
    public String getCustomerOrSupplier() {
        return this.CustomerOrSupplier;
    }
    public void setCustomerOrSupplier(String CustomerOrSupplier) {
        this.CustomerOrSupplier = CustomerOrSupplier;
    }
    public String getOrderType() {
        return this.OrderType;
    }
    public void setOrderType(String OrderType) {
        this.OrderType = OrderType;
    }
    public List<MaterielBean> getMaterielList() {
        return this.MaterielList;
    }
    public void setMaterielList(List<MaterielBean> MaterielList) {
        this.MaterielList = MaterielList;
    }
    public class MaterielBean{
        public String MaterielID;
        public String MaterielName;
        public String Unit;
        public String Quantity;
    }

}
