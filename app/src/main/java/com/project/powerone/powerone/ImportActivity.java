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
import com.project.powerone.powerone.pojo.ARBalance;
import com.project.powerone.powerone.pojo.Customer;
import com.project.powerone.powerone.pojo.Price;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView totalCustomer, totalProduct, totalPrice, totalAr;

    private Button buttonCustomer, buttonProduct, buttonPrice, buttonAr, importButton;

    private Product[] products;
    private Customer[] customers;
    private Price[] prices;
    private ARBalance[] arBalances;

    private Gson gson = new Gson();

    private String  siteID, productID, productName, bigPack, smallPack, prinsipalName, groupProductName, subGroupProductName;
    private int noOfPack, urutID, qtyOnHand;

    private String invoiceID, dDueDate;
    private long balanceAR;

    private String salesmanID, custID, custName, custAddress, priceType;
    private double geoMapLong, geoMapLat, gpsMapLong, gpsMapLat;

    private int salesPrice;
    private String productType;

    private DatabaseHelper databaseHelper;

    private String dbUserID, dbSiteID, statusVisit = "Not Active", dateTime = "";
    private static final int USERID = 0;
    private static final int SITEID = 2;

    private boolean insertedProduct = false, insertedCustomer = false, insertedPrice = false, insertedAR = false;

    private int countProduct = 0, countFailProduct = 0, countPrice = 0, countFailPrice = 0, countCustomer = 0, countFailCustomer = 0, countAR = 0, countFailAR = 0;

    private ProgressDialog pDialogC, pDialogP, pDialogPc, pDialogAr;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        databaseHelper = new DatabaseHelper(ImportActivity.this);

        pDialogC = new ProgressDialog(this);
        pDialogC.setCancelable(false);

        pDialogP = new ProgressDialog(this);
        pDialogP.setCancelable(false);

        pDialogPc = new ProgressDialog(this);
        pDialogPc.setCancelable(false);

        pDialogAr = new ProgressDialog(this);
        pDialogAr.setCancelable(false);

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
                    startActivity(new Intent(ImportActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(ImportActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(ImportActivity.this, OrderActivity.class));
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
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(ImportActivity.this, LoginActivity.class));

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
        if(view.equals(buttonCustomer)){
            customerData();

        } else if(view.equals(buttonProduct)){
            productData();

        } else if(view.equals(buttonPrice)) {
            productPrice();

        } else if(view.equals(buttonAr)){
            arData();

        } else if(view.equals(importButton)){
            customerData();

            productPrice();

            arData();

            productData();

        }
    }

    private void productPrice() {
        pDialogPc.setMessage("Wait Import Price Product . . . ");
        pDialogPc.show();

        String URLPrice = "http://202.43.162.180:8082/PowerONEMobileWebService/ws_retrieve_productprice?arg_site="+dbSiteID;

        RequestQueue requestQueuePrice = Volley.newRequestQueue(ImportActivity.this);

        StringRequest stringRequestPrice = new StringRequest(
                Request.Method.GET,
                URLPrice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responsePrice) {
                        Log.i("responsePrice", responsePrice);
                        databaseHelper.emptyDatabase("MobProductPrice");

                        try {
                            prices = gson.fromJson(responsePrice, Price[].class);

                            for(int i = 0; i < responsePrice.length(); i++) {

                                urutID = prices[i].getUrutID();
                                siteID = prices[i].getSiteID();
                                productID = prices[i].getProductID();
                                productType = prices[i].getProductType();
                                salesPrice = prices[i].getSalesPrice();

                                insertedPrice = databaseHelper.insertPrice(urutID, siteID, productID, productType, salesPrice);

                                if(!insertedPrice){
                                    countFailPrice++;
                                }

                                countPrice++;
                                insertedPrice = false;

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        pDialogPc.dismiss();

                        if(countFailPrice == 0){
                            totalPrice.setText(""+ countPrice);

                        } else {
                            Toast.makeText(ImportActivity.this, "Please Import Price Again", Toast.LENGTH_SHORT).show();
                        }

                        countPrice = 0;
                        countFailPrice = 0;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialogPc.dismiss();

                        if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                            Toast.makeText(ImportActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                        }

                        error.printStackTrace();
                    }
                }
        );

        requestQueuePrice.add(stringRequestPrice);
    }

    private void arData() {
        pDialogAr.setMessage("Wait Import AR Balance . . . ");
        pDialogAr.show();

        String URLar = "http://202.43.162.180:8082/PowerONEMobileWebService/ws_retrieve_arbalance?arg_site="+dbSiteID+"&arg_salesman="+dbUserID;

        RequestQueue requestQueueAR = Volley.newRequestQueue(ImportActivity.this);

        StringRequest stringRequestAR = new StringRequest(
                Request.Method.GET,
                URLar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseAR) {
                        Log.i("responseAR", responseAR);

                        databaseHelper.emptyDatabase("MobARBalance");

                        try {
                            arBalances = gson.fromJson(responseAR, ARBalance[].class);


                            for(int i = 0; i < responseAR.length(); i++) {

                                siteID = arBalances[i].getSiteID();
                                custID = arBalances[i].getCustID();
                                salesmanID = arBalances[i].getSalesmanID();
                                invoiceID = arBalances[i].getInvoiceID();
                                dDueDate = arBalances[i].getdDueDate();
                                balanceAR = arBalances[i].getBalanceAR();
                                urutID = arBalances[i].getUrutID();

                                insertedAR = databaseHelper.insertAR(siteID, salesmanID, custID, invoiceID, dDueDate, balanceAR, urutID, 0);

                                if(!insertedAR){
                                    countFailAR++;
                                }

                                countAR++;
                                insertedAR = false;

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        pDialogAr.dismiss();

                        if(countFailAR == 0){
                            totalAr.setText(""+ countAR);

                        } else {
                            Toast.makeText(ImportActivity.this, "Please Import AR Balance Again", Toast.LENGTH_SHORT).show();
                        }

                        countAR = 0;
                        countFailAR = 0;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialogAr.dismiss();

                        if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                            Toast.makeText(ImportActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                        }

                        error.printStackTrace();
                    }
                }
        );

        requestQueueAR.add(stringRequestAR);

    }

    private void productData() {
        pDialogP.setMessage("Wait Import Product . . . ");
        pDialogP.show();

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

                                if(!insertedProduct){
                                    countFailProduct++;
                                }

                                countProduct++;
                                insertedProduct = false;

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        pDialogP.dismiss();

                        if(countFailProduct == 0){
                            totalProduct.setText(""+ countProduct);

                        } else {
                            Toast.makeText(ImportActivity.this, "Please Import Product Again", Toast.LENGTH_SHORT).show();

                        }

                        countProduct = 0;
                        countFailProduct = 0;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialogP.dismiss();

                if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                    Toast.makeText(ImportActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                }

                error.printStackTrace();
            }
        }
        );

        requestQueueProduct.add(stringRequestProduct);
    }

    private void customerData() {
        pDialogC.setMessage("Wait Import Customer Data . . . ");
        pDialogC.show();

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

                                insertedCustomer = databaseHelper.insertCustomer(urutID, siteID, salesmanID, custID, custName, custAddress, priceType, geoMapLong, geoMapLat, gpsMapLong, gpsMapLat, statusVisit, dateTime);

                                if(!insertedCustomer){
                                    countFailCustomer++;
                                }

                                countCustomer++;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        pDialogC.dismiss();

                        if(countFailCustomer == 0){
                            totalCustomer.setText(""+ countCustomer);

                        } else {
                            Toast.makeText(ImportActivity.this, "Please Import Customer Again", Toast.LENGTH_SHORT).show();

                        }

                        countCustomer = 0;
                        countFailCustomer = 0;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialogC.dismiss();

                        if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                            Toast.makeText(ImportActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                        }

                        error.printStackTrace();

                    }
                }
        );

        requestQueueCustomer.add(stringRequestCustomer);

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
            startActivity(new Intent(ImportActivity.this, MainActivity.class));

        }
    }

}
