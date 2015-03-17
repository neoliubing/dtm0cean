package com.bitocean.dtm;

import android.os.Bundle;

import com.bitocean.dtm.controller.AppManager;

/**
 * @author bing.liu
 *
 */
public class BaseTimerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.loopTimer = AppManager.LOOP_TIMER;
		AppManager.isLoopTime = true;
	}

    protected void onDestroy(){
        AppManager.loopTimer = AppManager.LOOP_TIMER;
        super.onDestroy();
    }
}
