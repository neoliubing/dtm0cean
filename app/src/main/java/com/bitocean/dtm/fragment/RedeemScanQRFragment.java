package com.bitocean.dtm.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitocean.dtm.QrCaptureActivity;
import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.struct.RedeemConfirmStruct;
import com.bitocean.dtm.util.Util;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class RedeemScanQRFragment extends NodeFragment {
    private ProgressDialog progressDialog = null;
    private ImageView qr_image;
    private String redeemInfoString = null;

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
        View v = mInflater.inflate(R.layout.fragment_redeem_qr, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.check_redeem_code);

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

        qr_image = (ImageView) v.findViewById(R.id.qr_image);
        qr_image.setEnabled(true);
        qr_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToQrCaptureActivity();
            }
        });

        Button qrButton = (Button) v.findViewById(R.id.qr_btn);
        qrButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToQrCaptureActivity();
            }
        });

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.next);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                redeemConfirm(redeemInfoString);
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }

    private void goToQrCaptureActivity() {
        startActivityForResult(new Intent(getActivity(), QrCaptureActivity.class), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bundle bundle = data.getExtras();
                redeemInfoString = bundle.getString("result");
                // 显示
                qr_image.setImageBitmap((Bitmap) data
                        .getParcelableExtra("bitmap"));
            }
        }
    }

    private void redeemConfirm(String redeemString) {
        if (!AppManager.isNetEnable) {
            new Util(mContext).showFeatureToast(mContext
                    .getString(R.string.network_error));
            return;
        }

        if (redeemString == null) {
            new Util(getActivity()).showFeatureToast(getString(R.string.check_redeem_code));
            return;
        }


        int index = redeemString.indexOf("+");
        String redeem_code = redeemString.substring(0, index);
        String txid = redeemString.substring(index+1);

        NetServiceManager.getInstance().redeemConfirm(redeem_code, txid);
        progressDialog = new Util(getActivity()).showProgressBar(getActivity()
                .getString(R.string.wait));
    }

    public void onEventMainThread(ATMBroadCastEvent event) {
        switch (event.getType()) {
            case ATMBroadCastEvent.EVENT_REDEEM_CONFIRM_SUCCESS:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                RedeemConfirmStruct struct = (RedeemConfirmStruct) event
                        .getObject();
                if ("success".equals(struct.resutlString)) {
                    RedeemSuccessFragment fragment = new RedeemSuccessFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("redeemconfirmstruct", struct);
                    fragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("redeemconfirmfragment").commit();
                } else if ("fail".equals(struct.resutlString)) {
                    String msgString = null;
                    switch (struct.reason) {
                        case 1:
                            msgString = getString(R.string.redeem_fail_1);
                            break;
                        case 2:
                            msgString = getString(R.string.redeem_fail_2);
                            break;
                        case 3:
                            msgString = getString(R.string.redeem_fail_3);
                            break;
                        default:
                            break;
                    }
                    ConfirmFragment fragment = new ConfirmFragment();
                    Bundle b = new Bundle();
                    b.putString("reason", msgString);
                    fragment.setArguments(b);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .add(R.id.container, fragment)
                            .addToBackStack("redeemconfirmfragment").commit();
                }
                break;
            case ATMBroadCastEvent.EVENT_REDEEM_CONFIRM_FAIL:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                new Util(getActivity())
                        .showFeatureToast(R.string.error_redeem);
                break;
            default:
                break;
        }
    }
}
