package com.bitocean.dtm.db;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.bitocean.dtm.db.DataBase.IN_CASH_DATA_DB;
import com.bitocean.dtm.db.DataBase.OUT_CASH_DATA_DB;
import com.bitocean.dtm.db.DataBase.HARDWARE_DATA_DB;
import com.bitocean.dtm.db.DataBase.BUY_TRAN_DATA_DB;
import com.bitocean.dtm.db.DataBase.SELL_TRAN_DATA_DB;

public class DBContentProvider extends ContentProvider {
    private static DBProviderHelper dbHelper;

    private static final String URI_AUTHORITY = "com.bitocean.dtm.db.provider";
    private static final String DATABASE_NAME = "dtm_data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TB_IN_CASH_DATA = "in_cash_data";
    private static final String TB_OUT_CASH_DATA = "out_cash_data";
    private static final String TB_HARDWARE_DATA = "hardware_data";
    private static final String TB_BUY_TRAN_DATA = "buy_tran_data";
    private static final String TB_SELL_TRAN_DATA = "sell_tran_data";


    private static final int IN_CASH_DATA = 1;
    private static final int IN_CASH_DATA_ID = 2;
    private static final int OUT_CASH_DATA = 3;
    private static final int OUT_CASH_DATA_ID = 4;
    private static final int HARDWARE_DATA = 5;
    private static final int HARDWARE_DATA_ID = 6;
    private static final int BUY_TRAN_DATA = 7;
    private static final int BUY_TRAN_DATA_ID = 8;
    private static final int SELL_TRAN_DATA = 9;
    private static final int SELL_TRAN_DATA_ID = 10;

    private static HashMap<String, String> inCashDataMap;
    private static HashMap<String, String> outCashDataMap;
    private static HashMap<String, String> hardwareDataMap;
    private static HashMap<String, String> buyTranDataMap;
    private static HashMap<String, String> sellTranDataMap;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(URI_AUTHORITY, "in_cash_data", IN_CASH_DATA);
        sUriMatcher.addURI(URI_AUTHORITY, "in_cash_data/#", IN_CASH_DATA_ID);
        sUriMatcher.addURI(URI_AUTHORITY, "out_cash_data", OUT_CASH_DATA);
        sUriMatcher.addURI(URI_AUTHORITY, "out_cash_data/#", OUT_CASH_DATA_ID);
        sUriMatcher.addURI(URI_AUTHORITY, "hardware_data", HARDWARE_DATA);
        sUriMatcher.addURI(URI_AUTHORITY, "hardware_data/#", HARDWARE_DATA_ID);
        sUriMatcher.addURI(URI_AUTHORITY, "buy_tran_data", BUY_TRAN_DATA);
        sUriMatcher.addURI(URI_AUTHORITY, "buy_tran_data/#", BUY_TRAN_DATA_ID);
        sUriMatcher.addURI(URI_AUTHORITY, "sell_tran_data", SELL_TRAN_DATA);
        sUriMatcher.addURI(URI_AUTHORITY, "sell_tran_data/#", SELL_TRAN_DATA_ID);


        outCashDataMap = new HashMap<String, String>();
        outCashDataMap.put(OUT_CASH_DATA_DB._ID, OUT_CASH_DATA_DB._ID);
        outCashDataMap.put(OUT_CASH_DATA_DB.USER_ID, OUT_CASH_DATA_DB.USER_ID);
        outCashDataMap.put(OUT_CASH_DATA_DB.TRANS_TYPE, OUT_CASH_DATA_DB.TRANS_TYPE);
        outCashDataMap.put(OUT_CASH_DATA_DB.UUID, OUT_CASH_DATA_DB.UUID);
        outCashDataMap.put(OUT_CASH_DATA_DB.TIME, OUT_CASH_DATA_DB.TIME);
        outCashDataMap.put(OUT_CASH_DATA_DB.SOURCE, OUT_CASH_DATA_DB.SOURCE);
        outCashDataMap.put(OUT_CASH_DATA_DB.CURRENCY_TYPE, OUT_CASH_DATA_DB.CURRENCY_TYPE);
        outCashDataMap.put(OUT_CASH_DATA_DB.CURRENCY_NUM, OUT_CASH_DATA_DB.CURRENCY_NUM);

        inCashDataMap = new HashMap<String, String>();
        inCashDataMap.put(IN_CASH_DATA_DB._ID, IN_CASH_DATA_DB._ID);
        inCashDataMap.put(IN_CASH_DATA_DB.USER_ID, IN_CASH_DATA_DB.USER_ID);
        inCashDataMap.put(IN_CASH_DATA_DB.TRANS_TYPE, IN_CASH_DATA_DB.TRANS_TYPE);
        inCashDataMap.put(IN_CASH_DATA_DB.UUID, IN_CASH_DATA_DB.UUID);
        inCashDataMap.put(IN_CASH_DATA_DB.TIME, IN_CASH_DATA_DB.TIME);
        inCashDataMap.put(IN_CASH_DATA_DB.SOURCE, IN_CASH_DATA_DB.SOURCE);
        inCashDataMap.put(IN_CASH_DATA_DB.CURRENCY_TYPE, IN_CASH_DATA_DB.CURRENCY_TYPE);
        inCashDataMap.put(IN_CASH_DATA_DB.CURRENCY_NUM, IN_CASH_DATA_DB.CURRENCY_NUM);

        hardwareDataMap = new HashMap<String, String>();
        hardwareDataMap.put(HARDWARE_DATA_DB._ID, HARDWARE_DATA_DB._ID);
        hardwareDataMap.put(HARDWARE_DATA_DB.USER_ID, HARDWARE_DATA_DB.USER_ID);
        hardwareDataMap.put(HARDWARE_DATA_DB.HARDWARE, HARDWARE_DATA_DB.HARDWARE);
        hardwareDataMap.put(HARDWARE_DATA_DB.UUID, HARDWARE_DATA_DB.UUID);
        hardwareDataMap.put(HARDWARE_DATA_DB.TIME, HARDWARE_DATA_DB.TIME);
        hardwareDataMap.put(HARDWARE_DATA_DB.QUESTION, HARDWARE_DATA_DB.QUESTION);

        buyTranDataMap = new HashMap<String, String>();
        buyTranDataMap.put(BUY_TRAN_DATA_DB._ID, BUY_TRAN_DATA_DB._ID);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.TRANS_ID, BUY_TRAN_DATA_DB.TRANS_ID);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.USER_ID, BUY_TRAN_DATA_DB.USER_ID);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.TRANS_TYPE, BUY_TRAN_DATA_DB.TRANS_TYPE);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.UUID, BUY_TRAN_DATA_DB.UUID);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.TIME, BUY_TRAN_DATA_DB.TIME);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.RESULT, BUY_TRAN_DATA_DB.RESULT);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.REASON, BUY_TRAN_DATA_DB.REASON);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.CURRENCY_TYPE, BUY_TRAN_DATA_DB.CURRENCY_TYPE);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.CURRENCY_NUM, BUY_TRAN_DATA_DB.CURRENCY_NUM);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.BIT_TYPE, BUY_TRAN_DATA_DB.BIT_TYPE);
        buyTranDataMap.put(BUY_TRAN_DATA_DB.BIT_NUM, BUY_TRAN_DATA_DB.BIT_NUM);

        sellTranDataMap = new HashMap<String, String>();
        sellTranDataMap.put(SELL_TRAN_DATA_DB._ID, SELL_TRAN_DATA_DB._ID);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.TRANS_ID, SELL_TRAN_DATA_DB.TRANS_ID);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.USER_ID, SELL_TRAN_DATA_DB.USER_ID);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.TRANS_TYPE, SELL_TRAN_DATA_DB.TRANS_TYPE);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.UUID, SELL_TRAN_DATA_DB.UUID);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.TIME, SELL_TRAN_DATA_DB.TIME);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.RESULT, SELL_TRAN_DATA_DB.RESULT);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.REASON, SELL_TRAN_DATA_DB.REASON);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.CURRENCY_TYPE, SELL_TRAN_DATA_DB.CURRENCY_TYPE);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.CURRENCY_NUM, SELL_TRAN_DATA_DB.CURRENCY_NUM);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.BIT_TYPE, SELL_TRAN_DATA_DB.BIT_TYPE);
        sellTranDataMap.put(SELL_TRAN_DATA_DB.BIT_NUM, SELL_TRAN_DATA_DB.BIT_NUM);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        dbHelper = new DBProviderHelper(getContext(), DATABASE_NAME, null,
                DATABASE_VERSION);
        return true;
    }

    public static void closeDB() {
        dbHelper.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.ContentProvider#query(android.net.Uri,
     * java.lang.String[], java.lang.String, java.lang.String[],
     * java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case IN_CASH_DATA:
                qb.setTables(TB_IN_CASH_DATA);
                qb.setProjectionMap(inCashDataMap);
                break;
            case IN_CASH_DATA_ID:
                qb.setTables(TB_IN_CASH_DATA);
                qb.setProjectionMap(inCashDataMap);
                qb.appendWhere(IN_CASH_DATA_DB._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case OUT_CASH_DATA:
                qb.setTables(TB_OUT_CASH_DATA);
                qb.setProjectionMap(outCashDataMap);
                break;
            case OUT_CASH_DATA_ID:
                qb.setTables(TB_OUT_CASH_DATA);
                qb.setProjectionMap(outCashDataMap);
                qb.appendWhere(OUT_CASH_DATA_DB._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case HARDWARE_DATA:
                qb.setTables(TB_HARDWARE_DATA);
                qb.setProjectionMap(hardwareDataMap);
                break;
            case HARDWARE_DATA_ID:
                qb.setTables(TB_HARDWARE_DATA);
                qb.setProjectionMap(hardwareDataMap);
                qb.appendWhere(HARDWARE_DATA_DB._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case BUY_TRAN_DATA:
                qb.setTables(TB_BUY_TRAN_DATA);
                qb.setProjectionMap(buyTranDataMap);
                break;
            case BUY_TRAN_DATA_ID:
                qb.setTables(TB_BUY_TRAN_DATA);
                qb.setProjectionMap(buyTranDataMap);
                qb.appendWhere(BUY_TRAN_DATA_DB._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            case SELL_TRAN_DATA:
                qb.setTables(TB_SELL_TRAN_DATA);
                qb.setProjectionMap(sellTranDataMap);
                break;
            case SELL_TRAN_DATA_ID:
                qb.setTables(TB_SELL_TRAN_DATA);
                qb.setProjectionMap(sellTranDataMap);
                qb.appendWhere(SELL_TRAN_DATA_DB._ID + "="
                        + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.ContentProvider#insert(android.net.Uri,
     * android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues initValues) {
        ContentValues values = null;
        if (initValues == null) {
            return null;
        } else {
            values = new ContentValues(initValues);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri myURI;
        long retval = 0;
        switch (sUriMatcher.match(uri)) {
            case IN_CASH_DATA:
                retval = db.insert(TB_IN_CASH_DATA, IN_CASH_DATA_DB._ID, values);
                myURI = IN_CASH_DATA_DB.CONTENT_URI;
                break;
            case OUT_CASH_DATA:
                retval = db.insert(TB_OUT_CASH_DATA, OUT_CASH_DATA_DB._ID, values);
                myURI = OUT_CASH_DATA_DB.CONTENT_URI;
                break;
            case HARDWARE_DATA:
                retval = db.insert(TB_HARDWARE_DATA, HARDWARE_DATA_DB._ID, values);
                myURI = HARDWARE_DATA_DB.CONTENT_URI;
                break;
            case BUY_TRAN_DATA:
                retval = db.insert(TB_BUY_TRAN_DATA, BUY_TRAN_DATA_DB._ID, values);
                myURI = BUY_TRAN_DATA_DB.CONTENT_URI;
                break;
            case SELL_TRAN_DATA:
                retval = db.insert(TB_SELL_TRAN_DATA, SELL_TRAN_DATA_DB._ID, values);
                myURI = SELL_TRAN_DATA_DB.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (retval > 0) {
            Uri retUri = ContentUris.withAppendedId(myURI, retval);
            retUri.buildUpon().build();
            getContext().getContentResolver().notifyChange(retUri, null);

            return retUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.ContentProvider#update(android.net.Uri,
     * android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int retval = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case IN_CASH_DATA:
                retval = db.update(TB_IN_CASH_DATA, values, selection, selectionArgs);
                break;
            case IN_CASH_DATA_ID:
                retval = db.update(TB_IN_CASH_DATA, values,
                        IN_CASH_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case OUT_CASH_DATA:
                retval = db.update(TB_OUT_CASH_DATA, values, selection, selectionArgs);
                break;
            case OUT_CASH_DATA_ID:
                retval = db.update(TB_OUT_CASH_DATA, values,
                        OUT_CASH_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case HARDWARE_DATA:
                retval = db.update(TB_HARDWARE_DATA, values, selection, selectionArgs);
                break;
            case HARDWARE_DATA_ID:
                retval = db.update(TB_HARDWARE_DATA, values,
                        HARDWARE_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case BUY_TRAN_DATA:
                retval = db.update(TB_BUY_TRAN_DATA, values, selection, selectionArgs);
                break;
            case BUY_TRAN_DATA_ID:
                retval = db.update(TB_BUY_TRAN_DATA, values,
                        BUY_TRAN_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case SELL_TRAN_DATA:
                retval = db.update(TB_SELL_TRAN_DATA, values, selection, selectionArgs);
                break;
            case SELL_TRAN_DATA_ID:
                retval = db.update(TB_SELL_TRAN_DATA, values,
                        SELL_TRAN_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            default:
                break;

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retval;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.ContentProvider#delete(android.net.Uri,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int retval = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case IN_CASH_DATA:
                retval = db.delete(TB_IN_CASH_DATA, selection, selectionArgs);
                break;
            case IN_CASH_DATA_ID:
                retval = db.delete(TB_IN_CASH_DATA,
                        IN_CASH_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case OUT_CASH_DATA:
                retval = db.delete(TB_OUT_CASH_DATA, selection, selectionArgs);
                break;
            case OUT_CASH_DATA_ID:
                retval = db.delete(TB_OUT_CASH_DATA,
                        OUT_CASH_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case HARDWARE_DATA:
                retval = db.delete(TB_HARDWARE_DATA, selection, selectionArgs);
                break;
            case HARDWARE_DATA_ID:
                retval = db.delete(TB_HARDWARE_DATA,
                        HARDWARE_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case BUY_TRAN_DATA:
                retval = db.delete(TB_BUY_TRAN_DATA, selection, selectionArgs);
                break;
            case BUY_TRAN_DATA_ID:
                retval = db.delete(TB_BUY_TRAN_DATA,
                        BUY_TRAN_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            case SELL_TRAN_DATA:
                retval = db.delete(TB_SELL_TRAN_DATA, selection, selectionArgs);
                break;
            case SELL_TRAN_DATA_ID:
                retval = db.delete(TB_SELL_TRAN_DATA,
                        SELL_TRAN_DATA_DB._ID
                                + "="
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retval;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    public SQLiteDatabase getHelper() {
        return dbHelper.getWritableDatabase();
    }

    class DBProviderHelper extends SQLiteOpenHelper {
        /**
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        private static final String TB_IN_CASH_DATA = "in_cash_data";
        private static final String TB_OUT_CASH_DATA = "out_cash_data";
        private static final String TB_HARDWARE_DATA = "hardware_data";
        private static final String TB_BUY_TRAN_DATA = "buy_tran_data";
        private static final String TB_SELL_TRAN_DATA = "sell_tran_data";

        public DBProviderHelper(Context context, String name,
                                CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
         * .SQLiteDatabase)
         */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IN_CASH_DATA_DB.CREATE_TABLE);
            db.execSQL(OUT_CASH_DATA_DB.CREATE_TABLE);
            db.execSQL(HARDWARE_DATA_DB.CREATE_TABLE);
            db.execSQL(BUY_TRAN_DATA_DB.CREATE_TABLE);
            db.execSQL(SELL_TRAN_DATA_DB.CREATE_TABLE);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
         * .SQLiteDatabase, int, int)
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_IN_CASH_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TB_OUT_CASH_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TB_HARDWARE_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TB_IN_CASH_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TB_BUY_TRAN_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TB_SELL_TRAN_DATA);
            onCreate(db);
        }
    }
}
