package com.project.powerone.powerone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.project.powerone.powerone.adapter.ProductAdapter;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private RecyclerView rvmain;

    private DatabaseHelper databaseHelper;

    private ProductAdapter productAdapter;

    public EditText searchEditText;
    private List<Product> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(ProductActivity.this);

        rvmain = (RecyclerView) findViewById(R.id.rvmain);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setHasFixedSize(true);
        rvmain.setAdapter(new ProductAdapter(databaseHelper.getAllProduct(), ProductActivity.this));


        searchEditText = (EditText) findViewById(R.id.searchEditText);

        navigation();
        addTextListener();
    }

    private void addTextListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();


                list = databaseHelper.getAllProduct();

                final List<Product> filteredList = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {

                    final String text = list.get(i).getProductName().toLowerCase();

                    if (text.contains(query)) {

                        filteredList.add(list.get(i));
                    }
                }

                rvmain.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                productAdapter = new ProductAdapter(filteredList, ProductActivity.this);
                rvmain.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();  // data set changed
            }
        });
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
                    startActivity(new Intent(ProductActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(ProductActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(ProductActivity.this, OrderActivity.class));
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
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(ProductActivity.this, LoginActivity.class));

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
            startActivity(new Intent(ProductActivity.this, MainActivity.class));
        }
    }
}
