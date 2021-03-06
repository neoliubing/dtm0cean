package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.struct.LoginAdminStruct;
import com.bitocean.dtm.util.Util;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class AdminLoginFragment extends NodeFragment {
    private ProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this, ATMBroadCastEvent.class);
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
        View v = mInflater.inflate(R.layout.fragment_admin, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        final EditText nameEditText = (EditText) v.findViewById(R.id.name_edit);
        final EditText passwordEditText = (EditText) v
                .findViewById(R.id.password_edit);

        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.admin_prompt);

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
        nextButton.setText(R.string.login);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                loginAdmin(nameEditText.getText().toString(), passwordEditText
                        .getText().toString());
            }
        });
        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }

    private void loginAdmin(String admin_id, String admin_password) {
        if (admin_id == null || admin_id.equals("")) {
            new Util(getActivity()).showFeatureToast(getActivity().getString(
                    R.string.admin_name_error));
            return;
        }

        if (admin_password == null || admin_password.equals("")) {
            new Util(getActivity()).showFeatureToast(getActivity().getString(
                    R.string.admin_password_error));
            return;
        }
        NetServiceManager.getInstance().loginAdmin(admin_id, admin_password);
        progressDialog = new Util(getActivity()).showProgressBar(getActivity()
                .getString(R.string.wait));
    }

    public void onEventMainThread(ATMBroadCastEvent event) {
        switch (event.getType()) {
            case ATMBroadCastEvent.EVENT_ADMIN_LOGIN_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                LoginAdminStruct struct = (LoginAdminStruct) event.getObject();
                if ("success".equals(struct.resutlString)) {
                    KeyFragment keyFragment = new KeyFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("loginadminstruct", struct);
                    keyFragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, keyFragment)
                            .addToBackStack("adminfragment").commit();
                } else if ("fail".equals(struct.resutlString)) {
                    String msgString = null;
                    switch (struct.reason) {
                        case 1:
                            msgString = getString(R.string.admin_login_fail_1);
                            break;
                        case 2:
                            msgString = getString(R.string.admin_login_fail_2);
                            break;
                        case 3:
                            msgString = getString(R.string.admin_login_fail_3);
                            break;
                        default:
                            break;
                    }
                    new Util(getActivity()).showFeatureToast(msgString);
                }
                break;
            case ATMBroadCastEvent.EVENT_ADMIN_LOGIN_FAIL:
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
}
