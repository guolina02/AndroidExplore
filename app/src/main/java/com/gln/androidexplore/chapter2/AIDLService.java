package com.gln.androidexplore.chapter2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.gln.androidexplore.chapter2.aidl.BookManagerImpl;
import com.gln.androidexplore.chapter2.aidl.ComputeManagerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolina on 2017/6/23.
 */
public class AIDLService extends Service {

    private static final String TAG = AIDLService.class.getSimpleName();

    public static final int BINDER_BOOK = 21;
    public static final int BINDER_COMPUTE = 22;

    private Binder mBinderPool = new IBinderPool.Stub() {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_BOOK:
                    binder = new BookManagerImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeManagerImpl();
                    break;
            }
            return binder;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }
}
