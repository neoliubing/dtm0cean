package com.bitocean.dtm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.hardware.KS80TSDKManager;
import com.bitocean.dtm.hardware.L1000SDKManager;
import com.bitocean.dtm.hardware.L1000SDKManager.onDispenseListener;
import com.bitocean.dtm.struct.RedeemConfirmStruct;
import com.bitocean.dtm.util.Util;

/**
 * @author bing.liu
 */
public class RedeemSuccessFragment extends NodeFragment {
    private RedeemConfirmStruct struct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        Bundle b = getArguments();
        if (b == null)
            return;
        struct = (RedeemConfirmStruct) b.getSerializable("redeemconfirmstruct");
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
        titleTextView.setText(R.string.complete_trans);

        TextView textView = (TextView) v.findViewById(R.id.text);
        if (struct != null)
            textView.setText(getString(R.string.redeem_confirm_text) + struct.currency_num + AppManager.DTM_CURRENCY);

        Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.left_btn);
        cancelButton.setVisibility(View.INVISIBLE);

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.out);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getActivity().finish();
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);

        Util.out_cash(struct.currency_num);
        Util.printSellCoin(String.valueOf(struct.currency_num));
        new Util(getActivity()).recordRedeem(struct.currency_num+"");
    }
}
