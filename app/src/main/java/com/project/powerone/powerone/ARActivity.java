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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.adapter.ARBalanceAdapter;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ARActivity extends AppCompatActivity {

    @BindView(R.id.arName) TextView arName;
    @BindView(R.id.arAddress) TextView arAddress;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private String custID, custName, custAddress;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        ButterKnife.bind(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHelper databaseHelper = new DatabaseHelper(ARActivity.this);

        Cursor cursor = databaseHelper.getCustomer();

        while (cursor.moveToNext()) {
            custID = cursor.getString(3);
            custName = cursor.getString(4);
            custAddress = cursor.getString(5);
        }

        if(custID == null){
            custID = "";

            Toast.makeText(this, "Please Visit Customer First", Toast.LENGTH_SHORT).show();
        }

        arName.setText(custName);
        arAddress.setText(custAddress);

        RecyclerView rvmain = (RecyclerView) findViewById(R.id.rvmain);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setHasFixedSize(true);
        rvmain.setAdapter(new ARBalanceAdapter(databaseHelper.getAllArBalance(custID), ARActivity.this));

        navigation();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, ImportActivity.class));

                } else if(id == R.id.exportScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, ExportActivity.class));

                } else if(id == R.id.visitScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, SalesActivity.class));

                } else if(id == R.id.produkScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, ProductActivity.class));

                } else if(id == R.id.orderScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, OrderActivity.class));

                } else if(id == R.id.ARScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, ARActivity.class));

                } else if(id == R.id.reportScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, ReportActivity.class));

                } else if (id == R.id.photoScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, PhotoActivity.class));

                } else if(id == R.id.passwordScreen){
                    finish();
                    startActivity(new Intent(ARActivity.this, PasswordActivity.class));

                } else if(id == R.id.logout){
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(ARActivity.this, LoginActivity.class));

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
            startActivity(new Intent(ARActivity.this, MainActivity.class));

        }
    }
}
