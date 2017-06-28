package com.gln.androidexplore.chapter1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gln.androidexplore.BaseActivity;
import com.gln.androidexplore.R;

/**
 * Created by guolina on 2017/6/21.
 */
public class Chapter1Activity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_config_changes:
                intent.setClass(this, ConfigChangeActivity.class);
                break;
            case R.id.btn_without_config:
                intent.setClass(this, WithoutConfigActivity.class);
                break;
            case R.id.btn_intent_filter:
//                intent.setDataAndType(Uri.parse("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1498113627&di=9249901cb31a82147004a4891344362e&src=http://pic19.nipic.com/20120308/7491614_141057681000_2.png"), "image/png");
                intent.setAction("com.gln.chapter1.INTENTFILTER2");
//                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("com.gln.chapter1.category.b");
                break;
        }
        startActivity(intent);
    }
}
