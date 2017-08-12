package com.project.powerone.powerone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.powerone.powerone.pojo.Status;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

public class ExportActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private Status[] statuses;
    private Gson gson = new Gson();

    private DatabaseHelper databaseHelper;

    private ProgressDialog pDialogC;

    private String dbUserID;
    private String dbSiteID;
    private int exCustFail = 0, xStatusCustomer = 0, exTrackFail = 0, xStatusTrack = 0;;
    private static final int POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        declarate();
        navigation();
        getUserID();
        exportCustomer();
        exportTracking();
    }

    private void declarate() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialogC = new ProgressDialog(this);
        pDialogC.setCancelable(false);

        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onClick(View view) {
    }

    private void getUserID() {
        Cursor cursor = databaseHelper.loginSalesman();

        cursor.moveToFirst();
        dbUserID = cursor.getString(0);
        dbSiteID = cursor.getString(2);
    }

    private void exportCustomer() {
        Cursor mCurCust = databaseHelper.getAlCustomer();

        while(mCurCust.moveToNext()) {

            if(mCurCust.getString(11).equals("Active")) {

                String URLCustomer = "http://202.43.162.180:8082/PowerOneMobileWebService/ws_update_customer_position?arg_site="+dbSiteID+"&arg_salesman="+dbUserID+"&arg_customer="+mCurCust.getString(3)+"&arg_maplong="+mCurCust.getDouble(9)+"&arg_maplat="+mCurCust.getDouble(10);

                RequestQueue requestQueueCustomer = Volley.newRequestQueue(ExportActivity.this);

                StringRequest stringRequestCustomer = new StringRequest(
                        Request.Method.GET,
                        URLCustomer,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String responseCustomer) {
                                Log.i("responseCustomer", responseCustomer);

                                try {
                                    statuses = gson.fromJson(responseCustomer, Status[].class);

                                    xStatusCustomer = statuses[POSITION].getxStatus();

                                    if(xStatusCustomer != 1 || xStatusCustomer == 0){
                                        exCustFail++;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();

                                    exCustFail++;
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                                    exCustFail++;
                                } else {
                                    exCustFail++;
                                }

                                error.printStackTrace();

                            }
                        }
                );

                requestQueueCustomer.add(stringRequestCustomer);
            }
        }

        mCurCust.close();

        if(exCustFail > 0){
            Toast.makeText(this, "good1", Toast.LENGTH_SHORT).show();
            exportCustomer();

        }

    }

    private void exportTracking() {
        Cursor mCurTracking = databaseHelper.getTracking();

        while(mCurTracking.moveToNext()) {

            String URLTracking = "http://202.43.162.180:8082/PowerONEMobileWebService/ws_update_salesmantracking?arg_Tanggal="+mCurTracking.getString(3)+"%"+mCurTracking.getString(4)+"&arg_Site="+dbSiteID+"&arg_Salesman="+dbUserID+"&arg_MapLong="+mCurTracking.getDouble(2)+"&arg_mapLat="+mCurTracking.getDouble(1)+"&arg_Line="+mCurTracking.getInt(0);

            RequestQueue requestQueueTrack = Volley.newRequestQueue(ExportActivity.this);

            StringRequest stringRequestTrack = new StringRequest(
                    Request.Method.GET,
                    URLTracking,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseTrack) {
                            Log.i("responseTrack", responseTrack);

                            try {
                                statuses = gson.fromJson(responseTrack, Status[].class);

                                xStatusTrack = statuses[POSITION].getxStatus();

                                if(xStatusTrack != 1 || xStatusTrack == 0){
                                    exTrackFail++;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                                exTrackFail++;
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                                exTrackFail++;
                            } else {
                                exTrackFail++;
                            }

                            error.printStackTrace();

                        }
                    }
            );

            requestQueueTrack.add(stringRequestTrack);
        }

        mCurTracking.close();

        if(exTrackFail > 0){
            Toast.makeText(this, "good2", Toast.LENGTH_SHORT).show();
            exportTracking();

        } else {
            databaseHelper.emptyDatabase("MobTrack");

        }

    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    startActivity(new Intent(ExportActivity.this, ImportActivity.class));
                    finish();

                } else if(id == R.id.exportScreen){
                    startActivity(new Intent(ExportActivity.this, ExportActivity.class));
                    finish();

                } else if(id == R.id.visitScreen){
                    startActivity(new Intent(ExportActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(ExportActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(ExportActivity.this, OrderActivity.class));
                    finish();

                } else if(id == R.id.ARScreen){
                    startActivity(new Intent(ExportActivity.this, ARActivity.class));
                    finish();

                } else if(id == R.id.reportScreen){
                    startActivity(new Intent(ExportActivity.this, ReportActivity.class));
                    finish();

                } else if (id == R.id.photoScreen){
                    startActivity(new Intent(ExportActivity.this, PhotoActivity.class));
                    finish();

                } else if(id == R.id.passwordScreen){
                    startActivity(new Intent(ExportActivity.this, PasswordActivity.class));
                    finish();

                } else if(id == R.id.logout){
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(ExportActivity.this, LoginActivity.class));

                }

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            finish();
            startActivity(new Intent(ExportActivity.this, MainActivity.class));

        }
    }
}
