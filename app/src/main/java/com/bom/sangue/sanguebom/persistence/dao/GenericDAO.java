package com.bom.sangue.sanguebom.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.bom.sangue.sanguebom.persistence.bean.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 25/11/15.
 */
public abstract class GenericDAO<T extends Persistent> {

    protected SQLiteDatabase dataBase = null;

    public GenericDAO(Context context) {
        DBHelper persistenceHelper = DBHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }

    public abstract String getPrimaryKeyName();
    public abstract String getTableName();

    public abstract ContentValues entityToContentValues(T entity);
    public abstract T contentValuesToEntity(ContentValues contentValues);


    public void save(T entity) {
        ContentValues values = entityToContentValues(entity);
        dataBase.insert(getTableName(), null, values);
    }

    public void delete(T entity) {

        String[] valuesToReplace = {
                String.valueOf(entity.getId())
        };

        dataBase.delete(getTableName(), getPrimaryKeyName() + " =  ?", valuesToReplace);
    }

    public void deleteAll() {
        dataBase.execSQL("DELETE FROM " + getTableName());
    }

    public void update(T entity) {
        ContentValues values = entityToContentValues(entity);

        String[] valuesToReplace = {
                String.valueOf(entity.getId())
        };

        dataBase.update(getTableName(), values, getPrimaryKeyName() + " = ?", valuesToReplace);
    }

    public List<T> listAll() {
        String queryReturnAll = "SELECT * FROM " + getTableName();
        List<T> result = findByQuery(queryReturnAll);
        return result;
    }

    public T findById(int id) {
        String queryOne = "SELECT * FROM " + getTableName() + " where " + getPrimaryKeyName() + " = " + id;
        List<T> result = findByQuery(queryOne);
        if(result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public List<T> findByQuery(String query) {

        Cursor cursor = dataBase.rawQuery(query, null);

        List<T> result = new ArrayList<T>();
        if (cursor.moveToFirst()) {
            do {
                ContentValues contentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                T entity = contentValuesToEntity(contentValues);
                result.add(entity);
            } while (cursor.moveToNext());
        }
        return result;

    }

    public void closeConnection() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close();
    }
}
