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
import com.bitocean.dtm.service.ProcessEvent;

/**
 * @author bing.liu
 */
public class SellFlowDisplayFragment extends NodeFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_sell_flow_display, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.sell_flow_display);

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
        nextButton.setText(R.string.next);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//                ScanUserKeyFragment fragment = new ScanUserKeyFragment();
//                Bundle b = new Bundle();
//                b.putInt("process_event", ProcessEvent.EVENT_SELL);
//                fragment.setArguments(b);
//                getActivity()
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .setTransition(
//                                FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .add(R.id.container, fragment)
//                        .addToBackStack("sellflowdisplayfragment").commit();
                CurrencyCountFragment fragment = new CurrencyCountFragment();
                Bundle b = new Bundle();
                b.putInt("process_event", ProcessEvent.EVENT_SELL);
                fragment.setArguments(b);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(
                                FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.container, fragment)
                        .addToBackStack("sellflowdisplayfragment").commit();
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);
    }
}
