package com.bitocean.dtm;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.util.Util;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author bing.liu
 */
public class MainActivity extends BaseTimerActivity {
    private boolean isClick = false;
    private int clickNum = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"sh", "startservice", "-n", "com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "service call activity 42 s16 com.android.systemui"}); //WAS 79
            proc.waitFor();
        } catch (Exception ex) {
        }

        RelativeLayout clickLayout = (RelativeLayout) findViewById(R.id.click_layout);
        clickLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToTradeModeActivity();
            }
        });

        View logo = (View) findViewById(R.id.logo);
        logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                clickLogoSet();
            }
        });

        Button english_btn = (Button) findViewById(R.id.language_english_btn);
        english_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                changeLanguage("english");
            }
        });

        Button china_btn = (Button) findViewById(R.id.language_china_btn);
        china_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                changeLanguage("china");
            }
        });

        Button japan_btn = (Button) findViewById(R.id.language_japan_btn);
        japan_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                changeLanguage("japan");
            }
        });

        TextView phone_text = (TextView) findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }

    private void changeLanguage(String language) {
        Intent intent = new Intent(this, LanguageActivity.class);
        intent.putExtra("language", language);
        startActivity(intent);
        finish();
    }

    private void clickLogoSet() {
        if (isClick) {
            clickNum++;
            if (clickNum >= 7) {
                timer.cancel();
                isClick = false;
                clickNum = 0;
                goToSettingActivity();
                return;
            }
        } else {
            isClick = true;
            timer = new Timer();
            timer.schedule(new RemindTask(), 3 * 1000);
            clickNum++;
        }
    }

    class RemindTask extends TimerTask {
        public void run() {
            isClick = false;
            timer.cancel();
        }
    }

    private void goToTradeModeActivity() {
        if (AppManager.isLockDTM) {
            new Util(this).showFeatureToast(this
                    .getString(R.string.lock_dtm));
            return;
        }

        Intent intent = new Intent(this, TradeModeActivity.class);
        startActivity(intent);
    }

    private void goToSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
