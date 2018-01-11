package com.project.powerone.powerone;

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
import android.widget.Button;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExportActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.buttonAR) Button buttonAR;
    @BindView(R.id.buttonCustomer) Button buttonCustomer;
    @BindView(R.id.totalAR) TextView totalAR;
    @BindView(R.id.totalCustomer) TextView totalCustomer;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private Status[] statuses;
    private Gson gson = new Gson();

    private Calendar calendar = Calendar.getInstance();
    private DatabaseHelper databaseHelper;

    private String dbUserID;
    private String dbSiteID;
    private String dateConvert = "";
    private String dateNow;
    private String timeNow;
    private int exTrackFail;
    private int exCustOrder;
    private int exCustOrderFail;
    private int exARPayFail;
    private int exARPay;
    private static final int POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        getDate();
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

        databaseHelper = new DatabaseHelper(this);

        ButterKnife.bind(this);
        
        buttonAR.setOnClickListener(this);
        buttonCustomer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(buttonAR)) {
            exportAR();

        } else if (view.equals(buttonCustomer)) {
            exportCustOrder();

        }
    }

    private void getUserID() {
        Cursor cursor = databaseHelper.loginSalesman();

        cursor.moveToFirst();
        dbUserID = cursor.getString(0);
        dbSiteID = cursor.getString(2);

        String URLCustomer = "http://202.43.162.180:8082/poweronemobilewebservice/ws_update_salesmanmap?arg_site="+dbSiteID+"&arg_salesman="+dbUserID+"&arg_count="+cursor.getInt(5)+"&arg_tgl="+dateNow+"%"+timeNow;

        RequestQueue requestCount = Volley.newRequestQueue(ExportActivity.this);

        StringRequest stringRequestCount = new StringRequest(
                Request.Method.GET,
                URLCustomer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseCount) {
                        Log.i("responseCustomer", responseCount);

                        statuses = gson.fromJson(responseCount, Status[].class);

                        int xStatusCount = statuses[POSITION].getxStatus();

                        if(xStatusCount == 1){
                            if(databaseHelper.updateCountMap(dbUserID, 0)){
                                Toast.makeText(ExportActivity.this, "Please Check Order and AR Payment before Export Data", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );

        requestCount.add(stringRequestCount);

    }

    private void exportCustomer() {
        Cursor mCurCust = databaseHelper.getAlCustomer();

        while(mCurCust.moveToNext()) {
            String mStatusCost = mCurCust.getString(11);
            if(mStatusCost.equals("Active")) {

                String URLCustomer = "http://202.43.162.180:8082/PowerOneMobileWebService/ws_update_customer_position?arg_site="+dbSiteID+"&arg_salesman="+dbUserID+"&arg_customer="+mCurCust.getString(3)+"&arg_maplong="+mCurCust.getDouble(9)+"&arg_maplat="+mCurCust.getDouble(10);

                RequestQueue requestQueueCustomer = Volley.newRequestQueue(ExportActivity.this);

                StringRequest stringRequestCustomer = new StringRequest(
                        Request.Method.GET,
                        URLCustomer,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String responseCustomer) {
                                Log.i("responseCustomer", responseCustomer);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();

                            }
                        }
                );

                requestQueueCustomer.add(stringRequestCustomer);
            }
        }

    }

    private void exportTracking() {
        Cursor mCurTracking = databaseHelper.getTracking();

        if(mCurTracking.getCount() > 0) {

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

                                if(responseTrack.length() != 0){
                                    try {
                                        statuses = gson.fromJson(responseTrack, Status[].class);

                                        int xStatusTrack = statuses[POSITION].getxStatus();

                                        if(xStatusTrack != 1){
                                            exTrackFail++;
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                } else {
                                    exTrackFail++;
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                                    exTrackFail++;
                                }

                                error.printStackTrace();

                            }
                        }
                );

                requestQueueTrack.add(stringRequestTrack);
            }

            if(exTrackFail == 0) {
                mCurTracking.close();
                databaseHelper.emptyDatabase("MobTrack");

            }

            exTrackFail = 0;
            mCurTracking.close();
        }

    }

    private void exportCustOrder() {

        Cursor mCustORder = databaseHelper.getAllOrdeR();

        if (mCustORder.getCount() > 0) {
            while (mCustORder.moveToNext()) {
                int bConfirmOrder = mCustORder.getInt(11);
                int bTransferOrder = mCustORder.getInt(12);

                if(bConfirmOrder == 1 && bTransferOrder == 0) {
                    final String urutIDCustOrder = Integer.toString(mCustORder.getInt(0));

                    String URLCustOrder= "http://202.43.162.180:8082/PowerONEMobileWebService/ws_update_salesorder?arg_siteid="+mCustORder.getString(1)+"&arg_salesmanid="+mCustORder.getString(2)+"&arg_custid="+mCustORder.getString(3)+"&arg_productid="+mCustORder.getString(4)+"&arg_qtybig="+mCustORder.getInt(5)+"&arg_qtysmall="+mCustORder.getInt(6)+"&arg_salesprice="+mCustORder.getInt(7)+"&arg_pctdisc1="+mCustORder.getDouble(8)+"&arg_pctdisc2="+mCustORder.getDouble(9)+"&arg_pctdisc3="+mCustORder.getDouble(10)+"&arg_bconfirm=1&arg_btransfer=1";

                    RequestQueue sQueueCustOrder = Volley.newRequestQueue(ExportActivity.this);

                    StringRequest sRequestCustOrder = new StringRequest(
                            Request.Method.GET,
                            URLCustOrder,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String responseCustOrder) {
                                    Log.i("responseCustOrder", responseCustOrder);

                                    if(responseCustOrder.length() != 0) {

                                        try {

                                            statuses = gson.fromJson(responseCustOrder, Status[].class);

                                            int xStatusCustOrder = statuses[POSITION].getxStatus();

                                            if(xStatusCustOrder == 1) {

                                                if(!databaseHelper.updateCustOrder(urutIDCustOrder)){
                                                    exCustOrderFail++;

                                                } else {
                                                    exCustOrder++;
                                                    totalCustomer.setText(""+exCustOrder);

                                                    Toast.makeText(ExportActivity.this, "Export Customer Order Complete", Toast.LENGTH_SHORT).show();

                                                }

                                            } else {
                                                exCustOrderFail++;

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }
                                    } else {
                                        exCustOrderFail++;

                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();

                                    if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                                        exCustOrderFail++;
                                    }
                                }
                            }
                    );

                    sQueueCustOrder.add(sRequestCustOrder);

                }
            }

            if(exCustOrderFail > 0){
                Toast.makeText(this, "Please Export Customer Order Again", Toast.LENGTH_SHORT).show();

            } else {
                if(exCustOrder == 0) {
                    Toast.makeText(this, "Nothing To Export", Toast.LENGTH_SHORT).show();

                }

                exCustOrderFail = 0;
                exCustOrder = 0;

            }

        } else {
            Toast.makeText(this, "Please Make a Order or AR Payment First", Toast.LENGTH_SHORT).show();
        }

        mCustORder.close();

    }

    private void exportAR() {

        Cursor mCustARPayment= databaseHelper.getAllArPaymenT();

        if(mCustARPayment.getCount() != 0) {
            while (mCustARPayment.moveToNext()) {

                int bConfirmARP = mCustARPayment.getInt(9);
                int bTransferOrder = mCustARPayment.getInt(10);

                if(bConfirmARP == 1 && bTransferOrder == 0) {

                    final String urutIDPayment = Integer.toString(mCustARPayment.getInt(0));

                    if(!mCustARPayment.getString(8).isEmpty()){

                        SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                        try {

                            dateConvert = myFormat.format(fromUser.parse(mCustARPayment.getString(8)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    String URLARPayment = "http://202.43.162.180:8082/PowerONEMobileWebService/ws_update_ar_payment?arg_site="+mCustARPayment.getString(1)+"&arg_salesman="+mCustARPayment.getString(2)+"&arg_customer="+mCustARPayment.getString(3)+"&arg_invoice="+mCustARPayment.getString(4)+"&arg_nominal="+mCustARPayment.getInt(5)+"&arg_type="+mCustARPayment.getString(6)+"&arg_bilyet="+mCustARPayment.getString(7)+"&arg_bilyetdate="+dateConvert;

                    RequestQueue sQueueARP = Volley.newRequestQueue(ExportActivity.this);

                    StringRequest sRequestARP = new StringRequest(
                            Request.Method.GET,
                            URLARPayment,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String responseARP) {
                                    Log.i("responseARP", responseARP);

                                    if(responseARP.length() != 0){
                                        try {
                                            statuses = gson.fromJson(responseARP, Status[].class);

                                            int xStatusARP = statuses[POSITION].getxStatus();

                                            if(xStatusARP == 1) {

                                                if(!databaseHelper.updateTransferARPayment(urutIDPayment)){
                                                    exARPayFail++;

                                                } else {
                                                    exARPay++;
                                                    totalAR.setText(""+exARPay);

                                                }

                                            } else {
                                                exARPayFail++;
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        exARPayFail++;
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();

                                if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                                    exARPayFail++;
                                }
                            }
                        }
                    );

                    sQueueARP.add(sRequestARP);

                }

            }

            if(exARPayFail > 0){
                Toast.makeText(this, "Please Export Customer Order Again", Toast.LENGTH_SHORT).show();

            } else {
                if(exARPay > 0){
                    Toast.makeText(this, "Export AR Payment Complete", Toast.LENGTH_SHORT).show();
                    databaseHelper.emptyDatabase("MobARPayment");

                } else if(exARPay == 0){
                    Toast.makeText(this, "Nothing To Export", Toast.LENGTH_SHORT).show();

                }

                exARPayFail = 0;
                exARPay = 0;

            }
        } else {
            Toast.makeText(this, "Please Make a Order or AR Payment First", Toast.LENGTH_SHORT).show();
        }



        mCustARPayment.close();

    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, ImportActivity.class));

                } else if(id == R.id.exportScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, ExportActivity.class));

                } else if(id == R.id.visitScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, SalesActivity.class));

                } else if(id == R.id.produkScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, ProductActivity.class));

                } else if(id == R.id.orderScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, OrderActivity.class));

                } else if(id == R.id.ARScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, ARActivity.class));

                } else if(id == R.id.reportScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, ReportActivity.class));

                } else if (id == R.id.photoScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, PhotoActivity.class));

                } else if(id == R.id.passwordScreen){
                    finish();
                    startActivity(new Intent(ExportActivity.this, PasswordActivity.class));

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

    private void getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy:HH:mm");

        dateNow = dateFormat.format(calendar.getTime());
        timeNow = timeFormat.format(calendar.getTime());
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
