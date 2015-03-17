package com.bitocean.dtm.fragment;

import u.aly.cv;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author bing.liu
 * 
 */
public class PayConfirmFragment extends NodeFragment {
	private String user_public_key = null;
    private String bit_num = null;
	private int process_event = 0;
	private int currency_num;
	private TextView currency_num_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Bundle b = getArguments();
		if (b == null)
			return;
		user_public_key = (String) b.getString("user_public_key");
		process_event = (int) b.getInt("process_event", 0);
		currency_num = (int) b.getInt("currency_num", 0);
        bit_num = (String) b.getString("bit_num");
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
		textView.setText(R.string.buy_pay_confirm_prompt);
        textView.setTextColor(Color.RED);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 75);

        TextView notesTextView = (TextView) v.findViewById(R.id.notes);
        notesTextView.setVisibility(View.VISIBLE);
        notesTextView.setText(R.string.buy_pay_confirm_notes);
        notesTextView.setTextColor(Color.RED);
		
		currency_num_text = (TextView)v.findViewById(R.id.currency_num_text);

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
        nextButton.setText(R.string.confirm);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BuyBillFragment fragment = new BuyBillFragment();
				Bundle b = new Bundle();
				b.putString("user_public_key", user_public_key);
				b.putInt("currency_num", currency_num);
				b.putInt("process_event", process_event);
                b.putString("bit_num", bit_num);
				fragment.setArguments(b);
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.add(R.id.container, fragment)
						.addToBackStack("payconfirmfragment").commit();
			}
		});

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
	}
}
