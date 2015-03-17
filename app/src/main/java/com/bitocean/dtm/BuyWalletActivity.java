package com.bitocean.dtm;

import com.bitocean.dtm.fragment.BuyWalletFlowDisplayFragment;
import com.bitocean.dtm.R;

import android.os.Bundle;
/**
 * @author bing.liu
 *
 */
public class BuyWalletActivity extends BaseTimerActivity {
	private BuyWalletFlowDisplayFragment buyWalletFlowDisplayFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_wallet);
		initUI();
	}

	private void initUI() {
		buyWalletFlowDisplayFragment = (BuyWalletFlowDisplayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.buy_wallet_flow_display_fragment);
	}
}
