package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.powerone.powerone.PaymentActivity;
import com.project.powerone.powerone.R;
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

    private TextView arInvoice, arBalances, arDueDate, tvListPayment;
    private Button arButton;

    private int totalPayment = 0;
    private int costPayment;

    private LinearLayout linearGiro, linearTunai, linearEntry;
    private StringBuffer stringBuffer;

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

        DatabaseHelper databaseHelper = new DatabaseHelper(activity);

        Cursor cursor = databaseHelper.getPayment(arBalance.getInvoiceID());
        stringBuffer = new StringBuffer();

        while(cursor.moveToNext()){
            int payment = cursor.getInt(5);

            String paymentType;
            if(cursor.getString(6).equals("G")){
                paymentType = "Giro";

                stringBuffer.append("Tipe Pembayaran : ").append(paymentType).append("\n").append("No. Giro : ").append(cursor.getString(7)).append("\n").append("Nominal Pembayaran : ").append(NumberFormat.getNumberInstance(Locale.US).format(payment)).append("\n").append("Tgl Jatuh Tempo Giro : ").append(cursor.getString(8)).append(" \n \n");

            } else if(cursor.getString(6).equals("T")) {
                paymentType = "Tunai";

                stringBuffer.append("Tipe Pembayaran : ").append(paymentType).append("\n").append("Nominal Pembayaran : ").append(NumberFormat.getNumberInstance(Locale.US).format(payment)).append("\n \n");
            }


            totalPayment = totalPayment + payment;
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

                        tvListPayment = entryView.findViewById(R.id.tvListPayment);

                        tvListPayment.setText(stringBuffer);

                        entryBuilder.setCancelable(true);
                        entryBuilder.setView(entryView);
                        entryBuilder.show();
                    }
                });

                builder.setCancelable(true);
                builder.setView(mView);
                builder.show();

            }
        });

    }
}
