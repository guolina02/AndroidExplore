package com.gln.androidexplore.chapter2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.gln.androidexplore.BaseActivity;
import com.gln.androidexplore.R;
import com.gln.androidexplore.chapter2.model.Book;
import com.gln.androidexplore.util.CommExtras;
import com.gln.androidexplore.util.FileUtils;
import com.gln.androidexplore.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolina on 2017/6/22.
 */
public class Chapter2Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = Chapter2Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ipc_file) {
            List<String> permissions = new ArrayList<>();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() == 0) {
                String filePath = FileUtils.getSDFilePath();
                String fileName = "books.txt";
                saveFile(filePath, fileName);
            } else {
                String[] per = new String[permissions.size()];
                ActivityCompat.requestPermissions(this, permissions.toArray(per), 0);
            }
        } else {
            Intent intent = new Intent(this, IPCSecondActivity.class);
            switch (v.getId()) {
                case R.id.btn_ipc_bundle:
                    Bundle bundle = new Bundle();
                    Book book = new Book("BundleBook", 10.98f);
                    bundle.putParcelable(CommExtras.KEY_BOOK, book);
                    LogUtils.d(TAG, " BundleBookSource: " + book);

                    intent.putExtra(CommExtras.KEY_IPC_WAY, CommExtras.KEY_IPC_BUNDLE);
                    intent.putExtra(CommExtras.KEY_IPC_BUNDLE, bundle);
                    break;
                case R.id.btn_ipc_messenger:
                    intent.putExtra(CommExtras.KEY_IPC_WAY, CommExtras.KEY_IPC_MESSENGER);
                    break;
                case R.id.btn_ipc_aidl:
                    intent.putExtra(CommExtras.KEY_IPC_WAY, CommExtras.KEY_IPC_AIDL);
                    break;
                case R.id.btn_ipc_socket:
                    intent.putExtra(CommExtras.KEY_IPC_WAY, CommExtras.KEY_IPC_SOCKET);
                    break;
                case R.id.btn_ipc_contentProvider:
                    intent.putExtra(CommExtras.KEY_IPC_WAY, CommExtras.KEY_IPC_CONTENT_PROVIDER);
                    break;
            }
            startActivity(intent);
        }
    }

    private void saveFile(String filePath, String fileName) {
        File pathFile = new File(filePath);
        if (!pathFile.exists()) {
            LogUtils.d(TAG, "file:" + filePath + ", mkdirs: " + pathFile.mkdirs());
        }

        File file = new File(filePath + File.separator + fileName);
        if (!file.exists()) {
            try {
                LogUtils.d(TAG, "file:" + file.getPath() + ", createNewFile: " + file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Book book = new Book("FileBook1", 20.18f, "glnaa", "dianzigongyedaxue");
        Book book1 = new Book("FileBook2", 19.80f, "glnbb", "zhongguogongxinjituan");

        LogUtils.d(TAG, "FileBookSource: " + book);
        LogUtils.d(TAG, "FileBookSource: " + book1);

        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(book);
            outputStream.writeObject(book1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Intent intent = new Intent(this, IPCSecondActivity.class);
        intent.putExtra(CommExtras.KEY_IPC_WAY, CommExtras.KEY_IPC_FILE);
        intent.putExtra(CommExtras.KEY_IPC_FILE, filePath + File.separator + fileName);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            boolean result = true;
            if (grantResults != null && grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        result = false;
                    }
                }
            } else {
                result = false;
            }

            if (result) {
                String filePath = FileUtils.getSDFilePath();
                String fileName = "books.txt";
                saveFile(filePath, fileName);
            } else {
                LogUtils.d(TAG, "permission denied!");
            }
        }
    }
}
