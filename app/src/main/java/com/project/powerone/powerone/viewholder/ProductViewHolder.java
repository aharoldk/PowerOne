package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Product;

/**
 * Created by aharoldk on 31/07/17.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView productName, groupName, subgroupName, bigPack, smallPack, prinsipalName, stockTotal;

    public ProductViewHolder(View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.productName);
        groupName = itemView.findViewById(R.id.groupName);
        subgroupName = itemView.findViewById(R.id.subgroupName);
        bigPack = itemView.findViewById(R.id.bigPack);
        smallPack = itemView.findViewById(R.id.smallPack);
        prinsipalName = itemView.findViewById(R.id.prinsipalName);
        stockTotal = itemView.findViewById(R.id.stockTotal);

    }

    public void bind(Product product) {
        productName.setText(product.getProductName());
        groupName.setText(product.getGroupProductName());
        subgroupName.setText(product.getSubGroupProductName());
        bigPack.setText(product.getBigPack());
        smallPack.setText(product.getSmallPack());
        prinsipalName.setText(product.getPrinsipalName());
        stockTotal.setText(""+ product.getQtyOnHand());

    }

}
