package com.bitocean.dtm;

import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.fragment.WifiSettingFragment;
import com.bitocean.dtm.R;
import com.bitocean.dtm.service.UpdateReceiver;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * @author bing.liu
 */
public class SettingActivity extends BaseActivity {
    private WifiSettingFragment wifiFragment;
    private UpdateReceiver updateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateReceiver = new UpdateReceiver();
        AppManager.isLoopTime = false;
        /** register download success broadcast **/
        registerReceiver(updateReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        setContentView(R.layout.activity_setting);
        initUI();
    }

    private void initUI() {
        wifiFragment = (WifiSettingFragment) getSupportFragmentManager()
                .findFragmentById(R.id.wifi_fragment);
    }

    @Override
    protected void onDestroy() {
        AppManager.isLoopTime = true;
        unregisterReceiver(updateReceiver);
        super.onDestroy();
    }
}
