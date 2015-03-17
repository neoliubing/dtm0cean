package com.bitocean.dtm.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.RedeemQRActivity;
import com.bitocean.dtm.SellActivity;
import com.bitocean.dtm.TradeModeActivity;
import com.bitocean.dtm.UserActivity;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.util.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * @author bing.liu
 */
public class TradeModeFragment extends NodeFragment {
    Button nextButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_trade_mode, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        Button buyModeButton = (Button) v.findViewById(R.id.buy_mode_btn);
        buyModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToBuyModeActivity();
            }
        });

        Button sellModeButton = (Button) v.findViewById(R.id.sell_mode_btn);
        sellModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToSellModeActivity();
            }
        });

        Button qrModeButton = (Button) v.findViewById(R.id.redeem_mode_btn);
        qrModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToRedeemModeActivity();
            }
        });

        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.trade_mode);

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

        nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        if (AppManager.getUserStruct() == null)
            nextButton.setVisibility(View.INVISIBLE);
        else
            nextButton.setVisibility(View.VISIBLE);

        nextButton.setText(R.string.logout);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AppManager.userStruct = null;
                nextButton.setVisibility(View.INVISIBLE);
                new Util(mContext).showFeatureToast(R.string.logout_success);
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }

    private void goToBuyModeActivity() {
        String value = MobclickAgent.getConfigParams(getActivity(), "lock_" + AppManager.DTM_UUID);
        if (value != null && !"".equals(value))
            AppManager.serviceDTM = value;

        if ("off".equals(AppManager.serviceDTM)) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.lock_dtm));
            return;
        }

        if (AppManager.isLockDTM) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.lock_dtm));
            return;
        }

        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        if (!AppManager.DTM_CURRENCY.equals(AppManager.typeRateStructs.currency_typeString)) {
            new Util(mContext).showFeatureToast(R.string.currency_fail);
            return;
        }

        if (AppManager.typeRateStructs.rateStructs.size() == 0) {
            new Util(mContext).showFeatureToast(R.string.rate_fail);
            return;
        }

        if (AppManager.getUserStruct() != null || !AppManager.isLogined) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(
                            FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container, new BuyModeFragment())
                    .addToBackStack("trademode").commit();
        } else {
            Intent intent = new Intent(getActivity(),
                    UserActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    private void goToSellModeActivity() {
        String value = MobclickAgent.getConfigParams(getActivity(), "lock_" + AppManager.DTM_UUID);
        if (value != null && !"".equals(value))
            AppManager.serviceDTM = value;

        if ("off".equals(AppManager.serviceDTM)) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.lock_dtm));
            return;
        }

        if (AppManager.isLockDTM) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.lock_dtm));
            return;
        }

        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        if (!AppManager.DTM_CURRENCY.equals(AppManager.typeRateStructs.currency_typeString)) {
            new Util(mContext).showFeatureToast(R.string.currency_fail);
            return;
        }

        if (AppManager.typeRateStructs.rateStructs.size() == 0) {
            new Util(mContext).showFeatureToast(R.string.rate_fail);
            return;
        }

        if (AppManager.getUserStruct() != null || !AppManager.isLogined) {
            Intent intent = new Intent(getActivity(), SellActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    private void goToRedeemModeActivity() {
        String value = MobclickAgent.getConfigParams(getActivity(), "lock_" + AppManager.DTM_UUID);
        if (value != null && !"".equals(value))
            AppManager.serviceDTM = value;

        if ("off".equals(AppManager.serviceDTM)) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.lock_dtm));
            return;
        }

        if (AppManager.isLockDTM) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.lock_dtm));
            return;
        }

        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        if (!AppManager.DTM_CURRENCY.equals(AppManager.typeRateStructs.currency_typeString)) {
            new Util(mContext).showFeatureToast(R.string.currency_fail);
            return;
        }
        Intent intent = new Intent(getActivity(), RedeemQRActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bundle bundle = data.getExtras();
                LoginUserStruct struct = (LoginUserStruct) bundle.getSerializable("loginuserstruct");
                AppManager.userStruct = struct;
                nextButton.setVisibility(View.VISIBLE);
            }
        }
    }
}