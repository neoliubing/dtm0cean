package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.hardware.UBASDKManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.service.ProcessEvent;
import com.bitocean.dtm.struct.BuyBitcoinPrintWalletStruct;
import com.bitocean.dtm.struct.BuyBitcoinQRStruct;
import com.bitocean.dtm.util.Util;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class BuyBillFragment extends NodeFragment {
    private String user_public_key = null;
    private String bit_num = null;
    private TextView currency_num_text, bit_num_text, cash_num_text;
    private int process_event = 0;
    private ProgressDialog progressDialog = null;
    private int currency_num;
    private int cash_num = 0;
    private Button cancelButton;

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
        currency_num = (int) b.getInt("currency_num", 0);
        bit_num = (String) b.getString("bit_num");
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        UBASDKManager.getInstance().end();
        UBASDKManager.getInstance().stopflag = false;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_buy_bill, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.buy_bill_prompt);

        cancelButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.left_btn);
        cancelButton.setText(R.string.back);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getActivity().getSupportFragmentManager().popBackStack("currenycountfragment", 0);
            }
        });

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.buy_btn);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                checkBoxInCash();
            }
        });

        currency_num_text = (TextView) v.findViewById(R.id.currency_num_text);
        String currency_text = getString(R.string.buy_currency_note) + String.valueOf(currency_num) + " " + AppManager.DTM_CURRENCY;
        currency_num_text.setText(currency_text);
        bit_num_text = (TextView) v.findViewById(R.id.bit_num_text);
        String bit_text = getString(R.string.buy_bit_note) + " " + String.valueOf(bit_num) + "BTC";
        bit_num_text.setText(bit_text);
        cash_num_text = (TextView) v.findViewById(R.id.cash_num_text);
        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);

        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }
        Util.in_cash();
    }

    private void checkBoxInCash() {
//        cash_num = 2;
        if (cash_num == 0) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.buy_incash_prompt));
            return;
        }

        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        UBASDKManager.getInstance().stopflag = false;
        if (process_event == ProcessEvent.EVENT_BUY_QR) {
            NetServiceManager.getInstance().BuyQRBitcoin(user_public_key,
                    AppManager.getUserId(), cash_num);
            new Util(getActivity()).recordBuyQR(cash_num + "");
            progressDialog = new Util(getActivity())
                    .showProgressBar(getActivity().getString(R.string.wait));
        } else if (process_event == ProcessEvent.EVENT_BUY_WALLET) {
            NetServiceManager.getInstance().BuyWalletBitcoin(
                    AppManager.getUserId(), cash_num);
            new Util(getActivity()).recordBuyWallet(cash_num + "");
            progressDialog = new Util(getActivity())
                    .showProgressBar(getActivity().getString(R.string.wait));
        }
    }

    public void onEventMainThread(ATMBroadCastEvent event) {
        switch (event.getType()) {
            case ATMBroadCastEvent.EVENT_BUY_QR_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                BuyBitcoinQRStruct qrStruct = (BuyBitcoinQRStruct) event
                        .getObject();
                if ("success".equals(qrStruct.resutlString)) {
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(qrStruct.bitcoin_num);
                    ConfirmFragment fragment = new ConfirmFragment();
                    Bundle b = new Bundle();
                    String text = getString(R.string.buy_bit_note) + " " + str + "BTC";
                    b.putString("reason", text);
                    fragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("redeemconfirmfragment").commit();
                    Util.printBuyQRCoin(str + "BTC", String.valueOf(cash_num));
                    DBTools.getInstance().insertBuyTranData(qrStruct.trans_id, AppManager.getUserId(),
                            "buy_qr", "success", String.valueOf(qrStruct.reason), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                            AppManager.DTM_CURRENCY, String.valueOf(cash_num), qrStruct.bit_type, String.valueOf(qrStruct.bitcoin_num));
                    DBTools.getInstance().closeDB();
                } else if ("fail".equals(qrStruct.resutlString)) {
                    String msgString = null;
                    switch (qrStruct.reason) {
                        case 1:
                            msgString = getString(R.string.buy_fail_reason_2);
                            break;
                        default:
                            break;
                    }
                    Util.printTransFaild();
                    new Util(getActivity()).showFeatureToast(msgString);
                    DBTools.getInstance().insertBuyTranData(qrStruct.trans_id, AppManager.getUserId(),
                            "buy_qr", "fail", String.valueOf(qrStruct.reason), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                            AppManager.DTM_CURRENCY, String.valueOf(cash_num), qrStruct.bit_type, String.valueOf(qrStruct.bitcoin_num));
                    DBTools.getInstance().closeDB();
                }
                break;
            case ATMBroadCastEvent.EVENT_BUY_WALLET_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                BuyBitcoinPrintWalletStruct struct = (BuyBitcoinPrintWalletStruct) event
                        .getObject();
                if ("success".equals(struct.resutlString)) {
                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    df.setMaximumFractionDigits(8);
                    String str = df.format(struct.bitcoin_num);
                    ConfirmFragment fragment = new ConfirmFragment();
                    Bundle b = new Bundle();
                    String text = getString(R.string.buy_bit_note) + " " + str + "BTC";
                    b.putString("reason", text);
                    fragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("redeemconfirmfragment").commit();


                    Util.printBuyWalletCoin(str + "BTC", String.valueOf(cash_num),
                            struct.wallet_public_key, struct.wallet_private_key,
                            Util.generateQRCode(struct.wallet_public_key),
                            Util.generateQRCode(struct.wallet_private_key));
                    DBTools.getInstance().insertBuyTranData(struct.trans_id, AppManager.getUserId(),
                            "buy_wallet", "success", String.valueOf(struct.reason), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                            AppManager.DTM_CURRENCY, String.valueOf(cash_num), struct.bit_type, String.valueOf(struct.bitcoin_num));
                    DBTools.getInstance().closeDB();
                } else if ("fail".equals(struct.resutlString)) {
                    String msgString = null;
                    switch (struct.reason) {
                        case 1:
                            msgString = getString(R.string.buy_fail_reason_1);
                            break;
                        case 2:
                            msgString = getString(R.string.buy_fail_reason_2);
                            break;
                        default:
                            break;
                    }
                    Util.printTransFaild();
                    new Util(getActivity()).showFeatureToast(msgString);
                    DBTools.getInstance().insertBuyTranData(struct.trans_id, AppManager.getUserId(),
                            "buy_wallet", "fail", String.valueOf(struct.reason), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                            AppManager.DTM_CURRENCY, String.valueOf(cash_num), struct.bit_type, String.valueOf(struct.bitcoin_num));
                    DBTools.getInstance().closeDB();
                }
                break;
            case ATMBroadCastEvent.EVENT_BUY_WALLET_FAIL:
            case ATMBroadCastEvent.EVENT_BUY_QR_FAIL:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                new Util(getActivity())
                        .showFeatureToast(R.string.error_sell_buy);
                DBTools.getInstance().insertBuyTranData(String.valueOf(-1), AppManager.getUserId(),
                        "buy_qr_wallet", "fail", String.valueOf(-1), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                        AppManager.DTM_CURRENCY, String.valueOf(0), "BTC", String.valueOf(0));
                DBTools.getInstance().closeDB();
                break;
            case ATMBroadCastEvent.EVENT_IN_CASH_NUM:
                int num = (Integer) event.getObject();
                cash_num = num;
                cancelButton.setVisibility(View.INVISIBLE);
                updateText(cash_num);
                break;
            default:
                break;
        }
    }

    private void updateText(int cash_num) {
        cash_num_text.setText(cash_num + "  " + AppManager.DTM_CURRENCY);
        if (cash_num >= currency_num)
            checkBoxInCash();
    }
}
