package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitocean.dtm.BuyQRActivity;
import com.bitocean.dtm.BuyWalletActivity;
import com.bitocean.dtm.R;
import com.bitocean.dtm.TradeModeActivity;
import com.bitocean.dtm.controller.AppManager;

/**
 * @author bing.liu
 * 
 */
public class BuyModeFragment extends NodeFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_buy_mode, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		Button buyQRButton = (Button) v.findViewById(R.id.buy_qr);
		buyQRButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goToBuyQRActivity();
			}
		});

		Button buyWalletButton = (Button) v.findViewById(R.id.buy_wallet);
		buyWalletButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goToBuyWalletActivity();
			}
		});


		TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
				.findViewById(R.id.view_text);
		titleTextView.setText(R.string.buy_prompt);

		Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
				.findViewById(R.id.left_btn);
		cancelButton.setText(R.string.back);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		Button nextButton = (Button) v.findViewById(R.id.bottom_button)
				.findViewById(R.id.right_btn);
		nextButton.setVisibility(View.INVISIBLE);

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
	}

	private void goToBuyQRActivity() {
		Intent intent = new Intent(getActivity(), BuyQRActivity.class);
		startActivity(intent);
	}
	
	private void goToBuyWalletActivity() {
		Intent intent = new Intent(getActivity(), BuyWalletActivity.class);
		startActivity(intent);
	}
}
