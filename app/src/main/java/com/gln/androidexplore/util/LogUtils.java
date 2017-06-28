package com.gln.androidexplore.util;

import android.util.Log;

/**
 * Created by guolina on 2017/6/22.
 */
public class LogUtils {

    public static final int LEVEL_VERBOSE = 5;
    public static final int LEVEL_DEBUG = 4;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_WARN = 2;
    public static final int LEVEL_ERROR = 1;
    public static final int LEVEL_NO_LOG = 0;

    public static int level = LEVEL_NO_LOG;

    public static void v(String tag, String msg) {
        if (level >= LEVEL_VERBOSE) {
            Log.v(tag, "gln " + tag + " " + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level >= LEVEL_DEBUG) {
            Log.d(tag, "gln " + tag + " " + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level >= LEVEL_INFO) {
            Log.i(tag, "gln " + tag + " " + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level >= LEVEL_WARN) {
            Log.w(tag, "gln " + tag + " " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level >= LEVEL_ERROR) {
            Log.e(tag, "gln " + tag + " " + msg);
        }
    }
}
