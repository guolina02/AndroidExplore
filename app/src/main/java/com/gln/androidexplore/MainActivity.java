package com.gln.androidexplore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gln.androidexplore.chapter1.Chapter1Activity;
import com.gln.androidexplore.chapter2.Chapter2Activity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_chapter1:
                intent.setClass(this, Chapter1Activity.class);
                break;
            case R.id.btn_chapter2:
                intent.setClass(this, Chapter2Activity.class);
                break;
        }
        startActivity(intent);
    }
}
