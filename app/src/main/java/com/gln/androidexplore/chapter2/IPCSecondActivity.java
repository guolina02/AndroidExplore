package com.gln.androidexplore.chapter2;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.gln.androidexplore.BaseActivity;
import com.gln.androidexplore.R;
import com.gln.androidexplore.chapter2.aidl.BookManagerImpl;
import com.gln.androidexplore.chapter2.aidl.ComputeManagerImpl;
import com.gln.androidexplore.chapter2.aidl.IBookManager;
import com.gln.androidexplore.chapter2.aidl.IComputeManager;
import com.gln.androidexplore.chapter2.db.IPCDbManager;
import com.gln.androidexplore.chapter2.model.Book;
import com.gln.androidexplore.chapter2.model.Compute;
import com.gln.androidexplore.util.CommExtras;
import com.gln.androidexplore.util.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * Created by guolina on 2017/6/22.
 */
public class IPCSecondActivity extends BaseActivity {

    private static final String TAG = IPCSecondActivity.class.getSimpleName();

    private static final int MSG_SEND_SOCKET = 301;

    private String mWay;

    private boolean hasBoundMessenger = false;
    private boolean hasBoundAidl = false;
    private boolean hasSocketStarted = false;

    private Messenger mMessenger;
    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            Message msg = Message.obtain(null, MessengerService.MSG_FROM_CLIENT);
            Book book = new Book("MessengerBook", 87.23f, "gln_mess", "PVC");
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommExtras.KEY_BOOK, book);
            bundle.putString(CommExtras.KEY_MSG, "This is msg from client!");
            msg.setData(bundle);
            msg.replyTo = mReplyMessenger;
            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private final Messenger mReplyMessenger = new Messenger(new ReplyHandler());
    private static class ReplyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_TO_CLIENT:
                    LogUtils.d(TAG, "MessengerReply: " + msg.getData().getString(CommExtras.KEY_MSG));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private IBookManager iBookManager;
    private IComputeManager iComputeManager;

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (!isFinishing()) {
                handleAIDL();
            }
        }
    };

    private ServiceConnection mAIDLConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBinderPool binderPool = IBinderPool.Stub.asInterface(service);
            try {
                binderPool.asBinder().linkToDeath(mDeathRecipient, 0);
                iBookManager = BookManagerImpl.asInterface(binderPool.queryBinder(AIDLService.BINDER_BOOK));
                iComputeManager = ComputeManagerImpl.asInterface(binderPool.queryBinder(AIDLService.BINDER_COMPUTE));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                Book book1 = new Book("AIDLBook1", 39.10f, "gln_AIDL", "AIDLPUH");
                Book book2 = new Book("AIDLBook2", 49.19f, "gln_AIDL", "AIDLKAE");
                iBookManager.addBook(book1);
                iBookManager.addBook(book2);

                List<Book> books = iBookManager.getBookList();
                LogUtils.d(TAG, "AIDLBooks: " + books);

                Compute compute1 = new Compute("ThinkPad", 4399f, "intel core i5", "4G");
                Compute compute2 = new Compute("IMAC", 13000f, "unknown", "128G");
                iComputeManager.addCompute(compute1);
                iComputeManager.addCompute(compute2);

                List<Compute> computes = iComputeManager.getComputeList();
                LogUtils.d(TAG, "AIDLComputes: " + computes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Socket mClientSocket;
    private PrintWriter mWriter;
    private int mMsgNumber = 0;

    private Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SEND_SOCKET) {
                sendSocketMsg();
            } else {
                super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_second);

        Intent intent = getIntent();
        mWay = intent.getStringExtra(CommExtras.KEY_IPC_WAY);
        if (CommExtras.KEY_IPC_BUNDLE.equals(mWay)) {
            handleBundle();
        } else if (CommExtras.KEY_IPC_FILE.equals(mWay)) {
            handleFile();
        } else if (CommExtras.KEY_IPC_MESSENGER.equals(mWay)) {
            handleMessenger();
        } else if (CommExtras.KEY_IPC_AIDL.equals(mWay)) {
            handleAIDL();
        } else if (CommExtras.KEY_IPC_SOCKET.equals(mWay)) {
            handleSocket();
        } else if (CommExtras.KEY_IPC_CONTENT_PROVIDER.equals(mWay)) {
            handleContentProvider();
        }
    }

    private void handleBundle() {
        Bundle bundle = getIntent().getBundleExtra(CommExtras.KEY_IPC_BUNDLE);
        Book book = bundle.getParcelable(CommExtras.KEY_BOOK);
        LogUtils.d(TAG, "BundleBook: " + (book == null ? "null" : book.toString()));
    }

    private void handleFile() {
        String fileName = getIntent().getStringExtra(CommExtras.KEY_IPC_FILE);
        File file = new File(fileName);

        if (file.exists()) {
            ObjectInputStream inputStream = null;
            Book book;
            try {
                inputStream = new ObjectInputStream(new FileInputStream(file));
                while ((book = (Book) inputStream.readObject()) != null) {
                    LogUtils.d(TAG, "FileBook: " + book);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleMessenger() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mMessengerConnection, Context.BIND_AUTO_CREATE);
        hasBoundMessenger = true;
    }

    private void handleAIDL() {
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, mAIDLConnection, Context.BIND_AUTO_CREATE);
        hasBoundAidl = true;
    }

    private void handleSocket() {
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        hasSocketStarted = true;

        new Thread() {
            @Override
            public void run() {
                while (mClientSocket == null) {
                    try {
                        mClientSocket = new Socket("localhost", 8688);
                        mWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                        sendSocketMsg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
                    while (!IPCSecondActivity.this.isFinishing() && !mClientSocket.isClosed()) {
                        String msg = reader.readLine();
                        if (msg != null) {
                            LogUtils.d(TAG, msg);
                        }
                    }
                    LogUtils.d(TAG, "Quit chat room");
                    reader.close();
                    mWriter.close();
                    mClientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendSocketMsg() {
        String msg = "Hello, Msg" + mMsgNumber + "[" + new Date() + "]";
        mWriter.println(msg);
        if (++mMsgNumber < 5) {
            mSocketHandler.sendEmptyMessageDelayed(MSG_SEND_SOCKET, 1000);
        }
    }

    private void handleContentProvider() {
        Uri uri = Uri.parse("content://com.gln.androidexplore.chapter2/book");
        ContentResolver resolver = getContentResolver();

        for (int i=0;i<3;i++) {
            ContentValues values1 = new ContentValues();
            values1.put(IPCDbManager.PARAM_BOOK_TITLE, "ContentTitle" + i);
            values1.put(IPCDbManager.PARAM_BOOK_PRICE, 29.34f + i);
            values1.put(IPCDbManager.PARAM_BOOK_WRITER, "gln_content" + i);
            values1.put(IPCDbManager.PARAM_BOOK_PUBLISHER, "gln_pub" + i);
            resolver.insert(uri, values1);
        }

        ContentValues values1 = new ContentValues();
        values1.put(IPCDbManager.PARAM_BOOK_TITLE, "ContentTitle");
        values1.put(IPCDbManager.PARAM_BOOK_PRICE, 29.34f);
        values1.put(IPCDbManager.PARAM_BOOK_WRITER, "gln_content");
        values1.put(IPCDbManager.PARAM_BOOK_PUBLISHER, "gln_pub");
        resolver.update(uri, values1, "title=?", new String[]{"ContentTitle0"});

        resolver.delete(uri, "writer=?", new String[]{"gln_content2"});

        Cursor cursor = resolver.query(uri, new String[]{IPCDbManager.PARAM_BOOK_TITLE, IPCDbManager.PARAM_BOOK_PRICE, IPCDbManager.PARAM_BOOK_WRITER, IPCDbManager.PARAM_BOOK_PUBLISHER}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Book book = new Book(cursor.getString(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3));
                LogUtils.d(TAG, "ContentBook: " + book);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (hasBoundMessenger) {
            unbindService(mMessengerConnection);
        }
        if (hasBoundAidl) {
            unbindService(mAIDLConnection);
        }

        if (hasSocketStarted) {
            stopService(new Intent(this, SocketService.class));
            if (mClientSocket != null) {
                try {
                    mClientSocket.shutdownInput();
                    mClientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onDestroy();
    }
}
