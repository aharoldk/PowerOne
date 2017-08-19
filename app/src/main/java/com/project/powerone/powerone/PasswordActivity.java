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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.powerone.powerone.pojo.Status;
import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordOld, passwordNew, passwordReNew;
    private Button passwordButton;

    private String dbUserID;
    private String oldPassword;
    private String newPassword;
    private String renewPassword;
    private String dateNow;
    private String timeNow;
    private boolean result;

    private DatabaseHelper databaseHelper;
    private Gson gson = new Gson();

    private Status[] statuses;
    private Calendar calendar = Calendar.getInstance();

    private ProgressDialog progressDialog;

    private static final int POSITION = 0;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        intent = new Intent(getApplicationContext(), AngelosService.class);

        databaseHelper = new DatabaseHelper(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        declarate();
        navigation();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, ImportActivity.class));

                } else if(id == R.id.exportScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, ExportActivity.class));

                } else if(id == R.id.visitScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, SalesActivity.class));

                } else if(id == R.id.produkScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, ProductActivity.class));

                } else if(id == R.id.orderScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, OrderActivity.class));

                } else if(id == R.id.ARScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, ARActivity.class));

                } else if(id == R.id.reportScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, ReportActivity.class));

                } else if (id == R.id.photoScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, PhotoActivity.class));

                } else if(id == R.id.passwordScreen){
                    finish();
                    startActivity(new Intent(PasswordActivity.this, PasswordActivity.class));

                } else if(id == R.id.logout){
                    finish();
                    stopService(intent);
                    startActivity(new Intent(PasswordActivity.this, LoginActivity.class));

                }

                return true;
            }
        });
    }

    private void declarate() {
        passwordOld = (EditText) findViewById(R.id.passwordOld);
        passwordNew = (EditText) findViewById(R.id.passwordNew);
        passwordReNew = (EditText) findViewById(R.id.passwordReNew);
        passwordButton = (Button) findViewById(R.id.passwordButton);

        passwordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(passwordButton)){
            progressDialog.setMessage("Wait . . .");
            progressDialog.show();

            oldPassword = passwordOld.getText().toString();
            newPassword = passwordNew.getText().toString();
            renewPassword = passwordReNew.getText().toString();

            if(!TextUtils.isEmpty(oldPassword) && !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(renewPassword)){
                checkPassword();

            } else {
                progressDialog.dismiss();

                Toast.makeText(this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkPassword() {
        Cursor cursor = databaseHelper.loginSalesman();

        cursor.moveToNext();
        dbUserID = cursor.getString(0);
        String dbPassword = cursor.getString(3);


        if(oldPassword.equals(dbPassword)){
            if(newPassword.equals(renewPassword)){
                exportData();

            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Your New Password Does Not Macth", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();

            Toast.makeText(this, "Your Old Password Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportData() {

        getDate();

        String URLlogin = "http://202.43.162.180:8082/poweronemobilewebservice/ws_update_salesman?arg_salesman="+dbUserID+"&arg_password="+newPassword+"&arg_lastlogin="+dateNow+"%"+timeNow;

        RequestQueue sQueueLogin = Volley.newRequestQueue(PasswordActivity.this);

        StringRequest sRequestLogin = new StringRequest(
                Request.Method.GET,
                URLlogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);

                        try {
                            statuses = gson.fromJson(response, Status[].class);

                            int xStatus = statuses[POSITION].getxStatus();

                            if(xStatus == 1){
                                result =databaseHelper.updatePassword(dbUserID, newPassword);

                                if(result){
                                    progressDialog.dismiss();
                                    Toast.makeText(PasswordActivity.this, "Your Password Updated", Toast.LENGTH_SHORT).show();
                                    passwordOld.setText("");
                                    passwordNew.setText("");
                                    passwordReNew.setText("");
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(PasswordActivity.this, "Please Try Again to Update", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PasswordActivity.this, "Request xStatus Login Error, Please Call Admin", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();

                            Toast.makeText(PasswordActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();

                Toast.makeText(PasswordActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        }
        );

        sQueueLogin.add(sRequestLogin);
    }

    private void getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy:HH:mm");

        dateNow = dateFormat.format(calendar.getTime());
        timeNow = timeFormat.format(calendar.getTime());
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
            startActivity(new Intent(PasswordActivity.this, MainActivity.class));
        }
    }
}
