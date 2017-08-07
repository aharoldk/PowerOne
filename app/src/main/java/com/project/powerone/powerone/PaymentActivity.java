package com.project.powerone.powerone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.pojo.ARBalance;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {
    @BindView(R.id.tvPaymentNuGiro) TextView tvPaymentNuGiro;
    @BindView(R.id.tvNominal) TextView tvNomimnal;
    @BindView(R.id.tvPaymentDateShow) TextView tvPaymentDateShow;
    @BindView(R.id.tvPaymentDate) TextView tvPaymentDate;

    @BindView(R.id.etPaymentNuGiro) EditText etPaymentNuGiro;
    @BindView(R.id.etPaymentNominal) EditText etPaymentNominal;

    @BindView(R.id.btnPaymentDate) Button btnPaymentDate;
    @BindView(R.id.btnPayment) Button btnPayment;
    @BindView(R.id.remainPayment) TextView remainPayment;

    private String custID, invoiceID, paymentType, siteID, salesmanID, mNuGiro, mDate, mNominal;
    private int paymentMethod, costPayment;
    private long nominal;
    private int year_x, month_x, date_x;
    private boolean result;

    private static final int DIALOG_ID = 0;

    private DatabaseHelper databaseHelper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        databaseHelper = new DatabaseHelper(this);
        ButterKnife.bind(this);

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        date_x = calendar.get(Calendar.DAY_OF_MONTH);

        paymentMethod = getIntent().getExtras().getInt("extra");
        custID = getIntent().getExtras().getString("custID");
        invoiceID = getIntent().getExtras().getString("invoiceID");
        costPayment = getIntent().getExtras().getInt("costPayment");

        remainPayment.setText("Remaining Payment = Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(costPayment));

        tvPaymentDateShow.setVisibility(View.GONE);

        if(paymentMethod == 0){
            tvPaymentNuGiro.setVisibility(View.GONE);
            tvPaymentDateShow.setVisibility(View.GONE);
            tvPaymentDate.setVisibility(View.GONE);
            btnPaymentDate.setVisibility(View.GONE);
            etPaymentNuGiro.setVisibility(View.GONE);
            btnPaymentDate.setVisibility(View.GONE);

            paymentType = "T";
        } else if(paymentMethod == 1) {
            paymentType = "G";
        }

        btnPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNuGiro = etPaymentNuGiro.getText().toString();
                mNominal = etPaymentNominal.getText().toString();

                cursor = databaseHelper.loginSalesman();
                cursor.moveToFirst();
                salesmanID = cursor.getString(0);
                siteID = cursor.getString(2);

                if(paymentMethod == 0){
                    if(!TextUtils.isEmpty(mNominal)){

                        nominal = Integer.parseInt(mNominal);

                        if(costPayment < nominal){
                            Toast.makeText(PaymentActivity.this, "Please Check Your Nominal, Nominal more than Remaining Payment", Toast.LENGTH_SHORT).show();
                        } else {
                            result = databaseHelper.insertPayment(siteID, salesmanID, custID, invoiceID, nominal, paymentType, "", "");

                            if(result != true){
                                Toast.makeText(PaymentActivity.this, "Please Try Save Product Again", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PaymentActivity.this, "Payment Complete", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PaymentActivity.this, ARActivity.class));
                            }
                        }

                    } else {
                        Toast.makeText(PaymentActivity.this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
                    }
                } else if(paymentMethod == 1){
                    if(!TextUtils.isEmpty(mNuGiro) && !TextUtils.isEmpty(mNominal) && !TextUtils.isEmpty(mDate)){

                        nominal = Long.parseLong(mNominal);
                        if(costPayment < nominal){
                            Toast.makeText(PaymentActivity.this, "Please Check Your Nominal, Nominal more than Remaining Payment", Toast.LENGTH_SHORT).show();
                        } else {
                            result = databaseHelper.insertPayment(siteID, salesmanID, custID, invoiceID, nominal, paymentType, mNuGiro, mDate);

                            if(result != true){
                                Toast.makeText(PaymentActivity.this, "Please Try Save Product Again", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PaymentActivity.this, "Payment Complete", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PaymentActivity.this, ARActivity.class));
                            }

                        }

                    } else {
                        Toast.makeText(PaymentActivity.this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 0) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, date_x);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            year_x = year;
            month_x = month + 1;
            date_x = day;

            mDate = date_x+"-"+month_x+"-"+year_x;
            tvPaymentDateShow.setVisibility(View.VISIBLE);
            tvPaymentDateShow.setText("Tanggal Jatuh Tempo : "+mDate);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
