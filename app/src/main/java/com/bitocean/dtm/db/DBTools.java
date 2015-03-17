package com.bitocean.dtm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.bitocean.dtm.BitOceanATMApp;
import com.bitocean.dtm.db.DataBase.IN_CASH_DATA_DB;
import com.bitocean.dtm.db.DataBase.OUT_CASH_DATA_DB;
import com.bitocean.dtm.db.DataBase.HARDWARE_DATA_DB;
import com.bitocean.dtm.db.DataBase.BUY_TRAN_DATA_DB;
import com.bitocean.dtm.db.DataBase.SELL_TRAN_DATA_DB;

public class DBTools {
    private static DBTools mInstance;
    private static Context mContext;

    private DBTools(Context context) {
        mContext = context;
    }

    public static DBTools getInstance() {
        synchronized (DBTools.class) {
            if (mInstance == null) {
                mInstance = new DBTools(BitOceanATMApp.getContext());
            }
            return mInstance;
        }
    }

    public void closeDB() {
        DBContentProvider.closeDB();
    }

    public Cursor getAllInCashData() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    IN_CASH_DATA_DB.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getAllOutCashData() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    OUT_CASH_DATA_DB.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getAllHardwareData() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    HARDWARE_DATA_DB.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getAllBuyTranData() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    BUY_TRAN_DATA_DB.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getAllSellTranData() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    SELL_TRAN_DATA_DB.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public void insertInCashData(String user_id, String trans_type,
                                 String uuid, String time, String source, String currency_type, String currency_num) {
        ContentValues value = new ContentValues();
        value.put("user_id", toValidRs(user_id));
        value.put("trans_type", toValidRs(trans_type));
        value.put("uuid", toValidRs(uuid));
        value.put("time", toValidRs(time));
        value.put("source", toValidRs(source));
        value.put("currency_type", toValidRs(currency_type));
        value.put("currency_num", toValidRs(currency_num));

        mContext.getContentResolver().insert(IN_CASH_DATA_DB.CONTENT_URI, value);
    }

    public void insertOutCashData(String user_id, String trans_type,
                                  String uuid, String time, String source, String currency_type, String currency_num) {
        ContentValues value = new ContentValues();
        value.put("user_id", toValidRs(user_id));
        value.put("trans_type", toValidRs(trans_type));
        value.put("uuid", toValidRs(uuid));
        value.put("time", toValidRs(time));
        value.put("source", toValidRs(source));
        value.put("currency_type", toValidRs(currency_type));
        value.put("currency_num", toValidRs(currency_num));
        mContext.getContentResolver().insert(OUT_CASH_DATA_DB.CONTENT_URI, value);
    }

    public void insertHardwareData(String user_id, String hardware,
                                   String uuid, String time, String question) {
        ContentValues value = new ContentValues();
        value.put("user_id", toValidRs(user_id));
        value.put("hardware", toValidRs(hardware));
        value.put("uuid", toValidRs(uuid));
        value.put("time", toValidRs(time));
        value.put("question", toValidRs(question));
        mContext.getContentResolver().insert(HARDWARE_DATA_DB.CONTENT_URI, value);
    }

    public void insertBuyTranData(String trans_id, String user_id, String trans_type, String result, String reason,
                                  String uuid, String time, String currency_type, String currency_num, String bit_type, String bit_num) {
        ContentValues value = new ContentValues();
        value.put("trans_id", toValidRs(trans_id));
        value.put("user_id", toValidRs(user_id));
        value.put("trans_type", toValidRs(trans_type));
        value.put("uuid", toValidRs(uuid));
        value.put("time", toValidRs(time));
        value.put("result", toValidRs(result));
        value.put("reason", toValidRs(reason));
        value.put("currency_type", toValidRs(currency_type));
        value.put("currency_num", toValidRs(currency_num));
        value.put("bit_type", toValidRs(bit_type));
        value.put("bit_num", toValidRs(bit_num));
        mContext.getContentResolver().insert(BUY_TRAN_DATA_DB.CONTENT_URI, value);
    }

    public void insertSellTranData(String trans_id, String user_id, String trans_type, String result, String reason,
                                   String uuid, String time, String currency_type, String currency_num, String bit_type, String bit_num) {
        ContentValues value = new ContentValues();
        value.put("trans_id", toValidRs(trans_id));
        value.put("user_id", toValidRs(user_id));
        value.put("trans_type", toValidRs(trans_type));
        value.put("uuid", toValidRs(uuid));
        value.put("time", toValidRs(time));
        value.put("result", toValidRs(result));
        value.put("reason", toValidRs(reason));
        value.put("currency_type", toValidRs(currency_type));
        value.put("currency_num", toValidRs(currency_num));
        value.put("bit_type", toValidRs(bit_type));
        value.put("bit_num", toValidRs(bit_num));
        mContext.getContentResolver().insert(SELL_TRAN_DATA_DB.CONTENT_URI, value);
    }

    public static String toValidRs(String obj) {
        if (obj == null)
            return "@*@";
        else if (obj.indexOf("'") != -1) {
            return obj.replace("'", "*@*");
        } else
            return obj;
    }

    public static String getUnvalidFormRs(String obj) {
        if (obj == null)
            return null;
        else if (obj.equals("@*@"))
            return null;
        else if (obj.indexOf("*@*") != -1) {
            return obj.replace("*@*", "'");
        } else
            return obj;
    }
}
