package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.ARBalance;

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

    public ARBalanceViewHolder(View itemView) {
        super(itemView);

        arInvoice = itemView.findViewById(R.id.arInvoice);
        arBalances = itemView.findViewById(R.id.arBalance);
        arDueDate = itemView.findViewById(R.id.arDueDate);

        arButton = itemView.findViewById(R.id.arButton);
    }

    public void bind(ARBalance arBalance, Activity activity) {

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

    }
}
