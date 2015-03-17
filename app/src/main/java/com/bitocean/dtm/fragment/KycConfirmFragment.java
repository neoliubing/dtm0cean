package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.struct.RedeemConfirmStruct;
/**
 * @author bing.liu
 * 
 */
public class KycConfirmFragment extends NodeFragment {
	private LoginUserStruct struct;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		
		Bundle b = getArguments();
		if (b == null)
			return;
		struct = (LoginUserStruct) b.getSerializable("loginuserstruct");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_confirm, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
				.findViewById(R.id.view_text);
		titleTextView.setText(R.string.confirm);
		
		TextView textView = (TextView) v.findViewById(R.id.text);
		textView.setText(R.string.register_kyc_info);

		Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
				.findViewById(R.id.left_btn);
		cancelButton.setVisibility(View.INVISIBLE);

		Button nextButton = (Button) v.findViewById(R.id.bottom_button)
				.findViewById(R.id.right_btn);
        nextButton.setText(R.string.register_kyc);
        nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RegisterPhotoFragment fragment = new RegisterPhotoFragment();
				Bundle b = new Bundle();
				b.putSerializable("loginuserstruct", struct);
				fragment.setArguments(b);
				getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.setTransition(
						FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.add(R.id.container, fragment)
				.addToBackStack("kycconfirmfragment").commit();
			}
		});

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
	}
}
