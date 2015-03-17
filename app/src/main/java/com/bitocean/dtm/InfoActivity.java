package com.bitocean.dtm;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.db.DataBase;
import com.umeng.analytics.MobclickAgent;


/**
 * @author bing.liu
 */
public class InfoActivity extends BaseTimerActivity {
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.isLoopTime = false;
        setContentView(R.layout.activity_info);
        initUI();
    }

    protected void onDestroy() {
        DBTools.getInstance().closeDB();
        AppManager.isLoopTime = true;
        super.onDestroy();
    }

    private void initUI() {
        type = getIntent().getIntExtra("type", 0);

        TextView titleTextView = (TextView) findViewById(R.id.title_text)
                .findViewById(R.id.view_text);

        ListView lv = (ListView) findViewById(R.id.info_list);
        switch (type) {
            case 0: {
                Cursor c = DBTools.getInstance().getAllBuyTranData();
                BuyTranAdapter adapter = new BuyTranAdapter(this,
                        c);
                lv.setAdapter(adapter);
                titleTextView.setText(R.string.buy_tran_btn);
            }
            break;
            case 1: {
                Cursor c = DBTools.getInstance().getAllSellTranData();
                SellTranAdapter adapter = new SellTranAdapter(this,
                        c);
                lv.setAdapter(adapter);
                titleTextView.setText(R.string.sell_tran_btn);
            }
            break;
            case 2: {
                Cursor c = DBTools.getInstance().getAllInCashData();
                InCashAdapter adapter = new InCashAdapter(this,
                        c);
                lv.setAdapter(adapter);
                titleTextView.setText(R.string.incash_btn);
            }
            break;
            case 3: {
                Cursor c = DBTools.getInstance().getAllOutCashData();
                OutCashAdapter adapter = new OutCashAdapter(this,
                        c);
                lv.setAdapter(adapter);
                titleTextView.setText(R.string.outcash_btn);
            }
            break;
            case 4: {
                Cursor c = DBTools.getInstance().getAllHardwareData();
                HardWareAdapter adapter = new HardWareAdapter(this,
                        c);
                lv.setAdapter(adapter);
                titleTextView.setText(R.string.device_btn);
            }
            break;
            default:
                break;
        }
        titleTextView.setText(R.string.key_prompt);

        Button cancelButton = (Button) findViewById(R.id.bottom_button)
                .findViewById(R.id.left_btn);
        cancelButton.setVisibility(View.INVISIBLE);

        Button nextButton = (Button) findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.out);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        TextView phone_text = (TextView) findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }


    public class BuyTranAdapter extends CursorAdapter {
        public BuyTranAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(InfoActivity.this);
            View v = mInflater.inflate(R.layout.view_info_text, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameView = (TextView) view.findViewById(R.id.name_text);
            nameView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.USER_ID))));

            TextView timeView = (TextView) view.findViewById(R.id.time_text);
            timeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.TIME)))
                    + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.RESULT))) + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.REASON))));

            TextView typeView = (TextView) view.findViewById(R.id.type_text);
            typeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.TRANS_TYPE))) + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.TRANS_ID))));

            TextView infoView = (TextView) view.findViewById(R.id.info_text);
            infoView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.CURRENCY_NUM))) + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.CURRENCY_TYPE)))
                    + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.BIT_NUM))) + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.BUY_TRAN_DATA_DB.BIT_TYPE))));
        }
    }

    public class SellTranAdapter extends CursorAdapter {
        public SellTranAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(InfoActivity.this);
            View v = mInflater.inflate(R.layout.view_info_text, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameView = (TextView) view.findViewById(R.id.name_text);
            nameView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.USER_ID))));

            TextView timeView = (TextView) view.findViewById(R.id.time_text);
            timeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.TIME)))
                    + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.RESULT))) + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.REASON))));

            TextView typeView = (TextView) view.findViewById(R.id.type_text);
            typeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.TRANS_TYPE))) + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.TRANS_ID))));

            TextView infoView = (TextView) view.findViewById(R.id.info_text);
            infoView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.CURRENCY_NUM))) + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.CURRENCY_TYPE)))
                    + "     " + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.BIT_NUM))) + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.SELL_TRAN_DATA_DB.BIT_TYPE))));
        }
    }

    public class InCashAdapter extends CursorAdapter {
        public InCashAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(InfoActivity.this);
            View v = mInflater.inflate(R.layout.view_info_text, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameView = (TextView) view.findViewById(R.id.name_text);
            nameView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.IN_CASH_DATA_DB.USER_ID))));

            TextView timeView = (TextView) view.findViewById(R.id.time_text);
            timeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.IN_CASH_DATA_DB.TIME))));

            TextView typeView = (TextView) view.findViewById(R.id.type_text);
            typeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.IN_CASH_DATA_DB.TRANS_TYPE))));

            TextView infoView = (TextView) view.findViewById(R.id.info_text);
            infoView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.IN_CASH_DATA_DB.CURRENCY_NUM))) + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.IN_CASH_DATA_DB.CURRENCY_TYPE))));
        }
    }

    public class OutCashAdapter extends CursorAdapter {
        public OutCashAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(InfoActivity.this);
            View v = mInflater.inflate(R.layout.view_info_text, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameView = (TextView) view.findViewById(R.id.name_text);
            nameView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.OUT_CASH_DATA_DB.USER_ID))));

            TextView timeView = (TextView) view.findViewById(R.id.time_text);
            timeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.OUT_CASH_DATA_DB.TIME))));

            TextView typeView = (TextView) view.findViewById(R.id.type_text);
            typeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.OUT_CASH_DATA_DB.TRANS_TYPE))));

            TextView infoView = (TextView) view.findViewById(R.id.info_text);
            infoView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.OUT_CASH_DATA_DB.CURRENCY_NUM))) + DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.OUT_CASH_DATA_DB.CURRENCY_TYPE))));
        }
    }

    public class HardWareAdapter extends CursorAdapter {

        public HardWareAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(InfoActivity.this);
            View v = mInflater.inflate(R.layout.view_info_text, null);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameView = (TextView) view.findViewById(R.id.name_text);
            nameView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.HARDWARE_DATA_DB.USER_ID))));

            TextView timeView = (TextView) view.findViewById(R.id.time_text);
            timeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.HARDWARE_DATA_DB.TIME))));

            TextView typeView = (TextView) view.findViewById(R.id.type_text);
            typeView.setText(DBTools.getUnvalidFormRs(cursor.getString(cursor.getColumnIndex(DataBase.HARDWARE_DATA_DB.HARDWARE))));

            TextView infoView = (TextView) view.findViewById(R.id.info_text);
            infoView.setText(cursor.getString(cursor.getColumnIndex(DataBase.HARDWARE_DATA_DB.QUESTION)));
        }
    }
}

