package com.gln.androidexplore.chapter2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gln.androidexplore.util.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by guolina on 2017/6/23.
 */
public class SocketService extends Service {

    private static final String TAG = SocketService.class.getSimpleName();

    private boolean mIsServiceDestroyed = false;

    private String[] mDefinedMessages = new String[]{
            "Hello, Android",
            "I am a robot!",
            "Who are you?",
            "Nice to meet you!",
            "I will see you soon."
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new SocketRunnable()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    private class SocketRunnable implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "failed to listen to port 8688");
                return;
            }

            while (!mIsServiceDestroyed) {
                try {
                    responseToClient(serverSocket.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseToClient(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

        writer.println("Welcome to the chat room");

        while (!mIsServiceDestroyed) {
            String string = reader.readLine();
            if (string == null) {
                break;
            }

            writer.println("I have received from client: " + string);
            int i = new Random().nextInt(mDefinedMessages.length);
            writer.println(mDefinedMessages[i]);
        }

        reader.close();
        writer.close();
        client.close();
    }
}
