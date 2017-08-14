package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.ARPayment;
import com.project.powerone.powerone.sql.DatabaseHelper;

/**
 * Created by aharoldk on 11/08/17.
 */

public class ARPaymentViewHolder extends RecyclerView.ViewHolder {
    private TextView arTypePayment;
    private TextView arNoGiro;
    private TextView arNoPayment;
    private TextView arDateDue;
    private ImageView arDelete;

    public ARPaymentViewHolder(View itemView) {
        super(itemView);

        arTypePayment = itemView.findViewById(R.id.arTypePayment);
        arNoGiro = itemView.findViewById(R.id.arNoGiro);
        arNoPayment = itemView.findViewById(R.id.arNoPayment);
        arDateDue = itemView.findViewById(R.id.arDateDue);

        arDelete = itemView.findViewById(R.id.arDelete);
    }

    public void bind(final ARPayment arPayment, final Activity activity) {
        String payment = null;

        if(arPayment.getPaymentType().equals("G")){
            payment = "Giro";
        } else if(arPayment.getPaymentType().equals("T")){
            payment = "Tunai";

            arNoGiro.setVisibility(View.GONE);
            arDateDue.setVisibility(View.GONE);
        }
        
        arTypePayment.setText("Tipe Pembayaran : "+payment);
        arNoGiro.setText("No. Giro : "+arPayment.getBillyetNo());
        arNoPayment.setText("Nominal Pembayaran : "+arPayment.getNominalPayment());
        arDateDue.setText("Jatuh Tempo Giro : "+arPayment.getBillyetDueDate());

        if(arPayment.getbTransfer() == 1){
            arDelete.setVisibility(View.GONE);
        }

        arDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(activity);

                String id = Integer.toString(arPayment.getUrutID());

                int delete = databaseHelper.deletePayment(id);

                if(delete > 0) {
                    activity.finish();
                    Intent intent = activity.getIntent();

                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(intent);

                } else {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
