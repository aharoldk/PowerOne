package com.project.powerone.powerone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.powerone.powerone.service.AngelosService;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity {
    @BindView(R.id.tvCustomer) TextView mCustomer;
    @BindView(R.id.tvOrder) TextView mOrder;
    @BindView(R.id.tvOmzet) TextView mOmzet;
    @BindView(R.id.tvTotalTagihan) TextView mTTagihan;
    @BindView(R.id.tvTotalRpTagihan) TextView mRpTagihan;
    @BindView(R.id.tvTotalRpKas) TextView mRpKas;
    @BindView(R.id.tvTotalRpGiro) TextView mRpGiro;
    @BindView(R.id.tvTotalExportSales) TextView mExSales;
    @BindView(R.id.tvTotalExportTagihan) TextView mExTagihan;
    @BindView(R.id.btnPrinsipal) Button btnPrinsipal;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private DatabaseHelper databaseHelper;

    private int iCustActive = 0;
    private int iCustOrder = 0;
    private int iTagihanDone = 0;
    private int iTotalTagihan = 0;
    private int iTotalGiro = 0;
    private int iTotalTunai = 0;
    private int iARPdone = 0;
    private int iOrderdone = 0;
    private double dTotalOmzet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        databaseHelper = new DatabaseHelper(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation();

        ccustomer();
        corder();
        comzet();
        ctagihan();
        cexport();

    }

    private void cexport() {
        Cursor cARPayment = databaseHelper.getAllArPaymenT();
        int iARPayment = cARPayment.getCount();

        while (cARPayment.moveToNext()){
            if(cARPayment.getInt(10) == 1) {
                iARPdone++;
            }
        }
        mExTagihan.setText(iARPdone+" of "+iARPayment);
        cARPayment.close();

        Cursor cOrder = databaseHelper.getAllOrdeR();

        int iOrder = cARPayment.getCount();

        while (cOrder.moveToNext()){
            if(cOrder.getInt(12) == 1) {
                iOrderdone++;
            }
        }
        mExSales.setText(iOrderdone+" of "+iOrder);
        cOrder.close();
    }



    private void ccustomer() {
        Cursor cCustomer = databaseHelper.getAllCustomerR();

        int iCustTotal = cCustomer.getCount();

        while(cCustomer.moveToNext()){
            if(cCustomer.getString(11).equals("Active")){
                iCustActive++;
            }

        }

        mCustomer.setText(iCustActive+" of "+ iCustTotal);

        cCustomer.close();
    }

    private void corder() {
        Cursor cOrder = databaseHelper.getOrder();

        while (cOrder.moveToNext()){
            if(cOrder.getInt(cOrder.getColumnIndex("total")) > 0) {
                iCustOrder++;
            }

        }

        mOrder.setText(iCustOrder+" of "+iCustActive);

        cOrder.close();
    }

    private void comzet() {
        Cursor cProduct = databaseHelper.getOmzet();

        final StringBuffer stringBuffer = new StringBuffer();

        while (cProduct.moveToNext()) {

            stringBuffer.append(cProduct.getString(0)).append(" : \t\tRp. ").append(NumberFormat.getNumberInstance(Locale.US).format(cProduct.getInt(1))).append("\n \n");


            dTotalOmzet += cProduct.getInt(1);
        }

        btnPrinsipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderAR = new AlertDialog.Builder(ReportActivity.this);
                View arView = getLayoutInflater().inflate(R.layout.detail_prinsipal, null);

                TextView tvPrinsipalList = arView.findViewById(R.id.tvPrinsipalList);

                tvPrinsipalList.setText(stringBuffer);

                builderAR.setCancelable(true);
                builderAR.setView(arView);
                builderAR.show();
            }
        });


        mOmzet.setText("Rp. "+NumberFormat.getNumberInstance(Locale.US).format(dTotalOmzet));

        cProduct.close();

    }

    private void ctagihan() {
        Cursor cTagihan = databaseHelper.getTagihan();
        int iTagihan = cTagihan.getCount();

        while (cTagihan.moveToNext()){
            if(cTagihan.getInt(7) == 1){
                iTagihanDone++;
            }
        }

        mTTagihan.setText(iTagihanDone+" of "+iTagihan);

        cTagihan.close();

        Cursor cRpTagihan = databaseHelper.getRpTagihan();

        while (cRpTagihan.moveToNext()){
            int iRpTagihan = cRpTagihan.getInt(5);

            if(cRpTagihan.getString(6).equals("G")) {
                iTotalGiro += iRpTagihan;

            } else if(cRpTagihan.getString(6).equals("T")) {
                iTotalTunai += iRpTagihan;

            }
            iTotalTagihan += iRpTagihan;
        }

        mRpTagihan.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(iTotalTagihan));
        mRpKas.setText("Rp. "+NumberFormat.getNumberInstance(Locale.US).format(iTotalTunai));
        mRpGiro.setText("Rp. "+NumberFormat.getNumberInstance(Locale.US).format(+iTotalGiro));

        cRpTagihan.close();
    }


    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, ImportActivity.class));

                } else if(id == R.id.exportScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, ExportActivity.class));

                } else if(id == R.id.visitScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, SalesActivity.class));

                } else if(id == R.id.produkScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, ProductActivity.class));

                } else if(id == R.id.orderScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, OrderActivity.class));

                } else if(id == R.id.ARScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, ARActivity.class));

                } else if(id == R.id.reportScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, ReportActivity.class));

                } else if (id == R.id.photoScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, PhotoActivity.class));

                } else if(id == R.id.passwordScreen){
                    finish();
                    startActivity(new Intent(ReportActivity.this, PasswordActivity.class));

                } else if(id == R.id.logout){
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(ReportActivity.this, LoginActivity.class));

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
            startActivity(new Intent(ReportActivity.this, MainActivity.class));

        }
    }
}

