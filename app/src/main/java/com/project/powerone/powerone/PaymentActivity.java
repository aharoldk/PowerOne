package com.project.powerone.powerone;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.pojo.ARBalance;
import com.project.powerone.powerone.sql.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {
    @BindView(R.id.tvPaymentNuGiro) TextView tvPaymentNuGiro;
    @BindView(R.id.tvNominal) TextView tvNomimnal;
    @BindView(R.id.tvPaymentDate) TextView tvPaymentDate;
    @BindView(R.id.etPaymentNuGiro) EditText etPaymentNuGiro;
    @BindView(R.id.etPaymentNominal) EditText etPaymentNominal;
    @BindView(R.id.etPaymentDate) EditText etPaymentDate;
    @BindView(R.id.btnPayment) Button btnPayment;

    private String custID, invoiceID, paymentType, siteID, salesmanID, mNuGiro, mDate, mNominal;
    private int paymentMethod;
    private boolean result;

    private DatabaseHelper databaseHelper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        databaseHelper = new DatabaseHelper(this);
        ButterKnife.bind(this);

        paymentMethod = getIntent().getExtras().getInt("extra");
        custID = getIntent().getExtras().getString("custID");
        invoiceID = getIntent().getExtras().getString("invoiceID");

        if(paymentMethod == 0){
            tvPaymentNuGiro.setVisibility(View.GONE);
            tvPaymentDate.setVisibility(View.GONE);
            etPaymentNuGiro.setVisibility(View.GONE);
            etPaymentDate.setVisibility(View.GONE);

            paymentType = "T";
        } else if(paymentMethod == 1) {
            paymentType = "G";
        }

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNuGiro = etPaymentNuGiro.getText().toString();
                mNominal = etPaymentNominal.getText().toString();
                mDate = etPaymentDate.getText().toString();

                cursor = databaseHelper.loginSalesman();
                cursor.moveToFirst();
                salesmanID = cursor.getString(0);
                siteID = cursor.getString(2);

                if(paymentMethod == 0){
                    if(!TextUtils.isEmpty(mNominal)){
                        result = databaseHelper.insertPayment(siteID, salesmanID, custID, invoiceID, mNominal, paymentType, "", "");

                        if(result !=true){
                            Toast.makeText(PaymentActivity.this, "Please Try Save Product Agan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Payment Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentActivity.this, ARBalance.class));
                        }

                    } else {
                        Toast.makeText(PaymentActivity.this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
                    }
                } else if(paymentMethod == 1){
                    if(!TextUtils.isEmpty(mNuGiro) && !TextUtils.isEmpty(mNominal) && !TextUtils.isEmpty(mDate)){
                        result = databaseHelper.insertPayment(siteID, salesmanID, custID, invoiceID, mNominal, paymentType, mNuGiro, mDate);

                        if(result !=true){
                            Toast.makeText(PaymentActivity.this, "Please Try Save Product Agan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Payment Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentActivity.this, ARBalance.class));
                        }

                    } else {
                        Toast.makeText(PaymentActivity.this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
