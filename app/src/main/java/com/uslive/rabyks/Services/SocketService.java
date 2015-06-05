package com.uslive.rabyks.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketService extends Service {
    public SocketService() {
    }

    Socket s;
    PrintStream os;
    PrintWriter pw;
    Runnable connect = new connectSocket();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        s = new Socket();
    }

    public void IsBoundable(){
        Toast.makeText(this, "I bind like butter", Toast.LENGTH_LONG).show();
    }

//    public void onStart(Intent intent, int startId){
//        super.onStart(intent, startId);
//        Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();
//        Runnable connect = new connectSocket();
//        new Thread(connect).start();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();

        new Thread(connect).start();
        return START_NOT_STICKY;

    }

    class connectSocket implements Runnable {

        @Override
        public void run() {
            SocketAddress socketAddress = new InetSocketAddress("10.0.2.2", 4444);
            try {
                s.connect(socketAddress);
                pw = new PrintWriter(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void WriteToServer(String message){
        try {
//            pw.write("Moja poruka");
            pw.println(message);
            pw.flush();
        } catch (Exception ex){
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            pw.close();
            s.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        s = null;
    }


}
