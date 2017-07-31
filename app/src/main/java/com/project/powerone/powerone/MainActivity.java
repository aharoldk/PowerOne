package com.project.powerone.powerone;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                startActivity(new Intent(MainActivity.this, VisitActivity.class));
                finish();

            } else if(id == R.id.produkScreen){
                startActivity(new Intent(MainActivity.this, ProductActivity.class));
                finish();

            } else if(id == R.id.orderScreen){
                startActivity(new Intent(MainActivity.this, SalesActivity.class));
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
