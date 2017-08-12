package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.ARActivity;
import com.project.powerone.powerone.PaymentActivity;
import com.project.powerone.powerone.R;
import com.project.powerone.powerone.adapter.ARPaymentAdapter;
import com.project.powerone.powerone.pojo.ARBalance;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aharoldk on 02/08/17.
 */

public class ARBalanceViewHolder extends RecyclerView.ViewHolder{

    private TextView arInvoice, arBalances, arDueDate;
    private Button arButton;

    private int totalPayment = 0;
    private int costPayment, countConfirm = 0;

    private DatabaseHelper databaseHelper;

    private LinearLayout linearGiro, linearTunai, linearEntry;

    public ARBalanceViewHolder(View itemView) {
        super(itemView);

        arInvoice = itemView.findViewById(R.id.arInvoice);
        arBalances = itemView.findViewById(R.id.arBalance);
        arDueDate = itemView.findViewById(R.id.arDueDate);

        arButton = itemView.findViewById(R.id.arButton);
    }

    public void bind(final ARBalance arBalance, final Activity activity) {

        arInvoice.setText(arBalance.getInvoiceID());
        arBalances.setText("Balance : Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(arBalance.getBalanceAR()));

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = simpleDateFormat.parse(arBalance.getdDueDate());
            DateFormat finalFormat = new SimpleDateFormat("dd-MM-yyyy");
            String s = finalFormat.format(date);

            arDueDate.setText("Due Date : "+ s);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        databaseHelper = new DatabaseHelper(activity);

        final Cursor cursor = databaseHelper.getPayment(arBalance.getInvoiceID());

        while(cursor.moveToNext()) {
            totalPayment = totalPayment + cursor.getInt(5);

            if(cursor.getInt(9) == 0){
                countConfirm++;
            }

        }

        arButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View mView = activity.getLayoutInflater().inflate(R.layout.detail_arbalance, null);

                linearEntry = mView.findViewById(R.id.linearEntry);
                linearTunai = mView.findViewById(R.id.linearTunai);
                linearGiro = mView.findViewById(R.id.linearGiro);

                if(totalPayment >= arBalance.getBalanceAR()){
                    linearGiro.setVisibility(View.GONE);
                    linearTunai.setVisibility(View.GONE);
                }

                costPayment = (int) (arBalance.getBalanceAR() - totalPayment);

                final Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra("custID", arBalance.getCustID());
                intent.putExtra("invoiceID", arBalance.getInvoiceID());
                intent.putExtra("costPayment", costPayment);

                linearTunai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("extra", 0);
                        activity.startActivity(intent);

                    }
                });

                linearGiro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("extra", 1);
                        activity.startActivity(intent);
                    }
                });

                linearEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder entryBuilder = new AlertDialog.Builder(activity);
                        View entryView = activity.getLayoutInflater().inflate(R.layout.detail_invoice, null);

                        RecyclerView rvmainARPayment = entryView.findViewById(R.id.rvmain);
                        Button btnConfirmPayment = entryView.findViewById(R.id.btnConfirmPayment);

                        rvmainARPayment.setLayoutManager(new LinearLayoutManager(activity));
                        rvmainARPayment.setHasFixedSize(true);
                        rvmainARPayment.setAdapter(new ARPaymentAdapter(databaseHelper.getAllPayment(arBalance.getInvoiceID()), activity));

                        entryBuilder.setCancelable(true);
                        entryBuilder.setView(entryView);
                        entryBuilder.show();

                        if(cursor.getCount() == 0) {
                            btnConfirmPayment.setVisibility(View.GONE);
                        }

                        if(countConfirm == 0) {
                            btnConfirmPayment.setVisibility(View.GONE);
                        }

                        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int countFail = 0;

                                Cursor cursorGetpayment = databaseHelper.getPayment(arBalance.getInvoiceID());

                                while(cursorGetpayment.moveToNext()) {

                                    boolean paymentUpdate = databaseHelper.updateARPayment(arBalance.getInvoiceID());

                                    if(!paymentUpdate){
                                        countFail++;
                                    }
                                }

                                if(countFail > 0){
                                    Toast.makeText(activity, "Please Confirm Payment Again", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, "All Payment Confirm", Toast.LENGTH_SHORT).show();
                                    activity.finish();
                                    activity.overridePendingTransition(0, 0);
                                    activity.startActivity(new Intent(activity, ARActivity.class));

                                }
                            }
                        });
                    }
                });

                builder.setCancelable(true);
                builder.setView(mView);
                builder.show();

            }
        });

    }
}
