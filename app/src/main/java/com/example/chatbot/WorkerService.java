package com.example.chatbot;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class WorkerService extends Service {

    private Handler handler = new Handler();
    private Runnable soundRunnable = new Runnable() {
        @Override
        public void run() {
            startService(new Intent(WorkerService.this, SoundService.class));
            handler.postDelayed(this, 10 * 1000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(soundRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(soundRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}