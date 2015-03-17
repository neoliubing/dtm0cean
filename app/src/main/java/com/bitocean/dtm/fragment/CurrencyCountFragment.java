package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitocean.dtm.BuyQRActivity;
import com.bitocean.dtm.BuyWalletActivity;
import com.bitocean.dtm.R;
import com.bitocean.dtm.TradeModeActivity;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.service.ProcessEvent;
import com.bitocean.dtm.struct.RateStruct;
import com.bitocean.dtm.struct.RedeemConfirmStruct;
import com.bitocean.dtm.struct.SellBitcoinQRStruct;
import com.bitocean.dtm.util.Util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class CurrencyCountFragment extends NodeFragment {
    private String user_public_key = null;
    private int process_event = 0;
    private ProgressDialog progressDialog = null;
    private int max_num, min_num;
    private int currency_num = AppManager.DTM_BOX_OUT_CASH;
    private Double bit_num;
    private TextView currency_num_text, bit_num_text;
    private TextView actionText, bitValueText, bitUnitText,
            moneyValueText, moneyUnitTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this, ATMBroadCastEvent.class);
        mContext = getActivity().getApplicationContext();
        Bundle b = getArguments();
        if (b == null)
            return;
        user_public_key = (String) b.getString("user_public_key");
        process_event = (int) b.getInt("process_event", 0);
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_currency_count, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.currency_prompt);

        Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.left_btn);
        cancelButton.setText(R.string.back);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.next);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                checkProcessEvent();
            }
        });

        Button addButton = (Button) v.findViewById(R.id.buy_add);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                countCurrencyNum(true);
            }
        });

        Button subButton = (Button) v.findViewById(R.id.buy_subtraction);
        subButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                countCurrencyNum(false);
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);

        RateStruct rate = null;
        for (int i = 0; i < AppManager.typeRateStructs.rateStructs.size(); i++) {
            rate = AppManager.typeRateStructs.rateStructs.get(i);
            if (rate == null)
                return;
            if ("btc".equals(rate.bit_type)) {
                if (ProcessEvent.EVENT_SELL == process_event) {
                    currency_num = rate.threshold_sell_min;
                    if (AppManager.isLogined) {
                        if (AppManager.userStruct.sell_date_remaining_quota > rate.threshold_sell_max) {
                            max_num = rate.threshold_sell_max;
                            min_num = rate.threshold_sell_min;
                        } else {
                            max_num = AppManager.userStruct.sell_date_remaining_quota;
                            min_num = rate.threshold_sell_min;
                        }
                    } else {
                        max_num = rate.threshold_sell_max;
                        min_num = rate.threshold_sell_min;
                    }
                } else {
                    currency_num = rate.threshold_buy_min;
                    if (AppManager.isLogined) {
                        if (AppManager.userStruct.buy_date_remaining_quota > rate.threshold_buy_max) {
                            max_num = rate.threshold_buy_max;
                            min_num = rate.threshold_buy_min;
                        } else {
                            max_num = AppManager.userStruct.buy_date_remaining_quota;
                            min_num = rate.threshold_buy_min;
                        }
                    } else {
                        max_num = rate.threshold_buy_max;
                        min_num = rate.threshold_buy_min;
                    }
                }
                break;
            }
        }

        currency_num_text = (TextView) v.findViewById(R.id.currency_num_text);
        bit_num_text = (TextView) v.findViewById(R.id.bit_num_text);

        actionText = (TextView) v.findViewById(R.id.rate_text)
                .findViewById(R.id.action_text);
        actionText.setVisibility(View.GONE);
        bitValueText = (TextView) v.findViewById(R.id.rate_text)
                .findViewById(R.id.bit_value_text);
        bitValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        bitUnitText = (TextView) v.findViewById(R.id.rate_text)
                .findViewById(R.id.bit_unit_text);
        bitUnitText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        moneyValueText = (TextView) v.findViewById(R.id.rate_text)
                .findViewById(R.id.money_value_text);
        moneyValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        moneyUnitTextView = (TextView) v.findViewById(R.id.rate_text)
                .findViewById(R.id.money_unit_text);
        moneyUnitTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);

        bitValueText.setText("1 ");
        bitUnitText.setText(R.string.unit_bit);
        moneyUnitTextView.setText(" " + AppManager.DTM_CURRENCY);

        currency_num_text.setText(String.valueOf(currency_num));
        if (ProcessEvent.EVENT_SELL == process_event) {
            bit_num = rate.bit_buy;
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setMaximumFractionDigits(8);
            String str = df.format(bit_num * currency_num / AppManager.DTM_BOX_OUT_CASH);
            moneyValueText.setText(" " + rate.currency_sell);
            bit_num_text.setText("= " + str + " " + getString(R.string.unit_mbit));
        } else {
            bit_num = rate.bit_sell;
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setMaximumFractionDigits(8);
            String str = df.format(bit_num * currency_num / AppManager.DTM_BOX_OUT_CASH);
            moneyValueText.setText(" " + rate.currency_buy);
            bit_num_text.setText("= " + str + " " + getString(R.string.unit_mbit));
        }


    }

    private void countCurrencyNum(boolean isAdd) {
        if (ProcessEvent.EVENT_SELL == process_event) {
            if (isAdd) {
                if (currency_num >= max_num) {
                    new Util(getActivity()).showShortToast(R.string.max_num);
                } else {
                    currency_num += AppManager.DTM_BOX_OUT_CASH;
                    currency_num_text.setText(String.valueOf(currency_num));
                    int size = Integer.valueOf(currency_num / AppManager.DTM_BOX_OUT_CASH);
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(bit_num * size);
                    bit_num_text.setText("= " + str + " " + getString(R.string.unit_mbit));
                }
            } else {
                if (currency_num <= min_num) {
                    new Util(getActivity()).showShortToast(R.string.min_num);
                } else {
                    currency_num -= AppManager.DTM_BOX_OUT_CASH;
                    currency_num_text.setText(String.valueOf(currency_num));
                    int size = Integer.valueOf(currency_num / AppManager.DTM_BOX_OUT_CASH);
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(bit_num * size);
                    bit_num_text.setText("= " + str + " " + getString(R.string.unit_mbit));
                }
            }
        } else {
            if (isAdd) {
                if (currency_num >= max_num) {
                    new Util(getActivity()).showShortToast(R.string.max_num);
                } else {
                    currency_num += AppManager.DTM_BOX_IN_CASH;
                    currency_num_text.setText(String.valueOf(currency_num));
                    int size = Integer.valueOf(currency_num / AppManager.DTM_BOX_IN_CASH);
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(bit_num * size);
                    bit_num_text.setText("= " + str + " " + getString(R.string.unit_mbit));
                }
            } else {
                if (currency_num <= min_num) {
                    new Util(getActivity()).showShortToast(R.string.min_num);
                } else {
                    currency_num -= AppManager.DTM_BOX_IN_CASH;
                    currency_num_text.setText(String.valueOf(currency_num));
                    int size = Integer.valueOf(currency_num / AppManager.DTM_BOX_IN_CASH);
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(bit_num * size);
                    bit_num_text.setText("= " + str + " " + getString(R.string.unit_mbit));
                }
            }
        }
    }

    private void checkProcessEvent() {
        if (process_event == ProcessEvent.EVENT_SELL) {
            // 卖币
            // 获取卖币码
            if (!AppManager.isNetEnable) {
                new Util(mContext).showFeatureToast(mContext
                        .getString(R.string.network_error));
                return;
            }

            NetServiceManager.getInstance().getSellQRCode(AppManager.getUserId(), currency_num);
            progressDialog = new Util(getActivity())
                    .showProgressBar(getActivity().getString(R.string.wait));
        } else {
            // 二维码买币
            // 纸钱包
            PayConfirmFragment fragment = new PayConfirmFragment();
            Bundle b = new Bundle();
            b.putString("user_public_key", user_public_key);
            b.putInt("currency_num", currency_num);
            b.putInt("process_event", process_event);

            int size = Integer.valueOf(currency_num / AppManager.DTM_BOX_OUT_CASH);
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setMaximumFractionDigits(8);
            String str = df.format(bit_num * size);
            b.putString("bit_num", str);

            fragment.setArguments(b);
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(
                            FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container, fragment)
                    .addToBackStack("currenycountfragment").commit();
        }
    }

    public void onEventMainThread(ATMBroadCastEvent event) {
        switch (event.getType()) {
            case ATMBroadCastEvent.EVENT_GET_SELL_QR_CODE_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                SellBitcoinQRStruct struct = (SellBitcoinQRStruct) event
                        .getObject();
                if ("success".equals(struct.resutlString)) {
                    SellScanQRCodeFragment fragment = new SellScanQRCodeFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("sellbitcoinqrstruct", struct);
//                    b.putString("user_public_key", user_public_key);
                    b.putInt("currency_num", currency_num);
                    b.putInt("process_event", process_event);

                    int size = Integer.valueOf(currency_num / AppManager.DTM_BOX_OUT_CASH);
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(bit_num * size);
                    b.putString("bit_num", str);

                    fragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("currenycountfragment").commit();
                } else if ("fail".equals(struct.resutlString)) {
                    String msgString = null;
                    switch (struct.reason) {
                        case 1:
                            msgString = getString(R.string.sell_qr_code_error_1);
                            break;
                        case 2:
                            msgString = getString(R.string.sell_qr_code_error_2);
                            break;
                        default:
                            break;
                    }
                    new Util(getActivity()).showFeatureToast(msgString);
                }
                break;
            case ATMBroadCastEvent.EVENT_GET_SELL_QR_CODE_FAIL:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                new Util(getActivity())
                        .showFeatureToast((String) event.getObject());
                break;
            default:
                break;
        }
    }
}
