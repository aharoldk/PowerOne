package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.powerone.powerone.OrderProductActivity;
import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.OrderProduct;

/**
 * Created by aharoldk on 04/08/17.
 */

public class OrderProductViewHolder extends RecyclerView.ViewHolder {

    private TextView productName, groupName, subgroupName, bigPack, smallPack, prinsipalName, stockTotal;


    public OrderProductViewHolder(View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.productName);
        groupName = itemView.findViewById(R.id.groupName);
        subgroupName = itemView.findViewById(R.id.subgroupName);
        bigPack = itemView.findViewById(R.id.bigPack);
        smallPack = itemView.findViewById(R.id.smallPack);
        prinsipalName = itemView.findViewById(R.id.prinsipalName);
        stockTotal = itemView.findViewById(R.id.stockTotal);
    }

    public void bind(final OrderProduct orderProduct, final Activity activity, final String custID) {
        productName.setText(orderProduct.getProductName());
        groupName.setText(orderProduct.getGroupProductName());
        subgroupName.setText(orderProduct.getSubGroupProductName());
        bigPack.setText(orderProduct.getBigPack());
        smallPack.setText(orderProduct.getSmallPack());
        prinsipalName.setText(orderProduct.getPrinsipalName());
        stockTotal.setText(""+ orderProduct.getQtyOnHand());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, OrderProductActivity.class);
                intent.putExtra("siteID", orderProduct.getSiteID());
                intent.putExtra("custID", custID);
                intent.putExtra("productName", orderProduct.getProductName());
                intent.putExtra("noOfPack", orderProduct.getNoOfPack());
                intent.putExtra("productID", orderProduct.getProductID());
                intent.putExtra("bigPack", orderProduct.getBigPack());
                intent.putExtra("smallPack", orderProduct.getSmallPack());
                intent.putExtra("salesPrice", orderProduct.getSalesPrice());

                activity.startActivity(intent);
            }
        });
    }
}
