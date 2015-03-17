package com.bitocean.dtm.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Base64;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bitocean.dtm.BitOceanATMApp;
import com.bitocean.dtm.protocol.ProtocolDataInput;
import com.bitocean.dtm.protocol.ProtocolDataOutput;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.service.PollingBroadCastEvent;
import com.google.common.io.Files;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class NetServiceManager extends BaseManager {
    private static NetServiceManager mInstance;

    private static final String NET_SERVER = "http://infocomm.duapp.com/";
//    private static final String NET_LOGIN_ADMIN = NET_SERVER
//            + "loginadmin_bit.py";

    private static final String NET_LOGIN_ADMIN = "http://54.64.141.230:8081/accounts/loginadmin/";
    private static final String NET_VERIFY_CODE = NET_SERVER + "verify_bit.py";
//    private static final String NET_REGISTER_USER = NET_SERVER
//            + "register_bit.py";

    private static final String NET_REGISTER_USER = "http://54.64.141.230:8081/accounts/regisuser/";
    //    private static final String NET_LOGIN_USER = NET_SERVER
//            + "loginuser_bit.py";
    private static final String NET_LOGIN_USER = "http://54.64.141.230:8081/accounts/loginuser/";
    //    private static final String NET_GET_RATE_LIST = NET_SERVER
//            + "getrate_bit.py";
    private static final String NET_GET_RATE_LIST = "http://54.64.141.230:8081/exchange_rate/";
    private static final String NET_REDEEM_CONFIRM = NET_SERVER
            + "redeemconfirm_bit.py";
    private static final String NET_SELL_QR_CODE = NET_SERVER + "sellqr_bit.py";
    private static final String NET_SELL_BITCOIN = NET_SERVER + "sell_bit.py";
    //    private static final String NET_BUY_QR_BITCOIN = NET_SERVER + "buy_qr_bit.py";
    private static final String NET_BUY_QR_BITCOIN = "http://54.64.141.230:8081/trade/qrbuycoin/";
    //    private static final String NET_BUY_WALLET_BITCOIN = NET_SERVER + "buy_wallet_bit.py";
    private static final String NET_BUY_WALLET_BITCOIN = "http://54.64.141.230:8081/trade/walletbuycoin/";
    private static final String NET_HARDWARE_STATE = NET_SERVER + "hardware_state.py";

    private NetServiceManager(Application app) {
        super(app);
        // TODO Auto-generated constructor stub
        initManager();
    }

    public static NetServiceManager getInstance() {
        NetServiceManager instance;
        if (mInstance == null) {
            synchronized (NetServiceManager.class) {
                if (mInstance == null) {
                    instance = new NetServiceManager(
                            BitOceanATMApp.getApplication());
                    mInstance = instance;
                }
            }
        }
        return mInstance;
    }

    @Override
    protected void initManager() {
        // TODO Auto-generated method stub

    }

    @Override
    public void DestroyManager() {
        // TODO Auto-generated method stub

    }

    // 管理员登录
    public void loginAdmin(String admin_id, String admin_password) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.loginAdmin(admin_id,
                    admin_password);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_LOGIN_ADMIN, obj,
                    new Listener() {

                        @Override
                        public void onResponse(Object arg0) {
                            // TODO Auto-generated method stub
                            try {
                                EventBus.getDefault()
                                        .post(new ATMBroadCastEvent(
                                                ATMBroadCastEvent.EVENT_ADMIN_LOGIN_SUCCESS,
                                                ProtocolDataInput
                                                        .parseLoginAdminToJson((JSONObject) arg0)));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                EventBus.getDefault()
                                        .post(new ATMBroadCastEvent(
                                                ATMBroadCastEvent.EVENT_ADMIN_LOGIN_FAIL,
                                                e.getMessage()));
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_ADMIN_LOGIN_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 获取注册验证码
    public void verifyCode(String user_id) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.verifyCode(user_id);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_VERIFY_CODE, obj,
                    new Listener() {

                        @Override
                        public void onResponse(Object arg0) {
                            // TODO Auto-generated method stub
                            try {
                                EventBus.getDefault()
                                        .post(new ATMBroadCastEvent(
                                                ATMBroadCastEvent.EVENT_VERIFY_CODE_SUCCESS,
                                                ProtocolDataInput
                                                        .parseVerifyCodeToJson((JSONObject) arg0)));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                EventBus.getDefault()
                                        .post(new ATMBroadCastEvent(
                                                ATMBroadCastEvent.EVENT_VERIFY_CODE_FAIL,
                                                e.getMessage()));
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_VERIFY_CODE_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 用户注册
    public void registerUser(String user_id, String user_password, String phone,
                             String verifyCode, String userIconString, String passportString) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            File userIcon = new File(userIconString);
            byte[] byteUserIconData = new byte[0];
            try {
                byteUserIconData = Files.toByteArray(userIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File passport = new File(passportString);
            byte[] bytepassportData = new byte[0];
            try {
                bytepassportData = Files.toByteArray(passport);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject obj = ProtocolDataOutput.register(user_id, user_password, phone, verifyCode, new String(Base64.encodeToString(byteUserIconData, Base64.NO_WRAP)), new String(Base64.encodeToString(bytepassportData, Base64.NO_WRAP)));
            mQueue.add(new JsonObjectRequest(Method.POST, NET_REGISTER_USER,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {

                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_USER_REGISTER_SUCCESS,
                                        ProtocolDataInput
                                                .parseRegisterToJson((JSONObject) arg0)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_USER_REGISTER_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_USER_REGISTER_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 用户登录
    public void loginUser(String user_id, String user_password) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.loginUser(user_id,
                    user_password);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_LOGIN_USER, obj,
                    new Listener() {

                        @Override
                        public void onResponse(Object arg0) {
                            // TODO Auto-generated method stub
                            try {
                                EventBus.getDefault()
                                        .post(new ATMBroadCastEvent(
                                                ATMBroadCastEvent.EVENT_USER_LOGIN_SUCCESS,
                                                ProtocolDataInput
                                                        .parseLoginUserToJson((JSONObject) arg0)));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                EventBus.getDefault()
                                        .post(new ATMBroadCastEvent(
                                                ATMBroadCastEvent.EVENT_USER_LOGIN_FAIL,
                                                e.getMessage()));
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_USER_LOGIN_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 获取汇率
    public void getRateList(ArrayList<String> bitType) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.getRateList(bitType);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_GET_RATE_LIST,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {
                        ProtocolDataInput
                                .parseRateListToJson((JSONObject) arg0);
                        EventBus.getDefault()
                                .post(new PollingBroadCastEvent(
                                        PollingBroadCastEvent.EVENT_GET_RATE_LIST_SUCCESS));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new PollingBroadCastEvent(
                                        PollingBroadCastEvent.EVENT_GET_RATE_LIST_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new PollingBroadCastEvent(
                                    PollingBroadCastEvent.EVENT_GET_RATE_LIST_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 赎回
    public void redeemConfirm(String redeemString, String txid) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.redeemConfirm(redeemString, txid);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_REDEEM_CONFIRM,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {

                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_REDEEM_CONFIRM_SUCCESS,
                                        ProtocolDataInput
                                                .parseRedeemConfirmToJson((JSONObject) arg0)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_REDEEM_CONFIRM_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_REDEEM_CONFIRM_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 获取卖币二维码
    public void getSellQRCode(String user_id,
                              int currency_num) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.getSellQRCode(user_id, currency_num);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_SELL_QR_CODE,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {

                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_GET_SELL_QR_CODE_SUCCESS,
                                        ProtocolDataInput
                                                .parseSellQRCodeToJson((JSONObject) arg0)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_GET_SELL_QR_CODE_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_GET_SELL_QR_CODE_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    // 卖币确认
    public void SellBitcoin(String user_id,
                            int currency_num, String bitcoin_qr) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.getSellBitcoinConfirm(
                    user_id, currency_num, bitcoin_qr);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_SELL_BITCOIN,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {

                        EventBus.getDefault()
                                .post(new PollingBroadCastEvent(
                                        PollingBroadCastEvent.EVENT_GET_SELL_SUCCESS,
                                        ProtocolDataInput
                                                .parseSellBitcoinConfirmToJson((JSONObject) arg0)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new PollingBroadCastEvent(
                                        PollingBroadCastEvent.EVENT_GET_SELL_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new PollingBroadCastEvent(
                                    PollingBroadCastEvent.EVENT_GET_SELL_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    //二维码买币
    public void BuyQRBitcoin(String user_public_key, String user_id,
                             int currency_num) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.buyBitcoinQR(user_public_key,
                    user_id, currency_num);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_BUY_QR_BITCOIN,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {

                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_BUY_QR_SUCCESS,
                                        ProtocolDataInput
                                                .parseBuyBitcoinQRToJson((JSONObject) arg0)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_BUY_QR_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_BUY_QR_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    //纸钱包买币
    public void BuyWalletBitcoin(String user_id,
                                 int currency_num) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.buyBitcoinPrintWallet(
                    user_id, currency_num);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_BUY_WALLET_BITCOIN,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                    try {

                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_BUY_WALLET_SUCCESS,
                                        ProtocolDataInput
                                                .parseBuyBitcoinPrintWalletToJson((JSONObject) arg0)));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        EventBus.getDefault()
                                .post(new ATMBroadCastEvent(
                                        ATMBroadCastEvent.EVENT_BUY_WALLET_FAIL,
                                        e.getMessage()));
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                    String msg = null;
                    if (arg0.getMessage() != null)
                        msg = arg0.getMessage();
                    else
                        msg = arg0.getLocalizedMessage();
                    EventBus.getDefault()
                            .post(new ATMBroadCastEvent(
                                    ATMBroadCastEvent.EVENT_BUY_WALLET_FAIL,
                                    msg));
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }

    //硬件设备状态上报
    public void UploadHardwareState(String module,
                                    String state, String time) {
        RequestQueue mQueue = Volley.newRequestQueue(BitOceanATMApp
                .getContext());
        try {
            JSONObject obj = ProtocolDataOutput.uploadHardwareState(module, state, time);
            mQueue.add(new JsonObjectRequest(Method.POST, NET_HARDWARE_STATE,
                    obj, new Listener() {

                @Override
                public void onResponse(Object arg0) {
                    // TODO Auto-generated method stub
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // TODO Auto-generated method stub
                }
            }));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mQueue.start();
    }
}
