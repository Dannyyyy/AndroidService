package com.example.danil.androidservice;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FirstService extends Service {

    ExecutorService executor;

    public void onCreate() {
        super.onCreate();
        executor = Executors.newFixedThreadPool(1);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        int time = intent.getIntExtra(FirstActivity.PARAM_TIME, 1);
        PendingIntent pi = intent.getParcelableExtra(FirstActivity.PARAM_PINTENT);

        MyRunnable runnable = new MyRunnable(time, startId, pi);
        executor.execute(runnable);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyRunnable implements Runnable {

        int time;
        int startId;
        PendingIntent pi;

        public MyRunnable(int time, int startId, PendingIntent pi) {
            this.time = time;
            this.startId = startId;
            this.pi = pi;
        }

        public void run() {
            try {
                pi.send(FirstActivity.STATUS_START);
                for(int i=0;i<=time;i++){
                    TimeUnit.SECONDS.sleep(1);
                    Intent intent = new Intent().putExtra(FirstActivity.PARAM_RESULT, i);
                    pi.send(FirstService.this, FirstActivity.STATUS_INTERMEDIATE, intent);
                }

                TimeUnit.SECONDS.sleep(1);

                Intent intent = new Intent().putExtra(FirstActivity.PARAM_RESULT, time * 100);
                pi.send(FirstService.this, FirstActivity.STATUS_FINISH, intent);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            stop();
        }

        void stop() {
            stopSelfResult(startId);
        }
    }
}
