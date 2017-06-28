package com.gln.androidexplore.chapter2.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.gln.androidexplore.chapter2.model.Compute;
import com.gln.androidexplore.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolina on 2017/6/23.
 */
public class ComputeManagerImpl extends Binder implements IComputeManager {

    private static final String TAG = ComputeManagerImpl.class.getSimpleName();

    private final List<Compute> mComputeList = new ArrayList<>();

    public ComputeManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public static IComputeManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }

        IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if (iInterface != null && iInterface instanceof IComputeManager) {
            return (IComputeManager) iInterface;
        }
        return new ComputeManagerImpl.Proxy(obj);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_addCompute:
                data.enforceInterface(DESCRIPTOR);
                Compute _arg0;
                if (data.readInt() != 0) {
                    _arg0 = Compute.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                this.addCompute(_arg0);
                reply.writeNoException();
                return true;
            case TRANSACTION_getComputeList:
                data.enforceInterface(DESCRIPTOR);
                List<Compute> _result = this.getComputeList();
                reply.writeNoException();
                reply.writeTypedList(_result);
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IComputeManager {

        private IBinder mRemote;
        public Proxy(IBinder iBinder) {
            mRemote = iBinder;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public List<Compute> getComputeList() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            List<Compute> _result;
            try {
                _data.writeInterfaceToken(getInterfaceDescriptor());
                mRemote.transact(TRANSACTION_getComputeList, _data, _reply, 0);
                _reply.readException();
                _result = _reply.createTypedArrayList(Compute.CREATOR);
            } finally {
                _data.recycle();
                _reply.recycle();
            }
            return _result;
        }

        @Override
        public void addCompute(Compute compute) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            try {
                _data.writeInterfaceToken(getInterfaceDescriptor());
                if (compute != null) {
                    _data.writeInt(1);
                    compute.writeToParcel(_data, 0);
                } else {
                    _data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addCompute, _data, _reply, 0);
                _reply.readException();
            } finally {
                _data.recycle();
                _reply.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }

    @Override
    public List<Compute> getComputeList() throws RemoteException {
        return mComputeList;
    }

    @Override
    public void addCompute(Compute compute) throws RemoteException {
        mComputeList.add(compute);
        LogUtils.d(TAG, "AIDL addCompute: " + compute);
    }
}
