package com.bitocean.dtm;

import com.bitocean.dtm.fragment.UserLoginFragment;
import com.bitocean.dtm.R;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * @author bing.liu
 */
public class UserActivity extends BaseTimerActivity {
    private UserLoginFragment UserLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initUI();
    }

    private void initUI() {
        UserLoginFragment = (UserLoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.user_login_fragment);
    }
}
