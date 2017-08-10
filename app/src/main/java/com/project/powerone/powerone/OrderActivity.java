package com.project.powerone.powerone;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.adapter.OrderAdapter;
import com.project.powerone.powerone.adapter.OrderProductAdapter;
import com.project.powerone.powerone.pojo.OrderProduct;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private String custName;
    private String custAddress;
    private String custPriceType;
    private String custID;
    private int countFail = 0;
    private boolean result;

    private List<OrderProduct> list = new ArrayList<>();

    private RecyclerView rvmainProduct;

    private LinearLayout orderButtonAdd;
    private EditText searchEditText;
    private Button orderConfirm;

    private DatabaseHelper databaseHelper;
    private OrderProductAdapter orderProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        databaseHelper = new DatabaseHelper(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView orderName = (TextView) findViewById(R.id.orderName);
        TextView orderAddress = (TextView) findViewById(R.id.orderAddress);
        TextView orderPriceType = (TextView) findViewById(R.id.orderPriceType);
        orderButtonAdd = (LinearLayout) findViewById(R.id.orderButtonAdd);
        TextView orderTotal = (TextView) findViewById(R.id.orderTotal);
        orderConfirm = (Button) findViewById(R.id.orderConfirm);

        orderButtonAdd.setOnClickListener(this);
        orderConfirm.setOnClickListener(this);

        Cursor cursor = databaseHelper.getCustomer();

        while (cursor.moveToNext()) {
            custID = cursor.getString(3);
            custName = cursor.getString(4);
            custAddress = cursor.getString(5);
            custPriceType = cursor.getString(6);
        }

        if(custID == null){
            custID = "";
            orderButtonAdd.setVisibility(View.GONE);
            orderPriceType.setVisibility(View.GONE);
            orderTotal.setVisibility(View.GONE);
            orderConfirm.setVisibility(View.GONE);
            Toast.makeText(this, "Please Visit Customer First", Toast.LENGTH_SHORT).show();
        }

        RecyclerView rvmain = (RecyclerView) findViewById(R.id.rvmain);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setHasFixedSize(true);
        rvmain.setAdapter(new OrderAdapter(databaseHelper.getAllOrder(custID), OrderActivity.this));

        navigation();

        orderName.setText(custName);
        orderAddress.setText(custAddress);
        orderPriceType.setText("Price Type : "+ custPriceType);

        Cursor cursorOrder = databaseHelper.getCurrentOrder(custID);

        int totalOrder = 0;

        while (cursorOrder.moveToNext()) {
            totalOrder += ((cursorOrder.getInt(1) * cursorOrder.getInt(3))+(cursorOrder.getInt(2) * cursorOrder.getInt(3) / cursorOrder.getInt(4)));
        }

        orderTotal.setText("Total Rp. "+NumberFormat.getNumberInstance(Locale.US).format(totalOrder));

    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(checkDatabaseBeforeMove()){
                    if(id == R.id.importScreen){
                        startActivity(new Intent(OrderActivity.this, ImportActivity.class));
                        finish();

                    } else if(id == R.id.exportScreen){
                        startActivity(new Intent(OrderActivity.this, ExportActivity.class));
                        finish();

                    } else if(id == R.id.visitScreen){
                        startActivity(new Intent(OrderActivity.this, SalesActivity.class));
                        finish();

                    } else if(id == R.id.produkScreen){
                        startActivity(new Intent(OrderActivity.this, ProductActivity.class));
                        finish();

                    } else if(id == R.id.orderScreen){
                        startActivity(new Intent(OrderActivity.this, OrderActivity.class));
                        finish();

                    } else if(id == R.id.ARScreen){
                        startActivity(new Intent(OrderActivity.this, ARActivity.class));
                        finish();

                    } else if(id == R.id.reportScreen){
                        startActivity(new Intent(OrderActivity.this, ReportActivity.class));
                        finish();

                    } else if (id == R.id.photoScreen){
                        startActivity(new Intent(OrderActivity.this, PhotoActivity.class));
                        finish();

                    } else if(id == R.id.passwordScreen){
                        startActivity(new Intent(OrderActivity.this, PasswordActivity.class));
                        finish();

                    } else if(id == R.id.logout){
                        Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                        stopService(intent);

                        finish();
                        startActivity(new Intent(OrderActivity.this, LoginActivity.class));

                    }
                }



                return true;
            }
        });
    }

    private boolean checkDatabaseBeforeMove() {
        Cursor cursor = databaseHelper.getCustOrder(custID);

        int notyetSave = 0;

        while (cursor.moveToNext()) {

            if(cursor.getInt(11) == 0){
                notyetSave++;
            }
        }

        if(notyetSave > 0){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(OrderActivity.this, R.style.MyAlertDialogStyle);
            } else {
                builder = new AlertDialog.Builder(OrderActivity.this);
            }
            builder.setTitle("Not Yet Save Order")
                    .setMessage("Are you sure you want to move this activity?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result = true;
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result = false;
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            result = true;
        }

        return result;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(orderButtonAdd)){
            productActivity();
        } else if (view.equals(orderConfirm)){

            Cursor cursorUpdate = databaseHelper.getCustOrder(custID);

            while (cursorUpdate.moveToNext()) {

                String productID = cursorUpdate.getString(4);
                boolean productUpdate = databaseHelper.updateOrderProduct(productID);

                if(!productUpdate){
                    countFail++;
                }
            }
            if(countFail > 0){
                Toast.makeText(this, "Please Order Confirm Again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "All Order Confirm Success added", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                startActivity(new Intent(this, OrderActivity.class));

            }
        }
    }

    private void productActivity() {

        AlertDialog.Builder builderAR = new AlertDialog.Builder(OrderActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.row_order_product, null);

        searchEditText = mView.findViewById(R.id.searchEditText);

        rvmainProduct = mView.findViewById(R.id.rvmain);
        rvmainProduct.setLayoutManager(new LinearLayoutManager(this));
        rvmainProduct.setHasFixedSize(true);
        rvmainProduct.setAdapter(new OrderProductAdapter(databaseHelper.getAllProductPrice(custPriceType), OrderActivity.this, custID));

        builderAR.setCancelable(true);
        builderAR.setView(mView);
        builderAR.show();

        addTextListener();
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
            if(checkDatabaseBeforeMove()){
                super.onBackPressed();

                finish();
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
            }

        }
    }

    private void addTextListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();


                list = databaseHelper.getAllProductPrice(custPriceType);

                final List<OrderProduct> filteredList = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {

                    final String text = list.get(i).getProductName().toLowerCase();

                    if (text.contains(query)) {

                        filteredList.add(list.get(i));
                    }
                }

                rvmainProduct.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
                orderProductAdapter = new OrderProductAdapter(filteredList, OrderActivity.this, custID);
                rvmainProduct.setAdapter(orderProductAdapter);
                orderProductAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}
