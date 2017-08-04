package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.Locale;

/**
 * Created by aharoldk on 04/08/17.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private TextView orderName, orderPrice, orderBig, orderSmall, orderDisc1, orderDisc2, orderDisc3, orderTotalPrice;
    private ImageView orderDelete;

    private DatabaseHelper databaseHelper;
    private Cursor cursor;

    private String bigPack, smallPack, productName;
    private int delete, urutID;

    public OrderViewHolder(View itemView) {
        super(itemView);

        orderName = itemView.findViewById(R.id.orderName);
        orderPrice = itemView.findViewById(R.id.orderPrice);
        orderBig = itemView.findViewById(R.id.orderBig);
        orderSmall = itemView.findViewById(R.id.orderSmall);
        orderDisc1 = itemView.findViewById(R.id.orderDisc1);
        orderDisc2 = itemView.findViewById(R.id.orderDisc2);
        orderDisc3 = itemView.findViewById(R.id.orderDisc3);
        orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);

        orderDelete = itemView.findViewById(R.id.orderDelete);
    }

    public void bind(Order order, final Activity activity) {
        databaseHelper = new DatabaseHelper(activity);

        cursor = databaseHelper.getProduct(order.getProductID());

        while (cursor.moveToNext()){
            urutID = cursor.getInt(0);
            productName = cursor.getString(3);
            bigPack = cursor.getString(4);
            smallPack = cursor.getString(5);
        }

        orderName.setText(productName);
        orderPrice.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(order.getSalesPrice()));
        orderBig.setText(order.getQtyBig()+" /"+bigPack);
        orderSmall.setText(order.getQtySmall()+" /"+smallPack);

        if(order.getPctDisc1() !=0){
            orderDisc1.setText("Disc-1 : "+order.getPctDisc1()+"%");
        } else {
            orderDisc1.setText("");
        }

        if (order.getPctDisc2() != 0){
            orderDisc2.setText("Disc-2 : "+order.getPctDisc2()+"%");
        } else {
            orderDisc2.setText("");
        }

        if (order.getPctDisc3() != 0){
            orderDisc3.setText("Disc-3 : "+order.getPctDisc3()+"%");
        } else {
            orderDisc3.setText("");
        }

//        orderTotalPrice.setText();

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
