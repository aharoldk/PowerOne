package com.project.powerone.powerone.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AngelosService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double mLat, mLong;
    private String dateNow, timeNow;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLat = location.getLatitude();
                mLong = location.getLongitude();

                saveToDatabase();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000, 0, locationListener);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    private void saveToDatabase() {
        getDate();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        databaseHelper.insertLocationSales(mLat, mLong, dateNow, timeNow);

        Toast.makeText(this, mLat+" , "+mLong, Toast.LENGTH_SHORT).show();
    }

    private void getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        dateNow = dateFormat.format(calendar.getTime());
        timeNow = timeFormat.format(calendar.getTime());
    }
}
