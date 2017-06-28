package com.gln.androidexplore;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by guolina on 2017/6/15.
 */
public class ActivityManager {

    private static final ArrayList<Activity> sActivityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    public static void removeAllActivities() {
        for (Activity activity: sActivityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }

        sActivityList.clear();
    }
}
