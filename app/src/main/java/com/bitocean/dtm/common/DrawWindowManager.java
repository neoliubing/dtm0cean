/**
 *
 */
package com.bitocean.dtm.common;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.struct.RateStruct;
import com.bitocean.dtm.util.Util;

/**
 * @author bing.liu
 */
public class    DrawWindowManager {
    private Context mContext;
    private WindowManager mWManager;
    private ImageView wifiImageView;
    private TextView timerTextView;
    private LinearLayout buyRateLayout, sellRateLayout;
    private TextView buyActionText, buyBitValueText, buyBitUnitText,
            buyMoneyValueText, buyMoneyUnitTextView, sellActionText,
            sellBitValueText, sellBitUnitText, sellMoneyValueText,
            sellMoneyUnitTextView;

    public DrawWindowManager(Context context) {
        mContext = context;
        mWManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public void updateNetworkStatus(NetworkInfo info) {
        if (info != null && info.isAvailable()) {
            AppManager.isNetEnable = true;
            showNetWorkStatus(info.isAvailable());
        } else {
            AppManager.isNetEnable = false;
            if (info != null)
                showNetWorkStatus(info.isAvailable());
            else
                showNetWorkStatus(false);
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
        }
    }

    public void showNetWorkStatus(boolean isAvailable) {
        if (wifiImageView != null) {
            if (isAvailable)
                wifiImageView.setImageResource(R.drawable.stat_sys_wifi_signal_4_fully);
            else
                wifiImageView.setImageResource(R.drawable.stat_sys_wifi_signal_null);
            wifiImageView.invalidate();
        } else {
            wifiImageView = new ImageView(mContext);
            if (isAvailable)
                wifiImageView.setImageResource(R.drawable.stat_sys_wifi_signal_4_fully);
            else
                wifiImageView.setImageResource(R.drawable.stat_sys_wifi_signal_null);
            wifiImageView.invalidate();
            LayoutParams wmParams = new LayoutParams();
            wmParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            wmParams.type = LayoutParams.TYPE_PHONE;
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
            wmParams.x = 10;//Util.dip2px(mContext, 5);
            wmParams.y = 10;//Util.dip2px(mContext, 5);
            wmParams.width = 80;//Util.dip2px(mContext, 30);
            wmParams.height = 80;//Util.dip2px(mContext, 30);
            mWManager.addView(wifiImageView, wmParams);
        }
    }

    public void removeNetWorkStatus() {
        if (mWManager != null && wifiImageView != null)
            mWManager.removeView(wifiImageView);
    }

    public void showTimer(boolean isAvailable) {
        if (timerTextView != null) {
            if (isAvailable) {
                timerTextView.setVisibility(View.VISIBLE);
                timerTextView.setText(AppManager.loopTimer + "");
            } else {
                timerTextView.setVisibility(View.INVISIBLE);
            }
            timerTextView.invalidate();
        } else {
            timerTextView = new TextView(mContext);
            if (isAvailable) {
                timerTextView.setVisibility(View.VISIBLE);
                timerTextView.setText(AppManager.loopTimer + "");
            } else {
                timerTextView.setVisibility(View.INVISIBLE);
            }
            timerTextView.setTextColor(Color.WHITE);
            timerTextView.setGravity(Gravity.CENTER);
            timerTextView.invalidate();
            timerTextView.setTextSize(60);
            LayoutParams wmParams = new LayoutParams();
            wmParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            wmParams.type = LayoutParams.TYPE_PHONE;
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
            wmParams.x = 100;//Util.dip2px(mContext, 20);
            wmParams.y = 10;//Util.dip2px(mContext, 20);
            wmParams.width = 120;//Util.dip2px(mContext, 10);
            wmParams.height = 80;//Util.dip2px(mContext, 10);
            mWManager.addView(timerTextView, wmParams);
        }
    }

    public void removeTimer() {
        if (mWManager != null && timerTextView != null)
            mWManager.removeView(timerTextView);
    }

    public void showBuyRate() {
        if (buyRateLayout != null) {
            buyActionText.setText(R.string.buy);
            buyBitValueText.setText("1");
            buyBitUnitText.setText(R.string.unit_bit);
            for (RateStruct rate : AppManager.typeRateStructs.rateStructs) {
                if ("btc".equals(rate.bit_type)) {
                    buyMoneyValueText.setText(rate.currency_buy + "");
                }
            }
            buyMoneyUnitTextView.setText(AppManager.DTM_CURRENCY);
            buyRateLayout.invalidate();
        } else {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            buyRateLayout = (LinearLayout) mInflater.inflate(R.layout.view_rate_text, null);
            buyRateLayout.setBackgroundResource(R.drawable.rate_buy);
            buyRateLayout.setGravity(Gravity.CENTER);

            buyActionText = (TextView) buyRateLayout.findViewById(R.id.action_text);
            buyBitValueText = (TextView) buyRateLayout.findViewById(R.id.bit_value_text);
            buyBitUnitText = (TextView) buyRateLayout.findViewById(R.id.bit_unit_text);
            buyMoneyValueText = (TextView) buyRateLayout.findViewById(R.id.money_value_text);
            buyMoneyUnitTextView = (TextView) buyRateLayout.findViewById(R.id.money_unit_text);
            buyActionText.setTextColor(Color.rgb(255, 165, 0));
            buyBitValueText.setTextColor(Color.rgb(255, 165, 0));
            buyBitUnitText.setTextColor(Color.rgb(255, 165, 0));
            buyMoneyValueText.setTextColor(Color.rgb(255, 165, 0));
            buyMoneyUnitTextView.setTextColor(Color.rgb(255, 165, 0));
            TextView equal_text = (TextView)buyRateLayout.findViewById(R.id.equal_text);
            equal_text.setTextColor(Color.rgb(255, 165, 0));

            buyActionText.setText(R.string.buy);
            buyBitValueText.setText("1");
            buyBitUnitText.setText(R.string.unit_bit);
            for (RateStruct rate : AppManager.typeRateStructs.rateStructs) {
                if ("btc".equals(rate.bit_type)) {
                    buyMoneyValueText.setText(rate.bit_buy + "");
                }
            }
            buyMoneyUnitTextView.setText(AppManager.DTM_CURRENCY);


            LayoutParams wmParams = new LayoutParams();
            wmParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            wmParams.type = LayoutParams.TYPE_PHONE;
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
            wmParams.x = 540;//Util.dip2px(mContext, 20);
            wmParams.y = 20;//Util.dip2px(mContext, 20);
            wmParams.width = 300;//Util.dip2px(mContext, 10);
            wmParams.height = 60;//Util.dip2px(mContext, 10);
            mWManager.addView(buyRateLayout, wmParams);
        }
    }

    public void removeBuyRate() {
        if (mWManager != null && buyRateLayout != null)
            mWManager.removeView(buyRateLayout);
    }

    public void showSellRate() {
        if (sellRateLayout != null) {
            sellActionText.setText(R.string.sell);
            sellBitValueText.setText("1");
            sellBitUnitText.setText(R.string.unit_bit);
            for (RateStruct rate : AppManager.typeRateStructs.rateStructs) {
                if ("btc".equals(rate.bit_type)) {
                    sellMoneyValueText.setText(rate.currency_sell + "");
                }
            }
            sellMoneyUnitTextView.setText(AppManager.DTM_CURRENCY);
            sellRateLayout.invalidate();
        } else {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            sellRateLayout = (LinearLayout) mInflater.inflate(R.layout.view_rate_text, null);
            sellRateLayout.setBackgroundResource(R.drawable.rate_sell);
            sellRateLayout.setGravity(Gravity.CENTER);

            sellActionText = (TextView) sellRateLayout.findViewById(R.id.action_text);
            sellBitValueText = (TextView) sellRateLayout.findViewById(R.id.bit_value_text);
            sellBitUnitText = (TextView) sellRateLayout.findViewById(R.id.bit_unit_text);
            sellMoneyValueText = (TextView) sellRateLayout.findViewById(R.id.money_value_text);
            sellMoneyUnitTextView = (TextView) sellRateLayout.findViewById(R.id.money_unit_text);
            sellActionText.setTextColor(Color.WHITE);
            sellBitValueText.setTextColor(Color.WHITE);
            sellBitUnitText.setTextColor(Color.WHITE);
            sellMoneyValueText.setTextColor(Color.WHITE);
            sellMoneyUnitTextView.setTextColor(Color.WHITE);
            TextView equal_text = (TextView)sellRateLayout.findViewById(R.id.equal_text);
            equal_text.setTextColor(Color.WHITE);

            sellActionText.setText(R.string.sell);
            sellBitValueText.setText("1");
            sellBitUnitText.setText(R.string.unit_bit);
            for (RateStruct rate : AppManager.typeRateStructs.rateStructs) {
                if ("btc".equals(rate.bit_type)) {
                    sellMoneyValueText.setText(rate.bit_sell + "");
                }
            }
            sellMoneyUnitTextView.setText(AppManager.DTM_CURRENCY);

            LayoutParams wmParams = new LayoutParams();
            wmParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            wmParams.type = LayoutParams.TYPE_PHONE;
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
            wmParams.x = 230;//Util.dip2px(mContext, 20);
            wmParams.y = 20;//Util.dip2px(mContext, 20);
            wmParams.width = 300;//Util.dip2px(mContext, 10);
            wmParams.height = 60;//Util.dip2px(mContext, 10);
            mWManager.addView(sellRateLayout, wmParams);
        }
    }

    public void removeSellRate() {
        if (mWManager != null && sellRateLayout != null)
            mWManager.removeView(sellRateLayout);
    }
}
