package com.project.powerone.powerone;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.project.powerone.powerone.adapter.CustomerAdapter;
import com.project.powerone.powerone.adapter.ProductAdapter;
import com.project.powerone.powerone.pojo.Customer;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private RecyclerView rvmain;

    private DatabaseHelper databaseHelper;

    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        rvmain = (RecyclerView) findViewById(R.id.rvmain);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setHasFixedSize(true);
        rvmain.setAdapter(new CustomerAdapter(databaseHelper.getAllCustomer(), this));

        navigation();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    startActivity(new Intent(SalesActivity.this, ImportActivity.class));
                    finish();

                } else if(id == R.id.exportScreen){
                    startActivity(new Intent(SalesActivity.this, ExportActivity.class));
                    finish();

                } else if(id == R.id.visitScreen){
                    startActivity(new Intent(SalesActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(SalesActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(SalesActivity.this, OrderActivity.class));
                    finish();

                } else if(id == R.id.ARScreen){
                    startActivity(new Intent(SalesActivity.this, ARActivity.class));
                    finish();

                } else if(id == R.id.reportScreen){
                    startActivity(new Intent(SalesActivity.this, ReportActivity.class));
                    finish();

                } else if (id == R.id.photoScreen){
                    startActivity(new Intent(SalesActivity.this, PhotoActivity.class));
                    finish();

                } else if(id == R.id.passwordScreen){
                    startActivity(new Intent(SalesActivity.this, PasswordActivity.class));
                    finish();

                } else if(id == R.id.logout){
                    startActivity(new Intent(SalesActivity.this, LoginActivity.class));
                    finish();

                }

                return true;
            }
        });
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

            startActivity(new Intent(SalesActivity.this, MainActivity.class));
            finish();
        }
    }

}
