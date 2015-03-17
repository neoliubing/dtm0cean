package com.bitocean.dtm.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bitocean.dtm.BitOceanATMApp;
import com.bitocean.dtm.service.ATMReceiver;
import com.bitocean.dtm.service.ATMService;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.struct.TypeRateStruct;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bing.liu
 */
public class AppManager extends BaseManager {
    private static AppManager mInstance;

    public final static int LOOP_TIMER = 300;//循环时间
    public static boolean isAdmin = false;//管理员模式
    public static boolean isLoopTime = true;//是否循环
    public static int loopTimer = LOOP_TIMER;
    public static String public_keyString = null;//公钥
    public static int versionCode = 0;
    public static String versionNameString = null;
    public static String DTM_UUID = null;
    public static String DTM_CURRENCY = "CNY";//机器支持的法币类型
    public static String DTM_STATE = "CHINA";//机器所在国家
    public static String DTM_OPERATORS = "BITOCEAN";//机器所属运营商
    public static String DTM_OPERATORS_PHONE = "";//运营商联系方式
    public static int DTM_BOX_IN_CASH = 1;//入钞机器基准法币值
    public static int DTM_BOX_OUT_CASH = 1;//出钞机器基准法币值
    public static ArrayList<String> bitType = new ArrayList<String>();//
    public static TypeRateStruct typeRateStructs = new TypeRateStruct();
    public static boolean isLogined = true;//是否需要登录
    public static boolean isKycRegister = true;//是否需要kyc注册
    public static boolean isNetEnable = true;
    public static boolean isLockDTM = false;
    public static int lockReason = 0;
    public static LoginUserStruct userStruct = null;
    public static String serviceDTM = "on";

    public ATMReceiver atmReceiver;
//    public ATMService mService;

    private AppManager(Application app) {
        super(app);
        // TODO Auto-generated constructor stub
        initManager();
    }

    public static AppManager getInstance() {
        AppManager instance;
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    instance = new AppManager(BitOceanATMApp.getApplication());
                    mInstance = instance;
                }
            }
        }
        return mInstance;
    }

    @Override
    protected void initManager() {
        getAdminStauts();
        if (atmReceiver == null) {
            atmReceiver = new ATMReceiver();
        }
        mContext.startService(new Intent(mContext, ATMService.class));
//        bindService();

        PackageManager pm = BitOceanATMApp.getApplication().getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(BitOceanATMApp.getApplication().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApplicationInfo appInfo = null;
        try {
            appInfo = BitOceanATMApp.getApplication().getPackageManager()
                    .getApplicationInfo(BitOceanATMApp.getApplication().getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (appInfo != null) {
            AppManager.DTM_UUID = appInfo.metaData.getString("DTM_UUID");
            AppManager.DTM_CURRENCY = appInfo.metaData.getString("DTM_CURRENCY");
            AppManager.DTM_STATE = appInfo.metaData.getString("DTM_STATE");
            AppManager.DTM_OPERATORS = appInfo.metaData.getString("DTM_OPERATORS");
            AppManager.DTM_OPERATORS_PHONE = appInfo.metaData.getString("DTM_OPERATORS_PHONE");
            AppManager.DTM_BOX_IN_CASH = appInfo.metaData.getInt("DTM_BOX_IN_CASH");
            AppManager.DTM_BOX_OUT_CASH = appInfo.metaData.getInt("DTM_BOX_OUT_CASH");
            BitOceanATMApp.getPublicKey();
        }

//        MobclickAgent.setDebugMode( true );
        UmengUpdateAgent.update(getContext());
        MobclickAgent.updateOnlineConfig(getContext());
        MobclickAgent.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
            @Override
            public void onDataReceived(JSONObject obj) {
                if (obj == null) {
                    return;
                }
                String value = null;
                try {
                    value = obj.getString("lock_" + AppManager.DTM_UUID);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (value != null && !"".equals(value))
                    serviceDTM = value;
            }
        });

        String value = MobclickAgent.getConfigParams(getContext(), "lock_" + AppManager.DTM_UUID);
        if (value != null && !"".equals(value))
            AppManager.serviceDTM = value;

//        //是否需要登录
//        if (AppManager.DTM_STATE.equals("JAPAN")) {
//            isLogined = false;
//        } else {
//            isLogined = true;
//        }
//
//        //是否需要kyc注册
//        if (AppManager.DTM_STATE.equals("JAPAN")) {
//            isKycRegister = false;
//        } else {
//            isKycRegister = true;
//        }

        if (pi != null) {
            versionCode = pi.versionCode;
            versionNameString = pi.versionName;
        }

        bitType.add("btc");
    }

    @Override
    public void DestroyManager()
    {
//        unbindService();
    }

    private void getAdminStauts() {
        isAdmin = mSharedPreferences.getBoolean("isadmin", false);
    }

    public static LoginUserStruct getUserStruct() {
        return userStruct;
    }

    public static String getUserId() {
        if (userStruct != null)
            return userStruct.user_idString;
        return null;
    }

    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

//    public void bindService() {
//        Intent intent = new Intent().setClass(getContext(), ATMService.class);
//        getContext().bindService(intent, mConnection, getContext().BIND_AUTO_CREATE);
//    }
//
//    public void unbindService() {
//        getContext().unbindService(mConnection);
//    }
//
//    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
//            // TODO Auto-generated method stub
//            mService = ((ATMService.LocalBinder) arg1).getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName arg0) {
//            // TODO Auto-generated method stub
//
//        }
//    };
}
