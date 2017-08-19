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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.powerone.powerone.adapter.CustomerAdapter;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

public class SalesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private DatabaseHelper databaseHelper;

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

        RecyclerView rvmain = (RecyclerView) findViewById(R.id.rvmain);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setHasFixedSize(true);
        rvmain.setAdapter(new CustomerAdapter(databaseHelper.getAllCustomer(), this));

        Button btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = databaseHelper.loginSalesman();
                cursor.moveToNext();
                int count = cursor.getInt(5) + 1;
                if(databaseHelper.updateCountMap(cursor.getString(0), count)){
                    startActivity(new Intent(SalesActivity.this, MapsActivity.class));
                } else {
                    Toast.makeText(SalesActivity.this, "Press Again", Toast.LENGTH_SHORT).show();

                }

            }
        });

        navigation();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, ImportActivity.class));

                } else if(id == R.id.exportScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, ExportActivity.class));

                } else if(id == R.id.visitScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, SalesActivity.class));

                } else if(id == R.id.produkScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, ProductActivity.class));

                } else if(id == R.id.orderScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, OrderActivity.class));

                } else if(id == R.id.ARScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, ARActivity.class));

                } else if(id == R.id.reportScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, ReportActivity.class));

                } else if (id == R.id.photoScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, PhotoActivity.class));

                } else if(id == R.id.passwordScreen){
                    finish();
                    startActivity(new Intent(SalesActivity.this, PasswordActivity.class));

                } else if(id == R.id.logout){
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(SalesActivity.this, LoginActivity.class));

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
            startActivity(new Intent(SalesActivity.this, MainActivity.class));

        }
    }

}
