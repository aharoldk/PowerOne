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

    private TextView arInvoice, arBalances, arDueDate;
    private Button arButton;

    private String s;

    private LinearLayout linearGiro, linearTunai;

    private DatabaseHelper databaseHelper;
    private Cursor cursor;

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
            s = finalFormat.format(date);

            arDueDate.setText("Due Date : "+ s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        databaseHelper = new DatabaseHelper(activity);
        cursor = databaseHelper.getPayment(arBalance.getInvoiceID());

        if(cursor.getCount() == 1){
            arButton.setVisibility(View.GONE);
        }

        arButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View mView = activity.getLayoutInflater().inflate(R.layout.detail_arBalance, null);

                linearTunai = mView.findViewById(R.id.linearTunai);
                linearGiro = mView.findViewById(R.id.linearGiro);

                final Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra("custID", arBalance.getCustID());
                intent.putExtra("invoiceID", arBalance.getInvoiceID());

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

                builder.setCancelable(true);
                builder.setView(mView);
                builder.show();

            }
        });

    }
}
