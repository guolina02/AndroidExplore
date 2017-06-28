package com.gln.androidexplore.chapter1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.gln.androidexplore.BaseActivity;
import com.gln.androidexplore.R;
import com.gln.androidexplore.util.LogUtils;

/**
 * Created by guolina on 2017/6/21.
 *
 * 不设置activity的configChanges的生命周期
 */
public class WithoutConfigActivity extends BaseActivity {

    private static final String TAG = WithoutConfigActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_changes);

        TextView textView = (TextView) findViewById(R.id.text_config_changes);
        textView.setText(TAG);

        LogUtils.d(TAG, ":onCreate:saveInstanceState[" + savedInstanceState + "]");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, ":onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, ":onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG, ":onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, ":onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, ":onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, ":onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d(TAG, ":onSaveInstanceState[" + outState + "]");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.d(TAG, ":onRestoreInstanceState[" + savedInstanceState + "]");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, ":onConfigurationChanged[" + newConfig + "]");
    }
}
