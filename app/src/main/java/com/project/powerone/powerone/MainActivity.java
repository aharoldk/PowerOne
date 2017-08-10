package com.project.powerone.powerone;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private DatabaseHelper databaseHelper;

    private Calendar calendar = Calendar.getInstance();

    private String dbUserID, dbDate, dateNow;
    private boolean doubleBackToExitPressedOnce = false;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDate();
        declarate();
        checkDate();
        navigation();

    }

    private void declarate() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = new Intent(getApplicationContext(), AngelosService.class);
        startService(intent);

        databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.loginSalesman();

        cursor.moveToFirst();
        dbUserID = cursor.getString(0);
        dbDate = cursor.getString(4);


    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    startActivity(new Intent(MainActivity.this, ImportActivity.class));
                    finish();

                } else if(id == R.id.exportScreen){
                    startActivity(new Intent(MainActivity.this, ExportActivity.class));
                    finish();

                } else if(id == R.id.visitScreen){
                    startActivity(new Intent(MainActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(MainActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(MainActivity.this, OrderActivity.class));
                    finish();

                } else if(id == R.id.ARScreen){
                    startActivity(new Intent(MainActivity.this, ARActivity.class));
                    finish();

                } else if(id == R.id.reportScreen){
                    startActivity(new Intent(MainActivity.this, ReportActivity.class));
                    finish();

                } else if (id == R.id.photoScreen){
                    startActivity(new Intent(MainActivity.this, PhotoActivity.class));
                    finish();

                } else if(id == R.id.passwordScreen){
                    startActivity(new Intent(MainActivity.this, PasswordActivity.class));
                    finish();

                } else if(id == R.id.logout){
                    finish();
                    stopService(intent);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }

                return true;
            }
        });

    }

    private void checkDate() {

        if(!dbDate.equals(dateNow)){
            databaseHelper.emptyDatabase("MobCustomer");
            databaseHelper.emptyDatabase("MobProduct");
            databaseHelper.emptyDatabase("MobProductPrice");
            databaseHelper.emptyDatabase("MobARBalance");

            boolean resultUpdate = databaseHelper.updateLogin(dbUserID, dateNow);

            if(resultUpdate){
                Toast.makeText(this, "Product, Customer, Product, and AR Balance are Empty, Please Import new data", Toast.LENGTH_SHORT).show();
            }

            checkdatebase();
        }

    }

    private void checkdatebase() {
        Cursor mCursorOrder, mCursorArPayment;

        mCursorOrder = databaseHelper.getAllOrdeR();
        mCursorArPayment = databaseHelper.getAllArPaymenT();

        int bTransfer = 0;
        if((mCursorOrder.getCount() > 0) || (mCursorArPayment.getCount() > 0)){
            while (mCursorOrder.moveToNext()){
                if(mCursorOrder.getInt(12) == 0){
                    bTransfer++;
                }

            }

            while (mCursorArPayment.moveToNext()){
                if(mCursorOrder.getInt(9) == 0){
                    bTransfer++;
                }

            }

            if(bTransfer > 0){
                Toast.makeText(this, "Please Export Data to the Server", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, ExportActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, ImportActivity.class));
            }

            finish();
        }
    }

    private void getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dateNow = dateFormat.format(calendar.getTime());
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

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }


}
