package com.bitocean.dtm.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bitocean.dtm.R;
import com.bitocean.dtm.controller.AppManager;

/**
 * @author bing.liu
 */
public abstract class NodeFragment extends Fragment implements View.OnTouchListener {
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        AppManager.loopTimer = AppManager.LOOP_TIMER;
        return v;
    }

    public void onDestroyView(){
        super.onDestroyView();
        AppManager.loopTimer = AppManager.LOOP_TIMER;
    }

    public boolean onTouch(View v, MotionEvent event) {
//        if (getActivity().getCurrentFocus() == null)
//            return true;
//        if (getActivity().getCurrentFocus().getApplicationWindowToken() == null)
//            return true;
//        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        return false;
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
        super.onViewCreated(view, savedInstanceState);
    }
}
