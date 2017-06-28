package com.gln.androidexplore.chapter2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.gln.androidexplore.chapter2.model.Book;
import com.gln.androidexplore.util.CommExtras;
import com.gln.androidexplore.util.LogUtils;

/**
 * Created by guolina on 2017/6/23.
 */
public class MessengerService extends Service {

    private static final String TAG = MessengerService.class.getSimpleName();

    public static final int MSG_FROM_CLIENT = 101;
    public static final int MSG_TO_CLIENT = 102;

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    Bundle bundle1 = msg.getData();
                    // 这一句是必须的，否则会报android.os.BadParcelableException: ClassNotFoundException when unmarshalling
                    // 这是因为他们的ClassLoader不同，
                    // BookClassLoader: dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.gln.androidexplore-1/base.apk"],nativeLibraryDirectories=[/data/app/com.gln.androidexplore-1/lib/arm, /vendor/lib, /system/lib]]]
                    // BundleClassLoader: java.lang.BootClassLoader@be3fc03
                    bundle1.setClassLoader(Book.class.getClassLoader());
                    LogUtils.d(TAG, "MessengerBookFrom: " + bundle1.getString(CommExtras.KEY_MSG));
                    Book book = bundle1.getParcelable(CommExtras.KEY_BOOK);
                    LogUtils.d(TAG, "MessengerBookFrom: " + book);

                    Messenger reply = msg.replyTo;
                    if (reply != null) {
                        Message msg1 = Message.obtain(null, MSG_TO_CLIENT);
                        Bundle bundle = new Bundle();
                        bundle.putString(CommExtras.KEY_MSG, "I have received the book message, thanks.");
                        msg1.setData(bundle);
                        try {
                            reply.send(msg1);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
