package com.example.gabriel.telemetria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity{
    private TextView textView_LatitudeGPS, textView_LongitudeGPS, textView_AccuracyGPS, textView_Counter;
    private TextView textView_Accelerometer, textView_Rotation;

    private sensorData data;
    private int counter;

    private double latitudeGPS = 0;
    private double longitudeGPS = 0;
    private double accuracyGPS = 0;
    private double axisX = 0, axisY = 0, axisZ = 0;

    Sensor accelerometer;
    SensorManager sensorManager;
    SensorEventListener accelerometerListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event){
            axisX = event.values[0];
            axisY = event.values[1];
            axisZ = event.values[2];

            DecimalFormat dm = new DecimalFormat("#.###");

            textView_Accelerometer.setText("X: " + dm.format(axisX) + ", Y: " + dm.format(axisY) + ", Z: " + dm.format(axisZ));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){

        }
    };

    Sensor rotation;
    private double rotX = 0, rotY = 0, rotZ = 0;
    SensorEventListener rotationListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event){
            rotX = event.values[0];
            rotY = event.values[1];
            rotZ = event.values[2];

            DecimalFormat dm = new DecimalFormat("#.###");

            textView_Rotation.setText("X: " + dm.format(rotX) + ", Y: " + dm.format(rotY) + ", Z: " + dm.format(rotZ));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){

        }
    };

    LocationManager locationManagerGPS;
    LocationListenerGPS locationListenerGPS = new LocationListenerGPS();
    private class LocationListenerGPS implements LocationListener{
        @Override
        public void onLocationChanged(Location location){
            if(location != null){
                latitudeGPS = location.getLatitude();
                longitudeGPS = location.getLongitude();
                accuracyGPS = location.getAccuracy();

                textView_LatitudeGPS.setText(String.valueOf(latitudeGPS));
                textView_LongitudeGPS.setText(String.valueOf(longitudeGPS));
                textView_AccuracyGPS.setText(String.valueOf(accuracyGPS));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled(String provider){

        }

        @Override
        public void onProviderDisabled(String provider){

        }
    }

    private Timer updateTimer = null;
    private Handler mHandler = new Handler();

    private class updateTask extends TimerTask{
        @Override
        public void run(){
            long millis = System.currentTimeMillis();

            sensorData.Reading reading = new sensorData.Reading(millis, latitudeGPS, longitudeGPS, accuracyGPS);

            data.add(reading);

            counter++;

            mHandler.post(new Runnable(){
                @Override
                public void run(){
                    textView_Counter.setText(String.valueOf(counter));
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_LatitudeGPS = (TextView)findViewById(R.id.textView_LatitudeGPS);
        textView_LongitudeGPS = (TextView)findViewById(R.id.textView_LongitudeGPS);
        textView_AccuracyGPS = (TextView)findViewById(R.id.textView_AccuracyGPS);

        textView_Accelerometer = (TextView)findViewById(R.id.textView_Accelerometer);
        textView_Rotation = (TextView)findViewById(R.id.textView_Rotation);

        textView_Counter = (TextView)findViewById(R.id.textView_Counter);

        locationManagerGPS = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelerometerListener, accelerometer, 100000);

        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(rotationListener, rotation, 100000);

        data = new sensorData();
    }

    public void onButtonClick_Start(View v){
        data.clear();
        counter = 0;
        textView_Counter.setText("0");

        updateTimer = new Timer();
        updateTask refreshAll = new updateTask();
        updateTimer.schedule(refreshAll, 0, 100);
    }

    public void onButtonClick_Stop(View v){
        if(updateTimer != null){
            updateTimer.cancel();
            updateTimer = null;
        }
    }

    public void onButtonClick_Export(View v){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Data.xls");

        FileOutputStream fileOut;

        HSSFWorkbook workbook = ExportHelper.MakeSheetFromData(data);

        try{
            fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        } catch(IOException e){
            e.printStackTrace();
        }

        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("application/*");
        send.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        startActivity(Intent.createChooser(send, "Export"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        data.clear();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(rotationListener);
    }

}
