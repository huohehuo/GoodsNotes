package com.fangzuo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fangzuo.assist.Dao.UnitBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "UNIT_BEAN".
*/
public class UnitBeanDao extends AbstractDao<UnitBean, Long> {

    public static final String TABLENAME = "UNIT_BEAN";

    /**
     * Properties of entity UnitBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FID = new Property(1, String.class, "FID", false, "FID");
        public final static Property FName = new Property(2, String.class, "FName", false, "FNAME");
        public final static Property FCreateData = new Property(3, String.class, "FCreateData", false, "FCREATE_DATA");
    }


    public UnitBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UnitBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"UNIT_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FID\" TEXT," + // 1: FID
                "\"FNAME\" TEXT," + // 2: FName
                "\"FCREATE_DATA\" TEXT);"); // 3: FCreateData
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"UNIT_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UnitBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FID = entity.getFID();
        if (FID != null) {
            stmt.bindString(2, FID);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(3, FName);
        }
 
        String FCreateData = entity.getFCreateData();
        if (FCreateData != null) {
            stmt.bindString(4, FCreateData);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UnitBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FID = entity.getFID();
        if (FID != null) {
            stmt.bindString(2, FID);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(3, FName);
        }
 
        String FCreateData = entity.getFCreateData();
        if (FCreateData != null) {
            stmt.bindString(4, FCreateData);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UnitBean readEntity(Cursor cursor, int offset) {
        UnitBean entity = new UnitBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // FID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // FCreateData
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UnitBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFCreateData(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UnitBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UnitBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UnitBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
