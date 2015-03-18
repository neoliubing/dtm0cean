package com.bitocean.dtm.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.struct.BuyBitcoinPrintWalletStruct;
import com.bitocean.dtm.struct.BuyBitcoinQRStruct;
import com.bitocean.dtm.struct.LoginAdminStruct;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.struct.RateStruct;
import com.bitocean.dtm.struct.RedeemConfirmStruct;
import com.bitocean.dtm.struct.RegisterStruct;
import com.bitocean.dtm.struct.SellBitcoinConfirmStruct;
import com.bitocean.dtm.struct.SellBitcoinQRStruct;
import com.bitocean.dtm.struct.VerifyCodeStruct;
import com.google.common.eventbus.EventBus;

/**
 * @author bing.liu
 */
public class ProtocolDataInput {
    // 管理员登陆
    public static LoginAdminStruct parseLoginAdminToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {

            LoginAdminStruct struct = new LoginAdminStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            struct.update_infoString = obj.getString("info");
            struct.update_linkString = obj.getString("link");
            struct.public_keyString = obj.getString("public_key");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 用户登陆
    public static LoginUserStruct parseLoginUserToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            LoginUserStruct struct = new LoginUserStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            if(struct.resutlString.equals("fail"))
                return struct;
            struct.userTypeString = obj.getString("user_type");
            struct.levelString = obj.getString("level");
            struct.sourceString = obj.getString("source");
            struct.buy_date_remaining_quota = obj.getInt("buy_date_remaining_quota");
            struct.sell_date_remaining_quota = obj.getInt("sell_date_remaining_quota");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 验证码
    public static VerifyCodeStruct parseVerifyCodeToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            VerifyCodeStruct struct = new VerifyCodeStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 注册，完善资料
    public static RegisterStruct parseRegisterToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            RegisterStruct struct = new RegisterStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 汇率列表
    public static void parseRateListToJson(JSONObject obj) throws JSONException {
        if (obj == null) {
            return;
        }
        try {
            JSONArray arrays = obj.getJSONArray("rate_list");
            if (arrays == null || arrays.length() == 0)
                return;

            AppManager.typeRateStructs.rateStructs.clear();
            for (int i = 0; i < arrays.length(); i++) {
                JSONObject item = (JSONObject) arrays.opt(i);
                RateStruct struct = new RateStruct();
                struct.bit_type = item.getString("bit_type");
                struct.dtm_currency = item.getString("dtm_currency");
                struct.currency_buy = item.getDouble("currency_buy");
                struct.currency_sell = item.getDouble("currency_sell");
                struct.bit_buy = item.getDouble("bit_buy");
                struct.bit_sell = item.getDouble("bit_sell");
                struct.threshold_buy_min = item.getInt("threshold_buy_min");
                struct.threshold_buy_max = item.getInt("threshold_buy_max");
                struct.threshold_sell_min = item.getInt("threshold_sell_min");
                struct.threshold_sell_max = item.getInt("threshold_sell_max");
                AppManager.typeRateStructs.currency_typeString = struct.dtm_currency;
                AppManager.typeRateStructs.rateStructs.add(struct);
            }

            if (obj.getInt("lock") == 0) {
                AppManager.isLockDTM = false;
                AppManager.lockReason = obj.getInt("lock_reason");
                de.greenrobot.event.EventBus.getDefault()
                        .post(new ATMBroadCastEvent(
                                ATMBroadCastEvent.EVENT_DTM_UNLOCK));
            } else {
                AppManager.isLockDTM = true;
                AppManager.lockReason = obj.getInt("lock_reason");
                de.greenrobot.event.EventBus.getDefault()
                        .post(new ATMBroadCastEvent(
                                ATMBroadCastEvent.EVENT_DTM_LOCK));
            }
            return;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    // 赎回确认码
    public static RedeemConfirmStruct parseRedeemConfirmToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            RedeemConfirmStruct struct = new RedeemConfirmStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            struct.currency_type = obj.getString("dtm_currency");
            struct.currency_num = obj.getInt("currency_num");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取blockchain码
    public static SellBitcoinQRStruct parseSellQRCodeToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            SellBitcoinQRStruct struct = new SellBitcoinQRStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            struct.bitcoin_qr = obj.getString("bitcoin_qr");
            struct.tradeid = obj.getString("tradeid");
            struct.quota_num = obj.getInt("fast_threshold");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取卖币确认 需要轮询获取
    public static SellBitcoinConfirmStruct parseSellBitcoinConfirmToJson(
            JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            SellBitcoinConfirmStruct struct = new SellBitcoinConfirmStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            struct.dtm_currency = obj.getString("dtm_currency");
            struct.currency_num = obj.getInt("currency_num");
            struct.redeem_code = obj.getString("redeem_code");
            struct.txid = obj.getString("txid");
            if (AppManager.getUserStruct() != null)
                AppManager.getUserStruct().sell_date_remaining_quota = obj.getInt("sell_date_remaining_quota");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 二维码买币
    public static BuyBitcoinQRStruct parseBuyBitcoinQRToJson(JSONObject obj)
            throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            BuyBitcoinQRStruct struct = new BuyBitcoinQRStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            struct.user_public_key = obj.getString("user_public_key");
            struct.bit_type = obj.getString("bit_type");
            struct.trans_id = obj.getString("transid");
            struct.bitcoin_num = obj.getDouble("bitcoin_num");
            if (AppManager.getUserStruct() != null)
                AppManager.getUserStruct().buy_date_remaining_quota = obj.getInt("buy_date_remaining_quota");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 纸钱包买币
    public static BuyBitcoinPrintWalletStruct parseBuyBitcoinPrintWalletToJson(
            JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            BuyBitcoinPrintWalletStruct struct = new BuyBitcoinPrintWalletStruct();
            struct.resutlString = obj.getString("result");
            struct.reason = obj.getInt("reason");
            struct.wallet_public_key = obj.getString("wallet_public_key");
            struct.wallet_private_key = obj.getString("wallet_private_key");
            struct.bit_type = obj.getString("bit_type");
            struct.bitcoin_num = obj.getDouble("bitcoin_num");
            struct.trans_id = obj.getString("transid");
            if (AppManager.getUserStruct() != null)
                AppManager.getUserStruct().buy_date_remaining_quota = obj.getInt("buy_date_remaining_quota");
            return struct;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}