package com.gln.androidexplore.chapter1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.gln.androidexplore.BaseActivity;
import com.gln.androidexplore.R;

import java.util.Iterator;

/**
 * Created by guolina on 2017/6/21.
 */
public class IntentFilter1Activity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_changes);

        StringBuilder builder = new StringBuilder();
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getAction())) {
            builder.append(" action=" + intent.getAction());
        }
        if (!TextUtils.isEmpty(intent.getDataString())) {
            builder.append(" data=" + intent.getDataString());
        }

        if (intent.getCategories() != null && intent.getCategories().size() > 0) {
            builder.append(" category=[");
            Iterator ite = intent.getCategories().iterator();
            while (ite.hasNext()) {
                builder.append(ite.next() + ", ");
            }
            builder.append("]");
        }

        TextView textView = (TextView) findViewById(R.id.text_config_changes);
        textView.setText(builder.toString());
    }
}
