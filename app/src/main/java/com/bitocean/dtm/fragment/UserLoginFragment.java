package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.TradeModeActivity;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.util.Util;

import de.greenrobot.event.EventBus;
/**
 * @author bing.liu
 * 
 */
public class UserLoginFragment extends NodeFragment {
	private ProgressDialog progressDialog = null;
    private EditText nameEditText;
    private EditText passwordEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, ATMBroadCastEvent.class);
	}

	@Override
	public void onDestroy() {
		if(progressDialog != null)
			progressDialog.dismiss();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_user_login, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		nameEditText = (EditText) v.findViewById(R.id.name_edit);
        passwordEditText = (EditText) v
				.findViewById(R.id.password_edit);
		Button loginButton = (Button) v.findViewById(R.id.login_btn);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loginUser(nameEditText.getText().toString(), passwordEditText
						.getText().toString());
			}
		});

		TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
				.findViewById(R.id.view_text);
		titleTextView.setText(R.string.user_login_prompt);

		Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
				.findViewById(R.id.left_btn);
		cancelButton.setText(R.string.back);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});

		Button nextButton = (Button) v.findViewById(R.id.bottom_button)
				.findViewById(R.id.right_btn);
        nextButton.setText(R.string.register);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(AppManager.isKycRegister){
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, new RegisterPhotoFragment())
                            .addToBackStack("userlogin").commit();
                }else{
                    RegisterPhoneFragment fragment = new RegisterPhoneFragment();
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("registerpassportfragment")
                            .commit();
                }
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
	}

	private void loginUser(String user_id, String user_password) {
		if (user_id == null || user_id.equals("")) {
			new Util(getActivity()).showFeatureToast(getActivity().getString(
					R.string.admin_name_error));
			return;
		}

		if (user_password == null || user_password.equals("")) {
			new Util(getActivity()).showFeatureToast(getActivity().getString(
					R.string.admin_password_error));
			return;
		}
		
		if(!AppManager.isNetEnable){
			new Util(mContext).showFeatureToast(mContext
					.getString(R.string.network_error));
			return;
		}
		
		NetServiceManager.getInstance().loginUser(user_id, user_password);
		progressDialog = new Util(getActivity()).showProgressBar(getActivity()
				.getString(R.string.wait));
	}

	public void onEventMainThread(ATMBroadCastEvent event) {
		switch (event.getType()) {
		case ATMBroadCastEvent.EVENT_USER_LOGIN_SUCCESS:
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			LoginUserStruct struct = (LoginUserStruct) event.getObject();
			if ("success".equals(struct.resutlString)) {
                struct.user_idString = nameEditText.getText().toString();
                new Util(getActivity())
                        .showFeatureToast(R.string.login_success);
				goToTradeModeActivity(struct);
			} else if ("fail".equals(struct.resutlString)) {
				String msgString = null;
				switch (struct.reason) {
				case 1:
					msgString = getString(R.string.user_login_fail_1);
					break;
				case 2:
					msgString = getString(R.string.user_login_fail_2);
					break;
				case 3:
					msgString = getString(R.string.user_login_fail_3);
					break;
				default:
					break;
				}
				new Util(getActivity()).showFeatureToast(msgString);
			}
			break;
		case ATMBroadCastEvent.EVENT_USER_LOGIN_FAIL:
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			new Util(getActivity())
					.showFeatureToast(R.string.error_login);
			break;
		default:
			break;
		}
	}
	
	private void goToTradeModeActivity(LoginUserStruct struct){
        AppManager.userStruct = struct;
		if(("NO_KYC").equals(struct.userTypeString) && AppManager.isKycRegister){
			//如果用户没有kyc,但是当前需要kyc,走验证kyc流程
			KycConfirmFragment fragment = new KycConfirmFragment();
			Bundle b = new Bundle();
			b.putSerializable("loginuserstruct", struct);
			fragment.setArguments(b);
			
			getActivity()
			.getSupportFragmentManager()
			.beginTransaction()
			.setTransition(
					FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.add(R.id.container, fragment)
			.addToBackStack("userlogin").commit();
		}else{
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("loginuserstruct", struct);
			resultIntent.putExtras(bundle);
			getActivity().setResult(getActivity().RESULT_OK, resultIntent);
			getActivity().finish();	
		}
	}
}
