package com.bitocean.dtm.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.struct.SellBitcoinQRStruct;
import com.bitocean.dtm.util.Util;

/**
 * @author bing.liu
 */
public class SellScanQRCodeFragment extends NodeFragment {
    private SellBitcoinQRStruct struct = null;
//    private String user_public_key = null;
    private String bit_num = null;
    private int currency_num = 0;
    private int process_event = 0;
    private String path;
    private ImageView qr_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        Bundle b = getArguments();
        if (b == null)
            return;
        struct = (SellBitcoinQRStruct) b.getSerializable("sellbitcoinqrstruct");
//        user_public_key = (String) b.getString("user_public_key");
        currency_num = (int) b.getInt("currency_num", 0);
        process_event = (int) b.getInt("process_event", 0);
        bit_num = (String) b.getString("bit_num");
        return;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_sell_scan_qr_code, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        if (struct == null)
            return;

        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.sell_qr_code);

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
        if (struct != null)
            qr_image.setImageBitmap(Util.generateQRCode(struct.bitcoin_qr));

        if (struct.quota_num == 0) {
            TextView large_prompt_text = (TextView) v
                    .findViewById(R.id.large_prompt_text);
            large_prompt_text.setVisibility(View.VISIBLE);
        }

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.sell_btn);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SellConfirmWaitFragment fragment = new SellConfirmWaitFragment();
                Bundle b = new Bundle();
//                b.putString("user_public_key", user_public_key);
                b.putString("bitcoin_qr", struct.bitcoin_qr);
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
                        .addToBackStack("sellscanqrcodefragment").commit();
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }
}
