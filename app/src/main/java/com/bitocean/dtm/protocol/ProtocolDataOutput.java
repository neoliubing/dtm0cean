package com.bitocean.dtm.protocol;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bitocean.dtm.controller.AppManager;
import com.google.common.io.Files;

/**
 * @author bing.liu
 */
public class ProtocolDataOutput {

    //管理员登陆
    public static JSONObject loginAdmin(String admin_id, String admin_password)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("admin_id", admin_id);
            output.put("admin_password", admin_password);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("dtm_state", AppManager.DTM_STATE);
            output.put("dtm_operators", AppManager.DTM_OPERATORS);
            output.put("versionCode", String.valueOf(AppManager.versionCode));
            output.put("versionName", AppManager.versionNameString);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //用户登陆
    public static JSONObject loginUser(String user_id, String user_password)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("user_id", user_id);
            output.put("user_password", user_password);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("dtm_state", AppManager.DTM_STATE);
            output.put("dtm_operators", AppManager.DTM_OPERATORS);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取验证码
    public static JSONObject verifyCode(String user_id) throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("user_id", user_id);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取汇率
    public static JSONObject getRateList(ArrayList<String> bitType)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            JSONArray rate_list = new JSONArray();
            for (String type : bitType) {
                rate_list.put(type);
            }
            output.put("rate_list", rate_list);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("dtm_box_out_cash", AppManager.DTM_BOX_OUT_CASH);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //赎回确认码验证
    public static JSONObject redeemConfirm(String redeemCode, String txid)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("redeem_code", redeemCode);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("txid", txid);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //注册,完善资料
    public static JSONObject register(String register_id, String register_password, String phone, String verify_code, String user_icon, String passport)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("register_id", register_id);
            output.put("register_password", register_password);
            output.put("phone", phone);
            output.put("verify_code", verify_code);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("user_icon", user_icon);
            output.put("passport", passport);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取卖币二维码
    public static JSONObject getSellQRCode(String user_id, int currency_num)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("bit_type", "btc");
            output.put("user_id", user_id);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("currency_num", currency_num);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //卖币确认
    public static JSONObject getSellBitcoinConfirm(String user_id, int currency_num, String bitcoin_qr)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("bit_type", "btc");
            output.put("user_id", user_id);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("currency_num", currency_num);
            output.put("bitcoin_qr", bitcoin_qr);
            output.put("dtm_box_out_cash", AppManager.DTM_BOX_OUT_CASH);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //二维码买币
    public static JSONObject buyBitcoinQR(String user_public_key, String user_id, int currency_num)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("user_public_key", user_public_key);
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("bit_type", "btc");
            output.put("user_id", user_id);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("currency_num", currency_num);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //纸钱包买币
    public static JSONObject buyBitcoinPrintWallet(String user_id, int currency_num)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("bit_type", "btc");
            output.put("user_id", user_id);
            output.put("dtm_currency", AppManager.DTM_CURRENCY);
            output.put("currency_num", currency_num);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //硬件设备状态上报
    public static JSONObject uploadHardwareState(String module, String state, String time)
            throws JSONException {
        try {
            JSONObject output = new JSONObject();
            output.put("dtm_uuid", AppManager.DTM_UUID);
            output.put("module", "module");
            output.put("state", state);
            output.put("dtm_time", time);
            return output;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
