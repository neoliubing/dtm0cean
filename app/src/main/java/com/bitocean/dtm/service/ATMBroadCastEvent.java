package com.bitocean.dtm.service;
/**
 * @author bing.liu
 * 
 */
public class ATMBroadCastEvent {
	public final static int BASE_EVENT = 0;

	public final static int EVENT_GOHOME = BASE_EVENT + 1;
	public final static int EVENT_UPDATE_TIMER = BASE_EVENT + 2;
	public final static int EVENT_NETWORK_STATUS = BASE_EVENT + 3;
	//管理员登录===============
	public final static int EVENT_ADMIN_LOGIN_SUCCESS = BASE_EVENT + 4;
	public final static int EVENT_ADMIN_LOGIN_FAIL = BASE_EVENT + 5;
	//用户登录===============
	public final static int EVENT_USER_LOGIN_SUCCESS = BASE_EVENT + 6;
	public final static int EVENT_USER_LOGIN_FAIL = BASE_EVENT + 7;
	//用户注册===============
	public final static int EVENT_USER_REGISTER_SUCCESS = BASE_EVENT + 8;
	public final static int EVENT_USER_REGISTER_FAIL = BASE_EVENT + 9;
	//获取验证码===============
	public final static int EVENT_VERIFY_CODE_SUCCESS = BASE_EVENT + 10;
	public final static int EVENT_VERIFY_CODE_FAIL = BASE_EVENT + 11;
	//赎回======================
	public final static int EVENT_REDEEM_CONFIRM_SUCCESS = BASE_EVENT + 12;
	public final static int EVENT_REDEEM_CONFIRM_FAIL = BASE_EVENT + 13;
	//用户kyc注册======================
//	public final static int EVENT_USER_REGISTER_KYC_SUCCESS = BASE_EVENT + 14;
//	public final static int EVENT_USER_REGISTER_KYC_FAIL = BASE_EVENT + 15;
	//获取卖币bitcoin qr======================
	public final static int EVENT_GET_SELL_QR_CODE_SUCCESS = BASE_EVENT + 16;
	public final static int EVENT_GET_SELL_QR_CODE_FAIL = BASE_EVENT + 17;
	//二维码买币======================
	public final static int EVENT_BUY_QR_SUCCESS = BASE_EVENT + 18;
	public final static int EVENT_BUY_QR_FAIL = BASE_EVENT + 19;
	//纸钱包买币======================
	public final static int EVENT_BUY_WALLET_SUCCESS = BASE_EVENT + 20;
	public final static int EVENT_BUY_WALLET_FAIL = BASE_EVENT + 21;
    //设备锁定
    public final static int EVENT_DTM_LOCK = BASE_EVENT + 22;
    public final static int EVENT_DTM_UNLOCK = BASE_EVENT + 23;
    //入钞计数
    public final static int EVENT_IN_CASH_NUM = BASE_EVENT + 24;
	
	private int type;
	private Object obj;
	
	public ATMBroadCastEvent(int type) {
		this.type = type;
	}

	public ATMBroadCastEvent(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public Object getObject() {
		return obj;
	}
}
