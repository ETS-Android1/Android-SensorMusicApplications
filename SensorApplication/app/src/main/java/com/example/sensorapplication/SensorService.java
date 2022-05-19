package com.example.sensorapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;


public class SensorService extends Service implements SensorEventListener {

    private static final String TAG = "SensorService";
    private static final String INTENT_NAME = "SENSOR-VALUE";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private  Sensor light;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private float[] light_array;
    private float light_level;

    private boolean inLight;
    private boolean inMove;


    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "Initialzing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d(TAG, "Registered Accelerometer listener");
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Log.d(TAG, "Registered Light listener");


        Intent brodIntent = new Intent();

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        inLight = false;
        inMove = false;

        sensorManager.registerListener(SensorService.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorService.this, light, SensorManager.SENSOR_DELAY_NORMAL);


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service", "Service is running");

                            if (inMove && !inLight) {
                                // hareketli ve telefon cepte
//                                Log.d(TAG, "CEPTE, HAREKETLI");

                                brodIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                brodIntent.setAction("com.example.myBroadcastMessage");
                                brodIntent.putExtra(INTENT_NAME, 1);
                                sendBroadcast(brodIntent);

                            }
                            else if (!inMove && inLight) {
                                // hareketsiz ve telefon masada
//                                Log.d(TAG, "MASADA, HAREKETSIZ");

                                brodIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                brodIntent.setAction("com.example.myBroadcastMessage");
                                brodIntent.putExtra(INTENT_NAME, 2);
                                sendBroadcast(brodIntent);
                            }
                            else if (!inMove && !inLight) {
                                // hareketsiz ve telefon cepte
//                                Log.d(TAG, "CEPTE, HAREKETSIZ");

                                brodIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                brodIntent.setAction("com.example.myBroadcastMessage");
                                brodIntent.putExtra(INTENT_NAME, 3);
                                sendBroadcast(brodIntent);
                            }


                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        ).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            mGravity = sensorEvent.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
//            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
//            mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
            mAccelCurrent = (float) Math.sqrt(x*x + y*y);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 0.00001){
//                Log.d(TAG, " --- Accelerometer Changed : \n    X = " + sensorEvent.values[0] + "\n    Y = " + sensorEvent.values[1] + "\n    Z = " + sensorEvent.values[2]);
                inMove = true;
            }
            else {
                inMove = false;
            }

//            Log.d(TAG, " --- Accelerometer Changed : \n    X = " + sensorEvent.values[0] + "\n    Y = " + sensorEvent.values[1] + "\n    Z = " + sensorEvent.values[2]);
//            System.out.println("...");
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            light_array = sensorEvent.values.clone();
            light_level = light_array[0];
            if (light_level < 150) {
//                Log.d(TAG, "***DARK***");
                inLight = false;
            }
            else {
//                Log.d(TAG, "---LIGHT---");
                inLight = true;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}