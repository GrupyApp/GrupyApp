package com.grupy.grupy.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.UserProvider;

import java.util.List;

public class ViewedMessageHelper {

    public static void updateOnline(boolean status, final Context context) {
        UserProvider userProvider = new UserProvider();
        AuthProvider authProvider = new AuthProvider();
        if (authProvider != null) {
            if (isApplicationSentToBackground(context)) {
                userProvider.updateOnline(authProvider.getUid(), status);
            }
            else if (status) {
                userProvider.updateOnline(authProvider.getUid(), status);
            }
        }
    }

    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if(!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
