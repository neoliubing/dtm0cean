package com.bitocean.dtm;

import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.hardware.KS80TSDKManager;
import com.bitocean.dtm.hardware.L1000SDKManager;
import com.bitocean.dtm.hardware.UBASDKManager;
import com.bitocean.dtm.util.Util;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * @author bing.liu
 */
public class LanguageActivity extends BaseTimerActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initUI();
    }

    private void initUI() {
        String language = getIntent().getStringExtra("language");

        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        if("english".equals(language)){
            config.locale = Locale.ENGLISH;
        }else if("china".equals(language)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else if("japan".equals(language)){
            config.locale = Locale.JAPAN;
        }
        resources.updateConfiguration(config, null);

        TextView phone_text = (TextView) findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
        startActivity();
    }

    private void startActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        finish();
    }
}