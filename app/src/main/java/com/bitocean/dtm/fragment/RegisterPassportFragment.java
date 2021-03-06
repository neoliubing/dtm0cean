package com.bitocean.dtm.fragment;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitocean.dtm.CameraActivity;
import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;
import com.bitocean.dtm.controller.NetServiceManager;
import com.bitocean.dtm.service.ATMBroadCastEvent;
import com.bitocean.dtm.struct.LoginUserStruct;
import com.bitocean.dtm.struct.RegisterStruct;
import com.bitocean.dtm.util.Util;
import com.google.common.io.Files;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class RegisterPassportFragment extends NodeFragment {
    private ProgressDialog progressDialog = null;
    private LoginUserStruct struct;
    private ImageView photoImageView;
    private Bitmap bitmap = null;
    private String filepath;
    private String userIconString;

    private String userIconStringBase64;
    private String passportStringBase64;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this, ATMBroadCastEvent.class);
        mContext = getActivity().getApplicationContext();
        Bundle b = getArguments();
        if (b == null)
            return;
        userIconString = (String) b.getSerializable("user_icon_url");
        struct = (LoginUserStruct) b.getSerializable("loginuserstruct");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.fragment_register_passport, null);
        initView(v);
        return v;
    }

    public void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        EventBus.getDefault().unregister(this);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }

    private void initView(View v) {
        TextView titleTextView = (TextView) v.findViewById(R.id.title_text)
                .findViewById(R.id.view_text);
        titleTextView.setText(R.string.register_passport_prompt);

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
        nextButton.setText(R.string.next);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (bitmap != null) {
                    if (struct != null) {
                        if (!AppManager.isNetEnable) {
                            new Util(mContext).showFeatureToast(mContext
                                    .getString(R.string.network_error));
                            return;
                        }

                        NetServiceManager.getInstance().registerUser(
                                struct.user_idString, null, null, null, userIconStringBase64, passportStringBase64);
                        progressDialog = new Util(getActivity())
                                .showProgressBar(getActivity().getString(
                                        R.string.wait));
                    } else {
                        RegisterPhoneFragment fragment = new RegisterPhoneFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("user_icon_url", userIconString);
                        b.putSerializable("passport_url", filepath);
                        fragment.setArguments(b);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(
                                        FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .add(R.id.container, fragment)
                                .addToBackStack("registerpassportfragment")
                                .commit();
                    }
                } else {
                    new Util(getActivity())
                            .showFeatureToast(R.string.register_passport_error);
                }
            }
        });

        Button photoButton = (Button) v.findViewById(R.id.photo_btn);
        photoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startCameraActivity();
            }
        });

        photoImageView = (ImageView) v.findViewById(R.id.photo_image);
        photoImageView.setEnabled(true);
        photoImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startCameraActivity();
            }
        });

        TextView phone_text = (TextView) v.findViewById(R.id.phone_text);
        phone_text.setText(getString(R.string.print_hot_line) + AppManager.DTM_OPERATORS_PHONE);

        File userIcon = new File(userIconString);
        byte[] byteUserIconData = new byte[0];
        try {
            byteUserIconData = Files.toByteArray(userIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File passport = new File(filepath);
        byte[] bytepassportData = new byte[0];
        try {
            bytepassportData = Files.toByteArray(passport);
        } catch (IOException e) {
            e.printStackTrace();
        }

        userIconStringBase64 = new String(Base64.encodeToString(byteUserIconData, Base64.NO_WRAP));
        passportStringBase64 = new String(Base64.encodeToString(bytepassportData, Base64.NO_WRAP));
    }

    private void startCameraActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("fileType", "passport");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                filepath = data.getStringExtra("url");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Config.RGB_565;
                options.inPurgeable = false;

                File passport = new File(filepath);

                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
                bitmap = BitmapFactory.decodeFile(passport.getAbsolutePath(),
                        options);
                photoImageView.setImageBitmap(bitmap);
                photoImageView.invalidate();
            }
        }
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
            default:
                break;
        }
    }

    private void deleteUserIconPassport() {
        new Thread(new deletePicture()).start();

    }

    class deletePicture implements Runnable {
        @Override
        public void run() {
            File userIcon = new File(userIconString);
            userIcon.delete();
            File passport = new File(filepath);
            passport.delete();
        }
    }
}
