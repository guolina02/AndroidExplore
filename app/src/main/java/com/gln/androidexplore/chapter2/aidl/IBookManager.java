package com.gln.androidexplore.chapter2.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.gln.androidexplore.chapter2.model.Book;

import java.util.List;

/**
 * Created by guolina on 2017/6/23.
 */
public interface IBookManager extends IInterface {

    static final String DESCRIPTOR = "com.gln.androidexplore.chapter2.aidl.IBookManager";
    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Book> getBookList() throws RemoteException;
    public void addBook(Book book) throws RemoteException;
}
