package com.fangzuo.assist.Utils;

import android.database.Cursor;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Dao.AddrBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.greendao.gen.AddrBeanDao;
import com.fangzuo.greendao.gen.BuyAtBeanDao;
import com.fangzuo.greendao.gen.BuyBeanDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.NoteBeanDao;

public class LocDataUtil {

    //删除盘点方案
    public static void deletePd(String fid){
        if (null==fid || "".equals(fid))return;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        String sql="DELETE  FROM PDSUB WHERE FID = ?";
        daoSession.getDatabase().execSQL(sql,new String[]{fid});
    }
    public static boolean checkBuyBean(String string){
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        BuyBeanDao dao = daoSession.getBuyBeanDao();
        return dao.queryBuilder().where(BuyBeanDao.Properties.FName.eq(string)).count()>0;
    }
    public static boolean checkAddrBean(String string){
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        AddrBeanDao dao = daoSession.getAddrBeanDao();
        return dao.queryBuilder().where(AddrBeanDao.Properties.FName.eq(string)).count()>0;
    }
    public static long checkNoteBean(String string){
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        NoteBeanDao dao = daoSession.getNoteBeanDao();
        return dao.queryBuilder().where(NoteBeanDao.Properties.NBuyName.eq(string)).count();
    }
    public static long checkUseNum4Addr(String string){
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        BuyAtBeanDao dao = daoSession.getBuyAtBeanDao();
        return dao.queryBuilder().where(BuyAtBeanDao.Properties.FAddrName.eq(string)).count();
    }
    //添加说明历史，若是存在，不重复添加
    public static void addAddrBean(String string){
        if ("".equals(string))return;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        AddrBeanDao dao = daoSession.getAddrBeanDao();
        //当不存在时，采取添加
        if (dao.queryBuilder().where(AddrBeanDao.Properties.FName.eq(string)).build().list().size()<=0){
            dao.insert(new AddrBean(string, CommonUtil.getTime(true), CommonUtil.getTimeLong(false)));
        }
    }
    //添加项目
    public static boolean addBuyBean(String string){
        boolean addok=true;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        BuyBeanDao dao = daoSession.getBuyBeanDao();
        //当不存在时，采取添加
        if (dao.queryBuilder().where(BuyBeanDao.Properties.FName.eq(string)).build().list().size()<=0){
            dao.insert(new BuyBean(string, CommonUtil.getTime(true), CommonUtil.getTimeLong(false)));
        }else{
            addok = false;
        }

        return addok;
    }

    public static String getBuyAtAllNum(String string){
//        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
//        NoteBeanDao dao = daoSession.getNoteBeanDao();
//        return dao.queryBuilder().where(NoteBeanDao.Properties.NAddrName.eq(string)).count();
        String num="0";
        String SQL = "SELECT sum(FNUM) as num,FBUY_NAME FROM BUY_AT_BEAN where FBUY_NAME =? GROUP by FBUY_NAME";
        Lg.e("SQL:"+SQL);
        Cursor cursor = GreenDaoManager.getmInstance(App.getContext()).getDaoSession().getDatabase().rawQuery(SQL, new String[]{string});
        while (cursor.moveToNext()) {
            num = cursor.getString(cursor.getColumnIndex("num"));
        }
        return num;
    }
    public static String getBuyAtAllSize(String string){
//        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
//        NoteBeanDao dao = daoSession.getNoteBeanDao();
//        return dao.queryBuilder().where(NoteBeanDao.Properties.NAddrName.eq(string)).count();
        String num="0";
        String SQL = "SELECT count() as num,FBUY_NAME FROM BUY_AT_BEAN where FBUY_NAME =?";
        Lg.e("SQL:"+SQL);
        Cursor cursor = GreenDaoManager.getmInstance(App.getContext()).getDaoSession().getDatabase().rawQuery(SQL, new String[]{string});
        while (cursor.moveToNext()) {
            num = cursor.getString(cursor.getColumnIndex("num"));
        }
        return num;
    }
}
