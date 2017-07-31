package com.project.powerone.powerone;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.project.powerone.powerone.adapter.ProductAdapter;
import com.project.powerone.powerone.sql.DatabaseHelper;

public class ProductActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private RecyclerView rvmain;

    private DatabaseHelper databaseHelper;

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(ProductActivity.this);

        rvmain = (RecyclerView) findViewById(R.id.rvmain);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setHasFixedSize(true);
        rvmain.setAdapter(new ProductAdapter(databaseHelper.getAllProduct()));

        navigation();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    startActivity(new Intent(ProductActivity.this, ImportActivity.class));
                    finish();

                } else if(id == R.id.exportScreen){
                    startActivity(new Intent(ProductActivity.this, ExportActivity.class));
                    finish();

                } else if(id == R.id.visitScreen){
                    startActivity(new Intent(ProductActivity.this, VisitActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(ProductActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(ProductActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.ARScreen){
                    startActivity(new Intent(ProductActivity.this, ARActivity.class));
                    finish();

                } else if(id == R.id.reportScreen){
                    startActivity(new Intent(ProductActivity.this, ReportActivity.class));
                    finish();

                } else if (id == R.id.photoScreen){
                    startActivity(new Intent(ProductActivity.this, PhotoActivity.class));
                    finish();

                } else if(id == R.id.passwordScreen){
                    startActivity(new Intent(ProductActivity.this, PasswordActivity.class));
                    finish();

                } else if(id == R.id.logout){
                    startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                    finish();

                }

                return true;
            }
        });
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                productAdapter.getFilter().filter(newText);
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

            startActivity(new Intent(ProductActivity.this, MainActivity.class));
            finish();
        }
    }
}
