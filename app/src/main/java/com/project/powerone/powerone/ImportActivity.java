package com.project.powerone.powerone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.powerone.powerone.pojo.Customer;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.pojo.Status;
import com.project.powerone.powerone.sql.DatabaseHelper;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView totalCustomer, totalProduct, totalPrice, totalAr;

    private Button buttonCustomer, buttonProduct, buttonPrice, buttonAr, importButton;

    private Product[] products;
    private Customer[] customers;

    private Gson gson = new Gson();

    private String  siteID, productID, productName, bigPack, smallPack, prinsipalName, groupProductName, subGroupProductName;
    private int noOfPack, urutID, qtyOnHand, countProduct = 0, counFailProduct = 0, countCustomer = 0, countFailCustomer = 0;

    private String salesmanID, custID, custName, custAddress, priceType;
    private double geoMapLong, geoMapLat, gpsMapLong, gpsMapLat;


    private DatabaseHelper databaseHelper;
    private String dbUserID, dbSiteID;
    private static final int USERID = 0;
    private static final int SITEID = 2;

    private boolean insertedProduct, insertedCustomer;

    private ProgressDialog progressDialog;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        databaseHelper = new DatabaseHelper(ImportActivity.this);

        progressDialog = new ProgressDialog(this);

        Cursor cursor = databaseHelper.loginSalesman();
        cursor.moveToNext();

        dbUserID = cursor.getString(USERID);
        dbSiteID = cursor.getString(SITEID);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        declare();
        navigation();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    startActivity(new Intent(ImportActivity.this, ImportActivity.class));
                    finish();

                } else if(id == R.id.exportScreen){
                    startActivity(new Intent(ImportActivity.this, ExportActivity.class));
                    finish();

                } else if(id == R.id.visitScreen){
                    startActivity(new Intent(ImportActivity.this, VisitActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(ImportActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(ImportActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.ARScreen){
                    startActivity(new Intent(ImportActivity.this, ARActivity.class));
                    finish();

                } else if(id == R.id.reportScreen){
                    startActivity(new Intent(ImportActivity.this, ReportActivity.class));
                    finish();

                } else if (id == R.id.photoScreen){
                    startActivity(new Intent(ImportActivity.this, PhotoActivity.class));
                    finish();

                } else if(id == R.id.passwordScreen){
                    startActivity(new Intent(ImportActivity.this, PasswordActivity.class));
                    finish();

                } else if(id == R.id.logout){
                    startActivity(new Intent(ImportActivity.this, LoginActivity.class));
                    finish();

                }

                return true;
            }
        });
    }

    private void declare() {

        totalCustomer = (TextView) findViewById(R.id.totalCustomer);
        totalProduct = (TextView) findViewById(R.id.totalProduct);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        totalAr = (TextView) findViewById(R.id.totalAr);

        buttonCustomer = (Button) findViewById(R.id.buttonCustomer);
        buttonProduct = (Button) findViewById(R.id.buttonProduct);
        buttonPrice = (Button) findViewById(R.id.buttonPrice);
        buttonAr = (Button) findViewById(R.id.buttonAr);

        importButton = (Button) findViewById(R.id.importButton);

        buttonCustomer.setOnClickListener(this);
        buttonProduct.setOnClickListener(this);
        buttonPrice.setOnClickListener(this);
        buttonAr.setOnClickListener(this);

        importButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonCustomer){
            customerData();

        } else if(view == buttonProduct){
            productData();

        } else if(view == buttonPrice) {
            priceData();

        } else if(view == buttonAr){
            arData();

        } else if(view == importButton){
            customerData();
            productData();
            priceData();
            arData();

        }
    }

    private void arData() {
    }

    private void priceData() {

    }

    private void productData() {
        progressDialog.setMessage("Wait Import Product Data . . . ");
        progressDialog.show();

        String URLProduct = "http://202.43.162.180:8082/PowerONEMobileWebService/ws_retrieve_product?arg_site="+dbSiteID;

        RequestQueue requestQueueProduct = Volley.newRequestQueue(ImportActivity.this);

        StringRequest stringRequestProduct = new StringRequest(
                Request.Method.GET,
                URLProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseProduct) {
                        Log.i("responseProduct", responseProduct);
                        databaseHelper.emptyDatabase("MobProduct");

                        try {
                            products = gson.fromJson(responseProduct, Product[].class);

                            for(int i = 0; i < responseProduct.length(); i++) {

                                urutID = products[i].getUrutID();
                                siteID = products[i].getSiteID();
                                productID = products[i].getProductID();
                                productName = products[i].getProductName();
                                bigPack = products[i].getBigPack();
                                smallPack = products[i].getSmallPack();
                                prinsipalName = products[i].getPrinsipalName();
                                groupProductName = products[i].getGroupProductName();
                                subGroupProductName = products[i].getSubGroupProductName();
                                noOfPack = products[i].getNoOfPack();
                                qtyOnHand = products[i].getQtyOnHand();

                                insertedProduct = databaseHelper.insertProduct(urutID, siteID, productID, productName, bigPack, smallPack, prinsipalName, groupProductName, subGroupProductName, noOfPack, qtyOnHand);

                                if(insertedProduct != true){
                                    counFailProduct++;
                                }

                                countProduct++;
                                insertedProduct = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(counFailProduct == 0){
                            progressDialog.dismiss();

                            totalProduct.setText(""+ countProduct);
                            countProduct = 0;

                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(ImportActivity.this, "Please Import Product Again", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );

        requestQueueProduct.add(stringRequestProduct);

    }

    private void customerData() {
        progressDialog.setMessage("Wait Import Customer Data . . . ");
        progressDialog.show();

        String URLCustomer = "http://202.43.162.180:8082/PowerONEMobileWebService/ws_retrieve_customer?arg_site="+dbSiteID+"&arg_salesman="+dbUserID;

        RequestQueue requestQueueCustomer = Volley.newRequestQueue(ImportActivity.this);

        StringRequest stringRequestCustomer = new StringRequest(
                Request.Method.GET,
                URLCustomer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseCustomer) {
                        Log.i("responseCustomer", responseCustomer);

                        databaseHelper.emptyDatabase("MobCustomer");

                        try {
                            customers = gson.fromJson(responseCustomer, Customer[].class);
                            for(int i = 0; i < responseCustomer.length(); i++) {

                                urutID = customers[i].getUrutID();
                                siteID = customers[i].getSiteID();
                                salesmanID = customers[i].getSalesmanID();
                                custID = customers[i].getCustID();
                                custName = customers[i].getCustName();
                                custAddress = customers[i].getCustAddress();
                                priceType = customers[i].getPriceType();
                                geoMapLong = customers[i].getGeoMapLong();
                                geoMapLat = customers[i].getGeoMapLat();
                                gpsMapLong = customers[i].getGPSMapLong();
                                gpsMapLat = customers[i].getGPSMapLat();

                                insertedCustomer = databaseHelper.insertCustomer(urutID, siteID, salesmanID, custID, custName, custAddress, priceType, geoMapLong, geoMapLat, gpsMapLong, gpsMapLat);

                                if(insertedCustomer != true){
                                    countFailCustomer++;
                                }

                                countCustomer++;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(countFailCustomer == 0){
                            progressDialog.dismiss();

                            totalCustomer.setText(""+ countCustomer);
                            countCustomer = 0;

                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(ImportActivity.this, "Please Import Customer Again", Toast.LENGTH_SHORT).show();
                        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            startActivity(new Intent(ImportActivity.this, MainActivity.class));
            finish();
        }
    }


}
