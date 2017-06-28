package com.gln.androidexplore.util;

import android.os.Environment;

import com.gln.androidexplore.MyApplication;

import java.io.File;

/**
 * Created by guolina on 2017/6/23.
 */
public class FileUtils {

    public static String getSDFilePath() {
        String path;
        if (hasExternalStorage()) {
            LogUtils.d("FileUtils", "externalStorage: true");
            path =  MyApplication.getContext().getExternalCacheDir() + File.separator + "gln_test";
        } else {
            path = MyApplication.getContext().getFilesDir() + File.separator + "gln_test";
        }
        return path;
    }

    private static boolean hasExternalStorage() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }
}
