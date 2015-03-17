/**
 *
 */
package com.bitocean.dtm.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.hardware.KS80TSDKManager;
import com.bitocean.dtm.hardware.L1000SDKManager;
import com.bitocean.dtm.hardware.UBASDKManager;
import com.bitocean.dtm.protocol.ProtocolDataInput;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class Util {
    private Context mContext;
    private static Toast mToast;

    public Util(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void showFeatureToast(String info) {
        TextView text = new TextView(mContext);
        text.setText(info);
        text.setTextColor(Color.WHITE);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        text.setBackgroundColor(Color.BLACK);
        if (mToast != null)
            mToast.setView(text);
        else {
            mToast = new Toast(mContext);
            mToast.setView(text);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        mToast.show();
    }

    public void showFeatureToast(int id) {
        TextView text = new TextView(mContext);
        text.setText(mContext.getString(id));
        text.setTextColor(Color.WHITE);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        text.setBackgroundColor(Color.BLACK);
        if (mToast != null)
            mToast.setView(text);
        else {
            mToast = new Toast(mContext);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(text);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        mToast.show();
    }

    public void showShortToast(int id) {
        TextView text = new TextView(mContext);
        text.setText(mContext.getString(id));
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        text.setBackgroundColor(Color.BLACK);
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showLog(String log) {
        ApplicationInfo info = AppManager.getContext().getApplicationInfo();
        if (0 != ((info.flags) & ApplicationInfo.FLAG_DEBUGGABLE)) {
            Log.d("BITOCEAN ATM", log);
        }
    }

    public ProgressDialog showProgressBar(String info) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(info);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void out_cash(final int currency_num) {
        //TODO 出钞，发送出钞通知，用户,记录交易数据库，完成后检查钱箱，关闭交易
        if (currency_num == 0)
            return;
        DBTools.getInstance().insertOutCashData(AppManager.getUserId(), "out_cash", AppManager.DTM_UUID, AppManager.getCurrentTime(), "", AppManager.DTM_CURRENCY, String.valueOf(currency_num));
        DBTools.getInstance().closeDB();
        try {
            L1000SDKManager.getInstance().begin(currency_num / AppManager.DTM_BOX_OUT_CASH);
            L1000SDKManager.getInstance().setOnDispenseListener(new L1000SDKManager.onDispenseListener() {    //监听L1000SDK

                @Override
                public void onDispenseFinished() {
                    L1000SDKManager.getInstance().end(); // 关闭出钞模块
                }

                @Override
                public void onError(Exception e) {
                    DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "out_cash", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                    DBTools.getInstance().closeDB();
                    L1000SDKManager.getInstance().end(); // 关闭出钞模块
                }

                @Override
                public void onComRead(byte[] rxbuffer, int size) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onNotesNearEnd() {
                    // TODO Auto-generated method stub
                    // TODO 钱箱里面没钱了
                    DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "out_cash", AppManager.DTM_UUID, AppManager.getCurrentTime(), "no money");
                    DBTools.getInstance().closeDB();
                    AppManager.isLockDTM = true;
                    NetServiceManager.getInstance().UploadHardwareState("out_cash", "no money", AppManager.getCurrentTime());
                }
            });
        } catch (Exception e) {
            return;
        }
    }

    public static void in_cash() {
        UBASDKManager.getInstance().stopflag = true;
        UBASDKManager.getInstance().begin();
        UBASDKManager.getInstance().setOnUBAListener(new UBASDKManager.onUBAListener() { // 监听UBA
            @Override
            public void onCash(int num) {
                // 这里写监听到入钞结束后的处理
                DBTools.getInstance().insertInCashData(AppManager.getUserId(), "in_cash", AppManager.DTM_UUID, AppManager.getCurrentTime(), "", AppManager.DTM_CURRENCY, String.valueOf(num));
                DBTools.getInstance().closeDB();
                EventBus.getDefault()
                        .post(new ATMBroadCastEvent(
                                ATMBroadCastEvent.EVENT_IN_CASH_NUM, num));
            }

            @Override
            public void onComRead(byte[] rxbuffer, int size) {
                // 这里写监听到串口收到数据后的处理

            }

            @Override
            public void onError(Exception e) {
                // 这里写监听到初始化串口出错后的处理
                DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "in_cash", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                DBTools.getInstance().closeDB();
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void printBuyQRCoin(String bitcoin, String currency) {
        KS80TSDKManager.getInstance().printBuyQRCoin(KS80TSDKManager.PRINT_TYPE_BUY_QR, bitcoin, currency);
        KS80TSDKManager.getInstance().begin();
        KS80TSDKManager.getInstance().setOnPrintListener(new KS80TSDKManager.onPrintListener() {
            @Override
            public void onFinished() {
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onError(Exception e) {
                // 打印出钞凭条失败，跳转至交易失败页面
                DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "p_buy_qr", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                DBTools.getInstance().closeDB();
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onComRead(byte[] rxbuffer, int size) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void printBuyWalletCoin(String bitcoin, String currency, String public_key, String private_key, Bitmap publicKeyBitmap,
                                          Bitmap privateKeyBitmap) {
        KS80TSDKManager.getInstance().printBuyWalletCoin(KS80TSDKManager.PRINT_TYPE_BUY_WALLET, bitcoin, currency, public_key, private_key, publicKeyBitmap, privateKeyBitmap);
        KS80TSDKManager.getInstance().begin();
        KS80TSDKManager.getInstance().setOnPrintListener(new KS80TSDKManager.onPrintListener() {
            @Override
            public void onFinished() {
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onError(Exception e) {
                // 打印出钞凭条失败，跳转至交易失败页面
                DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "p_buy_wallet", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                DBTools.getInstance().closeDB();
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onComRead(byte[] rxbuffer, int size) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void printSellCoin(String currency) {
        KS80TSDKManager.getInstance().printSellCoin(KS80TSDKManager.PRINT_TYPE_SELL_COIN, currency);
        KS80TSDKManager.getInstance().begin();
        KS80TSDKManager.getInstance().setOnPrintListener(new KS80TSDKManager.onPrintListener() {
            @Override
            public void onFinished() {
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onError(Exception e) {
                DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "p_sell", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                DBTools.getInstance().closeDB();
                // 打印出钞凭条失败，跳转至交易失败页面
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onComRead(byte[] rxbuffer, int size) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void printRedeemCode(String currency, String redeem_code, Bitmap redeemCodeBitmap) {
        KS80TSDKManager.getInstance().printRedeemCode(KS80TSDKManager.PRINT_TYPE_REDEEM_CODE, currency, redeem_code, redeemCodeBitmap);
        KS80TSDKManager.getInstance().begin();
        KS80TSDKManager.getInstance().setOnPrintListener(new KS80TSDKManager.onPrintListener() {
            @Override
            public void onFinished() {
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onError(Exception e) {
                DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "p_redeem", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                DBTools.getInstance().closeDB();
                // 打印出钞凭条失败，跳转至交易失败页面
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onComRead(byte[] rxbuffer, int size) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void printTransFaild() {
        KS80TSDKManager.getInstance().printTransFaild(KS80TSDKManager.PRINT_TYPE_TRANS_FAILD);
        KS80TSDKManager.getInstance().begin();
        KS80TSDKManager.getInstance().setOnPrintListener(new KS80TSDKManager.onPrintListener() {
            @Override
            public void onFinished() {
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onError(Exception e) {
                DBTools.getInstance().insertHardwareData(AppManager.getUserId(), "p_tran_fail", AppManager.DTM_UUID, AppManager.getCurrentTime(), e.getMessage().toString());
                DBTools.getInstance().closeDB();
                // 打印出钞凭条失败，跳转至交易失败页面
                KS80TSDKManager.getInstance().end();
            }

            @Override
            public void onComRead(byte[] rxbuffer, int size) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,
                    500, 500);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    public void recordRedeem(String money) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user", AppManager.getUserId());
        map.put("dtm_uuid", AppManager.DTM_UUID);
        map.put("dtm_currency", AppManager.DTM_CURRENCY);
        map.put("time", Calendar.getInstance().getTime().toLocaleString());
        map.put("money", money);
        MobclickAgent.onEvent(mContext, "redeem", map);
    }

    public void recordSell(String money) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user", AppManager.getUserId());
        map.put("dtm_uuid", AppManager.DTM_UUID);
        map.put("dtm_currency", AppManager.DTM_CURRENCY);
        map.put("time", Calendar.getInstance().getTime().toLocaleString());
        map.put("money", money);
        MobclickAgent.onEvent(mContext, "sell", map);
    }

    public void recordBuyQR(String money) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user", AppManager.getUserId());
        map.put("dtm_uuid", AppManager.DTM_UUID);
        map.put("dtm_currency", AppManager.DTM_CURRENCY);
        map.put("time", Calendar.getInstance().getTime().toLocaleString());
        map.put("money", money);
        MobclickAgent.onEvent(mContext, "buyQR", map);
    }

    public void recordBuyWallet(String money) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user", AppManager.getUserId());
        map.put("dtm_uuid", AppManager.DTM_UUID);
        map.put("dtm_currency", AppManager.DTM_CURRENCY);
        map.put("time", Calendar.getInstance().getTime().toLocaleString());
        map.put("money", money);
        MobclickAgent.onEvent(mContext, "buyWallet", map);
    }
}
