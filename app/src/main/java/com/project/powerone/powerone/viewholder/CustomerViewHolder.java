package com.project.powerone.powerone.viewholder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Customer;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aharoldk on 01/08/17.
 */

public class CustomerViewHolder extends RecyclerView.ViewHolder implements LocationListener {

    private TextView custName, custAddress, custStatus, arList;
    private Button custButton;

    private LinearLayout linearDone, linearAR;

    private Criteria criteria = new Criteria();
    private Looper looper = null;

    private Activity activity;

    private LocationManager lm;

    private DatabaseHelper databaseHelper;

    private Calendar calendar = Calendar.getInstance();

    private String custID;
    private String dateTimeNow;
    private String dDueDate;

    private Intent intentService;
    private AlertDialog.Builder builder;

    public CustomerViewHolder(View itemView) {
        super(itemView);

        custName = itemView.findViewById(R.id.customerName);
        custAddress = itemView.findViewById(R.id.customerAddress);
        custStatus = itemView.findViewById(R.id.customerStatus);

        custButton = itemView.findViewById(R.id.customerButton);
    }

    public void bind(final Customer customer, final Activity activity) {
        intentService = new Intent(activity, AngelosService.class);

        this.activity = activity;

        custName.setText(customer.getCustName());
        custAddress.setText(customer.getCustAddress());
        custStatus.setText(customer.getStatusCustomer());


        custButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(activity);
                View mView = activity.getLayoutInflater().inflate(R.layout.detail_customer, null);

                linearDone = mView.findViewById(R.id.linearDone);
                linearAR = mView.findViewById(R.id.linearAR);

                linearDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.stopService(intentService);

                        lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        //criteria

                        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                        criteria.setPowerRequirement(Criteria.POWER_LOW);
                        criteria.setAltitudeRequired(false);
                        criteria.setBearingRequired(false);
                        criteria.setSpeedRequired(false);
                        criteria.setCostAllowed(true);
                        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

                        lm.requestSingleUpdate(criteria, CustomerViewHolder.this, looper);

                        custID = customer.getCustID();
                    }
                });

                linearAR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builderAR = new AlertDialog.Builder(activity);
                        View arView = activity.getLayoutInflater().inflate(R.layout.detail_arcust, null);

                        arList = arView.findViewById(R.id.arList);

                        StringBuffer stringBuffer = new StringBuffer();

                        databaseHelper = new DatabaseHelper(activity);

                        Cursor cursor = databaseHelper.getArCustomer(customer.getCustID());

                        while(cursor.moveToNext()){

                            dateTimeFormat(cursor.getString(5));

                            stringBuffer.append(cursor.getString(4)).append("   ").append(dDueDate).append("   Rp. ").append(NumberFormat.getNumberInstance(Locale.US).format(cursor.getLong(6))).append("\n \n");
                        }

                        arList.setText(stringBuffer);

                        builderAR.setCancelable(true);
                        builderAR.setView(arView);
                        builderAR.show();

                    }
                });


                builder.setCancelable(true);
                builder.setView(mView);
                builder.show();
            }
        });
    }

    private void dateTimeFormat(String ddueDate) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(ddueDate);
            DateFormat finalFormat = new SimpleDateFormat("dd-MM-yyyy");
            dDueDate = finalFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        databaseHelper = new DatabaseHelper(activity);

        double longitude = location.getLongitude();

        double latitude = location.getLatitude();

        Toast.makeText(activity, "Your Logitude : "+ longitude +" and Latitude : "+ latitude, Toast.LENGTH_SHORT).show();

        getDate();

        String active = "Active";
        boolean updateCustomer = databaseHelper.updateCustomerVisit(custID, longitude, latitude, active, dateTimeNow);

        if(!updateCustomer){
            Toast.makeText(activity, "Please Press Done Again and Check Your Connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Update Complete", Toast.LENGTH_SHORT).show();

            Intent intent = activity.getIntent();

            activity.finish();
            activity.overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.overridePendingTransition(0, 0);
            activity.startActivity(intent);



            activity.startService(intentService);
        }
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

    private void getDate() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dateTimeNow = dateTimeFormat.format(calendar.getTime());
    }

}
