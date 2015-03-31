package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.hardware.KS80TSDKManager;
import com.bitocean.dtm.hardware.L1000SDKManager;
import com.bitocean.dtm.service.PollingBroadCastEvent;
import com.bitocean.dtm.struct.SellBitcoinConfirmStruct;
import com.bitocean.dtm.util.Util;
import com.bitocean.dtm.hardware.KS80TSDKManager;
import com.bitocean.dtm.hardware.KS80TSDKManager.onPrintListener;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class SellConfirmWaitFragment extends NodeFragment {
//    private String user_public_key = null;
    private String bitcoin_qr = null;
    private String bit_num = null;
    private String tradeid = null;
    private int process_event = 0;
    private int currency_num = 0;
    private boolean isLoop = true;
    private ProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this, PollingBroadCastEvent.class);
        mContext = getActivity().getApplicationContext();
        Bundle b = getArguments();
        if (b == null)
            return;
//        user_public_key = (String) b.getString("user_public_key");
        bitcoin_qr = (String)b.getString("bitcoin_qr");
        process_event = (int) b.getInt("process_event", 0);
        currency_num = (int) b.getInt("currency_num", 0);
        bit_num = (String) b.getString("bit_num");
        tradeid = (String) b.getString("tradeid");
        return;
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
        AppManager.isLoopTime = false;
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_confirm, null);
        initView(v);
        return v;
    }

    public void onDestroyView() {
        AppManager.isLoopTime = true;
        super.onDestroyView();
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.confirm);

        TextView textView = (TextView) v.findViewById(R.id.text);
        textView.setText(R.string.sell_wait);

        Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.left_btn);
        cancelButton.setVisibility(View.INVISIBLE);

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setVisibility(View.INVISIBLE);
        checkSellResult();

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }

    private void checkSellResult() {
        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        NetServiceManager.getInstance().SellBitcoin(AppManager.getUserId(), currency_num, bitcoin_qr, bit_num, tradeid);

//        Thread sellThread = new Thread() {
//            public void run() {
//                while (isLoop) {
//                    NetServiceManager.getInstance().SellBitcoin(AppManager.getUserId(), currency_num, bitcoin_qr, bit_num, tradeid);
//                    try {
//                        sleep(6000);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        sellThread.start();
    }

    public void onEventMainThread(PollingBroadCastEvent event) {
        switch (event.getType()) {
            case PollingBroadCastEvent.EVENT_GET_SELL_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                SellBitcoinConfirmStruct struct = (SellBitcoinConfirmStruct) event
                        .getObject();
                if ("success".equals(struct.resutlString)) {
                    if (struct.currency_num == 0) {
                        //打印赎回码
                        Util.printRedeemCode(String.valueOf(struct.currency_num), struct.redeem_code, Util.generateQRCode(struct.redeem_code+"+"+struct.txid));
                    } else {
                        //吐钞
                        new Util(getActivity()).recordSell(struct.currency_num + "");
                        Util.out_cash(struct.currency_num);
                        Util.printSellCoin(String.valueOf(struct.currency_num));
                    }

                    DBTools.getInstance().insertSellTranData(struct.txid, AppManager.getUserId(),
                            "sell", "success", String.valueOf(struct.reason), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                            AppManager.DTM_CURRENCY, String.valueOf(struct.currency_num), "", "");
                    DBTools.getInstance().closeDB();

                    ConfirmFragment fragment = new ConfirmFragment();
                    Bundle b = new Bundle();
                    b.putString("reason", getString(R.string.complete_trans));
                    fragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("redeemconfirmfragment").commit();
                } else if ("fail".equals(struct.resutlString)) {
                    String msgString = null;
                    switch (struct.reason) {
                        case 1:
                            msgString = getString(R.string.sell_trans_error_1);
                            break;
                        case 2:
                            msgString = getString(R.string.sell_trans_error_2);
                            break;
                        case 3:
                            msgString = getString(R.string.sell_trans_error_3);
                        default:
                            break;
                    }
                    new Util(getActivity()).showFeatureToast(msgString);
                    DBTools.getInstance().insertSellTranData(struct.txid, AppManager.getUserId(),
                            "sell", "fail", String.valueOf(struct.reason), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                            AppManager.DTM_CURRENCY, String.valueOf(struct.currency_num), "", "");
                    DBTools.getInstance().closeDB();
                }
                isLoop = false;
                break;
            case PollingBroadCastEvent.EVENT_GET_SELL_FAIL:
                new Util(getActivity())
                        .showFeatureToast(R.string.error_sell_buy);
                DBTools.getInstance().insertSellTranData(String.valueOf(-1), AppManager.getUserId(),
                        "sell", "fail", String.valueOf(-1), AppManager.DTM_UUID, AppManager.getCurrentTime(),
                        AppManager.DTM_CURRENCY, "", "", "");
                DBTools.getInstance().closeDB();
                break;
            default:
                break;
        }
    }
}
