package com.bitocean.dtm.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {
    public static final class IN_CASH_DATA_DB implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.bitocean.dtm.db.provider/in_cash_data");
        public static final String _ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TRANS_TYPE = "trans_type";
        public static final String UUID = "uuid";
        public static final String TIME = "time";
        public static final String SOURCE = "source";
        public static final String CURRENCY_TYPE = "currency_type";
        public static final String CURRENCY_NUM = "currency_num";

        public static final String CREATE_TABLE = "CREATE TABLE in_cash_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", user_id TEXT, trans_type TEXT, uuid TEXT, time TEXT, source TEXT, currency_type TEXT, currency_num TEXT);";
    }

    public static final class OUT_CASH_DATA_DB implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.bitocean.dtm.db.provider/out_cash_data");
        public static final String _ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TRANS_TYPE = "trans_type";
        public static final String UUID = "uuid";
        public static final String TIME = "time";
        public static final String SOURCE = "source";
        public static final String CURRENCY_TYPE = "currency_type";
        public static final String CURRENCY_NUM = "currency_num";

        public static final String CREATE_TABLE = "CREATE TABLE out_cash_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", user_id TEXT, trans_type TEXT, uuid TEXT, time TEXT, source TEXT, currency_type TEXT, currency_num TEXT);";
    }

    public static final class HARDWARE_DATA_DB implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.bitocean.dtm.db.provider/hardware_data");
        public static final String _ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String HARDWARE = "hardware";
        public static final String UUID = "uuid";
        public static final String TIME = "time";
        public static final String QUESTION = "question";

        public static final String CREATE_TABLE = "CREATE TABLE hardware_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", user_id TEXT, hardware TEXT, uuid TEXT, time TEXT, question TEXT);";
    }

    public static final class BUY_TRAN_DATA_DB implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.bitocean.dtm.db.provider/buy_tran_data");
        public static final String _ID = "_id";
        public static final String TRANS_ID = "trans_id";
        public static final String USER_ID = "user_id";
        public static final String TRANS_TYPE = "trans_type";
        public static final String UUID = "uuid";
        public static final String TIME = "time";
        public static final String RESULT = "result";
        public static final String REASON = "reason";
        public static final String CURRENCY_TYPE = "currency_type";
        public static final String CURRENCY_NUM = "currency_num";
        public static final String BIT_TYPE = "bit_type";
        public static final String BIT_NUM = "bit_num";

        public static final String CREATE_TABLE = "CREATE TABLE buy_tran_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", trans_id TEXT, user_id TEXT, trans_type TEXT, uuid TEXT, time TEXT, result TEXT, reason TEXT, currency_type TEXT, currency_num TEXT, bit_type TEXT, bit_num TEXT);";
    }

    public static final class SELL_TRAN_DATA_DB implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.bitocean.dtm.db.provider/sell_tran_data");
        public static final String _ID = "_id";
        public static final String TRANS_ID = "trans_id";
        public static final String USER_ID = "user_id";
        public static final String TRANS_TYPE = "trans_type";
        public static final String UUID = "uuid";
        public static final String TIME = "time";
        public static final String RESULT = "result";
        public static final String REASON = "reason";
        public static final String CURRENCY_TYPE = "currency_type";
        public static final String CURRENCY_NUM = "currency_num";
        public static final String BIT_TYPE = "bit_type";
        public static final String BIT_NUM = "bit_num";

        public static final String CREATE_TABLE = "CREATE TABLE sell_tran_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", trans_id TEXT, user_id TEXT, trans_type TEXT, uuid TEXT, time TEXT, result TEXT, reason TEXT, currency_type TEXT, currency_num TEXT, bit_type TEXT, bit_num TEXT);";
    }
}
