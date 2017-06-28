package com.gln.androidexplore.chapter2.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.gln.androidexplore.chapter2.model.Book;
import com.gln.androidexplore.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolina on 2017/6/23.
 */
public class BookManagerImpl extends Binder implements IBookManager {

    private static final String TAG = BookManagerImpl.class.getSimpleName();

    private final List<Book> mBookList = new ArrayList<>();

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }

        IInterface iin = iBinder.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IBookManager) {
            return (IBookManager) iin;
        }

        return new BookManagerImpl.Proxy(iBinder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                Book _arg0;
                if (data.readInt() != 0) {
                    _arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                this.addBook(_arg0);
                reply.writeNoException();
                return true;
            case TRANSACTION_getBookList:
                data.enforceInterface(DESCRIPTOR);
                List<Book> _result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(_result);
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IBookManager {

        private IBinder mRemote;

        public Proxy(IBinder remote) {
            this.mRemote = remote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            List<Book> _result;
            try {
                _data.writeInterfaceToken(getInterfaceDescriptor());
                mRemote.transact(TRANSACTION_getBookList, _data, _reply, 0);
                _reply.readException();
                _result = _reply.createTypedArrayList(Book.CREATOR);
            } finally {
                _reply.recycle();
                _data.recycle();
            }
            return _result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            try {
                _data.writeInterfaceToken(getInterfaceDescriptor());
                if (book != null) {
                    _data.writeInt(1);
                    book.writeToParcel(_data, 0);
                } else {
                    _data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, _data, _reply, 0);
                _reply.readException();
            } finally {
                _reply.recycle();
                _data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return mBookList;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        mBookList.add(book);
        LogUtils.d(TAG, "AIDL addBook: " + book);
    }
}
