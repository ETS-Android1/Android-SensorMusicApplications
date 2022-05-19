package com.example.myapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

public class SensorReceiver extends BroadcastReceiver {

    private AudioManager audioManager;
    private String INTENT_NAME = "SENSOR-VALUE";
    private int maxVolume;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent1 = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            context.startActivity(intent1);
        }




        int sensor_value = intent.getIntExtra(INTENT_NAME, 1);

        if (sensor_value == 1) {
            // CEPTE HAREKETLI
            Log.d("SENSOR", "CEPTE HAREKETLI");
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        }
        else if (sensor_value == 2) {
            // MASADA HAREKETSIZ
            Log.d("SENSOR", "MASADA HAREKETSIZ");
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
        else {
            // CEPTE HAREKETSIZ
            Log.d("SENSOR", "CEPTE HAREKETSIZ");
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }

    }
}