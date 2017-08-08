package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.OrderActivity;
import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Order;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by aharoldk on 04/08/17.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private TextView orderName, orderExtra, orderPrice, orderQty, orderDisc;
    private ImageView orderDelete;

    private DatabaseHelper databaseHelper;

    private String productName;
    private int delete, urutID, noOfPack;

    public OrderViewHolder(View itemView) {
        super(itemView);

        orderName = itemView.findViewById(R.id.orderName);
        orderExtra = itemView.findViewById(R.id.orderExtra);
        orderQty = itemView.findViewById(R.id.orderQty);
        orderPrice = itemView.findViewById(R.id.orderPrice);
        orderDisc = itemView.findViewById(R.id.orderDisc);

        orderDelete = itemView.findViewById(R.id.orderDelete);
    }

    public void bind(Order order, final Activity activity, TextView orderTotal) {
        databaseHelper = new DatabaseHelper(activity);

        Cursor cursor = databaseHelper.getProduct(order.getProductID());

        while (cursor.moveToNext()){
            urutID = cursor.getInt(0);
            productName = cursor.getString(3);
        }

        orderName.setText(productName);
        orderPrice.setText("H. "+ NumberFormat.getNumberInstance(Locale.US).format(order.getSalesPrice()));
        orderExtra.setText("/ "+noOfPack+" pcs");
        orderQty.setText("Q. "+order.getQtyBig()+"/"+order.getQtySmall());
        orderDisc.setText("%. "+order.getPctDisc1()+"/"+order.getPctDisc2()+"/"+order.getPctDisc3());

        if(order.getbConfirm() == 1){
            orderDelete.setVisibility(View.GONE);

        } else {
            orderDelete.setVisibility(View.VISIBLE);

            orderDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = Integer.toString(urutID);
                    delete = databaseHelper.deleteOrder(id);

                    if(delete > 0) {
                        activity.startActivity(new Intent(activity, OrderActivity.class));
                        activity.finish();

                    } else {
                        Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
