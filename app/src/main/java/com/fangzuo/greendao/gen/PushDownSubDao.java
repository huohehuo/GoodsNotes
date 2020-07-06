package com.fangzuo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fangzuo.assist.Dao.PushDownSub;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PUSH_DOWN_SUB".
*/
public class PushDownSubDao extends AbstractDao<PushDownSub, Long> {

    public static final String TABLENAME = "PUSH_DOWN_SUB";

    /**
     * Properties of entity PushDownSub.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FName = new Property(1, String.class, "FName", false, "FNAME");
        public final static Property FNumber = new Property(2, String.class, "FNumber", false, "FNUMBER");
        public final static Property FModel = new Property(3, String.class, "FModel", false, "FMODEL");
        public final static Property FBillNo = new Property(4, String.class, "FBillNo", false, "FBILL_NO");
        public final static Property FInterID = new Property(5, String.class, "FInterID", false, "FINTER_ID");
        public final static Property FEntryID = new Property(6, String.class, "FEntryID", false, "FENTRY_ID");
        public final static Property FItemID = new Property(7, String.class, "FItemID", false, "FITEM_ID");
        public final static Property FUnitID = new Property(8, String.class, "FUnitID", false, "FUNIT_ID");
        public final static Property FAuxQty = new Property(9, String.class, "FAuxQty", false, "FAUX_QTY");
        public final static Property FAuxPrice = new Property(10, String.class, "FAuxPrice", false, "FAUX_PRICE");
        public final static Property FQtying = new Property(11, String.class, "FQtying", false, "FQTYING");
        public final static Property FDCStockID = new Property(12, String.class, "FDCStockID", false, "FDCSTOCK_ID");
        public final static Property FDCSPID = new Property(13, String.class, "FDCSPID", false, "FDCSPID");
        public final static Property FBatchNo = new Property(14, String.class, "FBatchNo", false, "FBATCH_NO");
        public final static Property FDCSTOCK_ID = new Property(15, String.class, "FDCSTOCK_ID", false, "FDCSTOCK__ID");
    }


    public PushDownSubDao(DaoConfig config) {
        super(config);
    }
    
    public PushDownSubDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PUSH_DOWN_SUB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FNAME\" TEXT," + // 1: FName
                "\"FNUMBER\" TEXT," + // 2: FNumber
                "\"FMODEL\" TEXT," + // 3: FModel
                "\"FBILL_NO\" TEXT," + // 4: FBillNo
                "\"FINTER_ID\" TEXT," + // 5: FInterID
                "\"FENTRY_ID\" TEXT," + // 6: FEntryID
                "\"FITEM_ID\" TEXT," + // 7: FItemID
                "\"FUNIT_ID\" TEXT," + // 8: FUnitID
                "\"FAUX_QTY\" TEXT," + // 9: FAuxQty
                "\"FAUX_PRICE\" TEXT," + // 10: FAuxPrice
                "\"FQTYING\" TEXT," + // 11: FQtying
                "\"FDCSTOCK_ID\" TEXT," + // 12: FDCStockID
                "\"FDCSPID\" TEXT," + // 13: FDCSPID
                "\"FBATCH_NO\" TEXT," + // 14: FBatchNo
                "\"FDCSTOCK__ID\" TEXT);"); // 15: FDCSTOCK_ID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PUSH_DOWN_SUB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PushDownSub entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(2, FName);
        }
 
        String FNumber = entity.getFNumber();
        if (FNumber != null) {
            stmt.bindString(3, FNumber);
        }
 
        String FModel = entity.getFModel();
        if (FModel != null) {
            stmt.bindString(4, FModel);
        }
 
        String FBillNo = entity.getFBillNo();
        if (FBillNo != null) {
            stmt.bindString(5, FBillNo);
        }
 
        String FInterID = entity.getFInterID();
        if (FInterID != null) {
            stmt.bindString(6, FInterID);
        }
 
        String FEntryID = entity.getFEntryID();
        if (FEntryID != null) {
            stmt.bindString(7, FEntryID);
        }
 
        String FItemID = entity.getFItemID();
        if (FItemID != null) {
            stmt.bindString(8, FItemID);
        }
 
        String FUnitID = entity.getFUnitID();
        if (FUnitID != null) {
            stmt.bindString(9, FUnitID);
        }
 
        String FAuxQty = entity.getFAuxQty();
        if (FAuxQty != null) {
            stmt.bindString(10, FAuxQty);
        }
 
        String FAuxPrice = entity.getFAuxPrice();
        if (FAuxPrice != null) {
            stmt.bindString(11, FAuxPrice);
        }
 
        String FQtying = entity.getFQtying();
        if (FQtying != null) {
            stmt.bindString(12, FQtying);
        }
 
        String FDCStockID = entity.getFDCStockID();
        if (FDCStockID != null) {
            stmt.bindString(13, FDCStockID);
        }
 
        String FDCSPID = entity.getFDCSPID();
        if (FDCSPID != null) {
            stmt.bindString(14, FDCSPID);
        }
 
        String FBatchNo = entity.getFBatchNo();
        if (FBatchNo != null) {
            stmt.bindString(15, FBatchNo);
        }
 
        String FDCSTOCK_ID = entity.getFDCSTOCK_ID();
        if (FDCSTOCK_ID != null) {
            stmt.bindString(16, FDCSTOCK_ID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PushDownSub entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(2, FName);
        }
 
        String FNumber = entity.getFNumber();
        if (FNumber != null) {
            stmt.bindString(3, FNumber);
        }
 
        String FModel = entity.getFModel();
        if (FModel != null) {
            stmt.bindString(4, FModel);
        }
 
        String FBillNo = entity.getFBillNo();
        if (FBillNo != null) {
            stmt.bindString(5, FBillNo);
        }
 
        String FInterID = entity.getFInterID();
        if (FInterID != null) {
            stmt.bindString(6, FInterID);
        }
 
        String FEntryID = entity.getFEntryID();
        if (FEntryID != null) {
            stmt.bindString(7, FEntryID);
        }
 
        String FItemID = entity.getFItemID();
        if (FItemID != null) {
            stmt.bindString(8, FItemID);
        }
 
        String FUnitID = entity.getFUnitID();
        if (FUnitID != null) {
            stmt.bindString(9, FUnitID);
        }
 
        String FAuxQty = entity.getFAuxQty();
        if (FAuxQty != null) {
            stmt.bindString(10, FAuxQty);
        }
 
        String FAuxPrice = entity.getFAuxPrice();
        if (FAuxPrice != null) {
            stmt.bindString(11, FAuxPrice);
        }
 
        String FQtying = entity.getFQtying();
        if (FQtying != null) {
            stmt.bindString(12, FQtying);
        }
 
        String FDCStockID = entity.getFDCStockID();
        if (FDCStockID != null) {
            stmt.bindString(13, FDCStockID);
        }
 
        String FDCSPID = entity.getFDCSPID();
        if (FDCSPID != null) {
            stmt.bindString(14, FDCSPID);
        }
 
        String FBatchNo = entity.getFBatchNo();
        if (FBatchNo != null) {
            stmt.bindString(15, FBatchNo);
        }
 
        String FDCSTOCK_ID = entity.getFDCSTOCK_ID();
        if (FDCSTOCK_ID != null) {
            stmt.bindString(16, FDCSTOCK_ID);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PushDownSub readEntity(Cursor cursor, int offset) {
        PushDownSub entity = new PushDownSub( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // FName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FNumber
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // FModel
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // FBillNo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // FInterID
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // FEntryID
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // FItemID
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // FUnitID
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // FAuxQty
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // FAuxPrice
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // FQtying
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // FDCStockID
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // FDCSPID
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // FBatchNo
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // FDCSTOCK_ID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PushDownSub entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFNumber(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFModel(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFBillNo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFInterID(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFEntryID(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFItemID(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFUnitID(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFAuxQty(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFAuxPrice(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFQtying(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFDCStockID(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFDCSPID(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFBatchNo(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFDCSTOCK_ID(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PushDownSub entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PushDownSub entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PushDownSub entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
