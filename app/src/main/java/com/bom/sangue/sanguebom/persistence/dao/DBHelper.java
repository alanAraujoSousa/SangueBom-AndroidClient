package com.bom.sangue.sanguebom.persistence.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 25/11/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "sanguebomdb";
    private static  final int  DATABASE_VERSION = 7;
    private static DBHelper instance;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if(instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.SCRIPT_CREATE_TABLE);
        db.execSQL(PatientDAO.SCRIPT_CREATE_TABLE);
        Log.i("DATABASE", "Deploying database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DATABASE", "Updating tables.");
        db.execSQL(UserDAO.SCRIPT_DELETE_TABLE);
        db.execSQL(PatientDAO.SCRIPT_DELETE_TABLE);
        this.onCreate(db);
    }
}








