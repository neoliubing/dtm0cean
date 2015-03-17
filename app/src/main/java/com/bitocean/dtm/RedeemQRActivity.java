package com.bitocean.dtm;

import android.os.Bundle;

import com.bitocean.dtm.fragment.RedeemFlowDisplayFragment;

/**
 * @author bing.liu
 */
public class RedeemQRActivity extends BaseTimerActivity {
    private RedeemFlowDisplayFragment redeemFlowDisplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_qr);
        initUI();
    }

    private void initUI() {
        redeemFlowDisplayFragment = (RedeemFlowDisplayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.redeem_flow_display_fragment);
    }
}
