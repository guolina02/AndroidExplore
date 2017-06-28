package com.gln.androidexplore.chapter2.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.gln.androidexplore.chapter2.model.Compute;

import java.util.List;

/**
 * Created by guolina on 2017/6/23.
 */
public interface IComputeManager extends IInterface {

    static final String DESCRIPTOR = "com.gln.androidexplore.chapter2.aidl.IComputeManager";

    static final int TRANSACTION_addCompute = IBinder.FIRST_CALL_TRANSACTION + 0;
    static final int TRANSACTION_getComputeList = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Compute> getComputeList() throws RemoteException;
    public void addCompute(Compute compute) throws RemoteException;
}
