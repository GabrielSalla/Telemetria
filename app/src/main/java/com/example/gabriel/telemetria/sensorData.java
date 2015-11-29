package com.example.gabriel.telemetria;
import java.util.ArrayList;

public class sensorData{
    private ArrayList<Long> millis;
    private ArrayList<Double> latitudeGPS;
    private ArrayList<Double> longitudeGPS;
    private ArrayList<Double> accuracyGPS;

    int size;

    public static class Reading{
        private long ms;
        private double latitudeGPS, longitudeGPS, accuracyGPS;

        public Reading(){}

        public Reading(long ms, double latitudeGPS, double longitudeGPS, double accuracyGPS){
            this.ms = ms;
            this.latitudeGPS = latitudeGPS;
            this.longitudeGPS = longitudeGPS;
            this.accuracyGPS = accuracyGPS;
        }

        public long getMs(){
            return ms;
        }

        public double getLatitudeGPS(){
            return latitudeGPS;
        }

        public double getLongitudeGPS(){
            return longitudeGPS;
        }

        public double getAccuracyGPS(){
            return accuracyGPS;
        }
    }

    public sensorData(){
        millis = new ArrayList<>();

        latitudeGPS = new ArrayList<>();
        longitudeGPS = new ArrayList<>();
        accuracyGPS = new ArrayList<>();

        size = 0;
    }

    public void add(Reading reading){
        size++;

        this.millis.add(reading.getMs());

        this.latitudeGPS.add(reading.getLatitudeGPS());
        this.longitudeGPS.add(reading.getLongitudeGPS());
        this.accuracyGPS.add(reading.getAccuracyGPS());
    }

    public Reading get(int index){
        return new Reading(millis.get(index), latitudeGPS.get(index), longitudeGPS.get(index), accuracyGPS.get(index));
    }

    public int size(){
        return size;
    }

    public void clear(){
        size = 0;

        millis.clear();

        latitudeGPS.clear();
        longitudeGPS.clear();
        accuracyGPS.clear();
    }
}
