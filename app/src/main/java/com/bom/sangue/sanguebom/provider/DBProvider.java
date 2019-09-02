package com.bom.sangue.sanguebom.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 11/11/15.
 */
public class DBProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.bom.sangue.sanguebom.provider.DBProvider");

    public static final String AUTHORITY =
            "com.bom.sangue.sanguebom.provider.DBProvider";

    private static  final String DATABASE_NAME = "sanguebom.db";

    private static  final int  DATABASE_VERSION = 1;

    private static final  String USER_TABLE = "user";

    private static final String DONATION_TABLE = "donation";

    private  static final int USERS = 1;

    public static final String TAG = "DBProvider";

    private DBHelper mHelper;

    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, USER_TABLE, USERS);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (mMatcher.match(uri)) {
            case USERS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long rowId = db.insert(USER_TABLE, null, values);
                if (rowId > 0) {
                    Uri userUri = ContentUris.withAppendedId(
                            User.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(
                            userUri, null);
                    return userUri;
                }
            default:
                throw new IllegalArgumentException(
                        "URI desconhecida " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public static final class  User implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + DBProvider.AUTHORITY + "/" + USER_TABLE);

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + DBProvider.AUTHORITY;

        public static final String USER_ID = "_id";

        public static Map<String, String> USER_COLUMNS = new HashMap<String, String>();

        static {
            USER_COLUMNS.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
            USER_COLUMNS.put("login", "LONGTEXT");
            USER_COLUMNS.put("token", "LONGTEXT");
        }
    }

    public static class Donation implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + DBProvider.AUTHORITY + "/" + DONATION_TABLE);

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + DBProvider.AUTHORITY;

        public static final String DONATION_ID = "_id";

        public static Map<String, String> DONATION_COLUMNS = new HashMap<String, String>();

        static {
            DONATION_COLUMNS.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
            DONATION_COLUMNS.put("donationDate", "DATETIME");
        }
    }


    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String userColumns = "";
            String donationColumns = "";
            for (Map.Entry<String, String> entry : User.USER_COLUMNS.entrySet()) {
                userColumns += ", " + entry.getKey() + " " + entry.getValue();
            }
            for (Map.Entry<String, String> entry : Donation.DONATION_COLUMNS.entrySet()) {
                donationColumns += ", " + entry.getKey() + " " + entry.getValue();
            }

            db.execSQL(
                    "CREATE TABLE " + USER_TABLE + " (" +
                            userColumns + ");" +
                            "CREATE TABLE " + DONATION_TABLE + " (" +
                            donationColumns + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            // Como ainda estamos na primeira versão do DB,
            // não precisamos nos preocupar com o update agora.
        }
    }
}
