/*
 * Copyright 2011-2014 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bitocean.dtm.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bitocean.dtm.MainActivity;
import com.bitocean.dtm.controller.AppManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author bing.liu
 */
public class ATMReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // make sure there is always an alarm scheduled
        AppManager.getInstance().atmReceiver = this;

        if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
            startATMService(context);
            startMainActivity(context);
        } else if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startATMService(context);
            startMainActivity(context);
        } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            checkStartATMService(context);
        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
                .getAction())) {
            checkNetworkStatus(context);
        }
    }

    private void startATMService(Context context) {
        context.startService(new Intent(context, ATMService.class));
    }

    private void startMainActivity(Context context) {
        if (AppManager.getInstance().isAdmin) {
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private void checkStartATMService(Context context) {
        boolean isServiceRunning = false;
        ActivityManager manager = (ActivityManager) AppManager.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if ("com.bitocean.dtm.service.ATMService".equals(service.service
                    .getClassName())) {
                isServiceRunning = true;
                break;
            }
        }
        if (!isServiceRunning) {
            Intent i = new Intent(context, ATMService.class);
            context.startService(i);
        }

//        if (!isTopActivity()) {
//            startMainActivity(context);
//        }
    }

    private void checkNetworkStatus(Context context) {
        EventBus.getDefault().post(
                new ATMBroadCastEvent(
                        ATMBroadCastEvent.EVENT_NETWORK_STATUS));
    }

    protected static boolean isTopActivity() {
        String packageName = "com.bitocean.dtm";
        ActivityManager activityManager = (ActivityManager) AppManager.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}