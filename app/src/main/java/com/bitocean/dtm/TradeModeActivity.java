package com.bitocean.dtm;

import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.fragment.TradeModeFragment;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.R;
import com.bitocean.dtm.struct.RegisterStruct;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author bing.liu
 */
public class TradeModeActivity extends BaseTimerActivity {
    private TradeModeFragment tradeModeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        initUI();
    }

    private void initUI() {
        tradeModeFragment = (TradeModeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.trade_mode_fragment);
    }
}
