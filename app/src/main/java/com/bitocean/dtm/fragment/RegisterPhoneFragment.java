package com.bitocean.dtm.fragment;

import java.io.File;
import java.util.Locale;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.struct.RegisterStruct;
import com.bitocean.dtm.struct.VerifyCodeStruct;
import com.bitocean.dtm.util.Util;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class RegisterPhoneFragment extends NodeFragment {
    private ProgressDialog progressDialog = null;
    private String userIconString = null;
    private String passportString = null;
    private EditText nameEditText, passwordEditText, phoneEditText, verifyEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this, ATMBroadCastEvent.class);
        Bundle b = getArguments();
        if (b == null)
            return;
        userIconString = (String) b.getSerializable("user_icon_url");
        passportString = (String) b.getSerializable("passport_url");
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_register_phone, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        nameEditText = (EditText) v.findViewById(R.id.name_edit);
        passwordEditText = (EditText) v
                .findViewById(R.id.password_edit);
        phoneEditText = (EditText) v.findViewById(R.id.phone_edit);
        verifyEditText = (EditText) v
                .findViewById(R.id.verify_edit);

        Button verifyButton = (Button) v.findViewById(R.id.verify_btn);
        verifyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getVerifyCode(phoneEditText.getText().toString());
            }
        });

        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.register_phone_prompt);

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
        nextButton.setText(R.string.register);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                registerUser(nameEditText.getText().toString(), phoneEditText.getText().toString(),
                        passwordEditText.getText().toString(), verifyEditText
                                .getText().toString());
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }

    private void deleteUserIconPassport() {
        new Thread(new deletePicture()).start();

    }

    class deletePicture implements Runnable {
        @Override
        public void run() {
            if (userIconString != null) {
                File userIcon = new File(userIconString);
                userIcon.delete();
            }
            if (passportString != null) {
                File passport = new File(passportString);
                passport.delete();
            }
        }
    }

    private void registerUser(String user_id, String user_password, String phone,
                              String verifyCode) {
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

        if (verifyCode == null || verifyCode.equals("")) {
            new Util(getActivity()).showFeatureToast(getActivity().getString(
                    R.string.verify_set));
            return;
        }

        if (phone == null || phone.equals("")) {
            new Util(getActivity()).showFeatureToast(getActivity().getString(
                    R.string.phone_set));
            return;
        }


        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        progressDialog = new Util(getActivity()).showProgressBar(getActivity()
                .getString(R.string.wait));
        NetServiceManager.getInstance().registerUser(user_id, user_password, phone,
                verifyCode, userIconString, passportString);
    }

    private void getVerifyCode(String phone) {
        if (phone == null || phone.equals("")) {
            new Util(getActivity()).showFeatureToast(getActivity().getString(
                    R.string.register_phone_error));
            return;
        }

        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        String country_code = null;
        if ("en".equals(config.locale.toString())) {
            country_code = "1";
        } else if ("zh_CN".equals(config.locale.toString())) {
            country_code = "86";
        } else if ("ja_JP".equals(config.locale.toString())) {
            country_code = "81";
        }

        NetServiceManager.getInstance().verifyCode(country_code, phone);
        progressDialog = new Util(getActivity()).showProgressBar(getActivity()
                .getString(R.string.wait));
    }

    public void onEventMainThread(ATMBroadCastEvent event) {
        switch (event.getType()) {
            case ATMBroadCastEvent.EVENT_USER_REGISTER_SUCCESS:
                deleteUserIconPassport();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }

                RegisterStruct struct = (RegisterStruct) event.getObject();
                if ("success".equals(struct.resutlString)) {
                    ConfirmFragment fragment = new ConfirmFragment();
                    Bundle b = new Bundle();
                    b.putString("reason", getString(R.string.register_success));
                    fragment.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("registerphonefragment").commit();
                } else if ("fail".equals(struct.resutlString)) {
                    String msgString = null;
                    switch (struct.reason) {
                        case 1:
                            msgString = getString(R.string.register_fail_1);
                            break;
                        case 2:
                            msgString = getString(R.string.register_fail_2);
                            break;
                        case 3:
                            msgString = getString(R.string.register_fail_3);
                            break;
                        default:
                            break;
                    }
                    new Util(getActivity()).showFeatureToast(msgString);
                }
                break;
            case ATMBroadCastEvent.EVENT_USER_REGISTER_FAIL:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }

                new Util(getActivity())
                        .showFeatureToast(R.string.error_register);
                break;
            case ATMBroadCastEvent.EVENT_VERIFY_CODE_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                VerifyCodeStruct vStruct = (VerifyCodeStruct) event.getObject();
                if ("success".equals(vStruct.resutlString)) {
                    new Util(getActivity()).showFeatureToast(getActivity()
                            .getString(R.string.send_verify_success));
                } else if ("fail".equals(vStruct.resutlString)) {
                    new Util(getActivity())
                            .showFeatureToast(getString(R.string.error_verify));
                }
                break;
            case ATMBroadCastEvent.EVENT_VERIFY_CODE_FAIL:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                new Util(getActivity())
                        .showFeatureToast(R.string.error_verify);
                break;
            default:
                break;
        }
    }
}
