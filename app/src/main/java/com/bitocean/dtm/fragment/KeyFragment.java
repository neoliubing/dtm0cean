package com.bitocean.dtm.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitocean.dtm.DownloadApkActivity;
import com.bitocean.dtm.InfoActivity;
import com.bitocean.dtm.R;
import com.bitocean.dtm.SettingActivity;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.db.DBTools;
import com.bitocean.dtm.struct.LoginAdminStruct;
import com.bitocean.dtm.util.Util;

/**
 * @author bing.liu
 */
public class KeyFragment extends NodeFragment {
    private LoginAdminStruct struct = null;
    private Button lock_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        Bundle b = getArguments();
        if (b == null)
            return;
        struct = (LoginAdminStruct) b.getSerializable("loginadminstruct");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_key, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.key_prompt);

        TextView keyTextView = (TextView) v.findViewById(R.id.key_info);
        if (struct != null) {
            keyTextView.setText(struct.public_keyString);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dtm", getActivity().MODE_PRIVATE); //私有数据
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("public_key", struct.public_keyString);
            editor.commit();//提交修改
            AppManager.public_keyString = struct.public_keyString;
        }

        TextView updateTextView = (TextView) v.findViewById(R.id.update_info);
        if (struct != null)
            updateTextView.setText(struct.update_infoString);

        if (struct.update_linkString.equals("") || struct.update_linkString == null) {
            RelativeLayout update_layout = (RelativeLayout) v.findViewById(R.id.update_layout);
            update_layout.setVisibility(View.INVISIBLE);
        }

        Button cancelButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.left_btn);
        cancelButton.setVisibility(View.INVISIBLE);

        Button nextButton = (Button) v.findViewById(R.id.bottom_button)
                .findViewById(R.id.right_btn);
        nextButton.setText(R.string.logout);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getActivity().finish();
            }
        });

        lock_btn = (Button) v.findViewById(R.id.lock_btn);
        if (AppManager.isLockDTM)
            lock_btn.setText(R.string.unlock);
        else
            lock_btn.setText(R.string.lock);

        lock_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (AppManager.isLockDTM) {
                    AppManager.isLockDTM = false;
                    lock_btn.setText(R.string.lock);
                } else {
                    AppManager.isLockDTM = true;
                    lock_btn.setText(R.string.unlock);
                }
            }
        });

        Button updateButton = (Button) v.findViewById(R.id.update_btn);
        updateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                downloadApk();
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);

        Button buyButton = (Button) v.findViewById(R.id.buy_btn);
        buyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToInfoActivity(0);
            }
        });

        Button sellButton = (Button) v.findViewById(R.id.sell_btn);
        sellButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToInfoActivity(1);
            }
        });

        Button incashButton = (Button) v.findViewById(R.id.incash_btn);
        incashButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToInfoActivity(2);
            }
        });

        Button outcashButton = (Button) v.findViewById(R.id.outcash_btn);
        outcashButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToInfoActivity(3);
            }
        });

        Button deviceButton = (Button) v.findViewById(R.id.device_btn);
        deviceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                goToInfoActivity(4);
            }
        });
    }

    private void downloadApk() {
        if (struct.update_linkString.equals("") || struct.update_linkString == null)
            return;
        downloadApk(struct.update_linkString);
    }

    private void downloadApk(String link) {
        Intent intent = new Intent(getActivity(), DownloadApkActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    private void goToInfoActivity(int type) {
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
