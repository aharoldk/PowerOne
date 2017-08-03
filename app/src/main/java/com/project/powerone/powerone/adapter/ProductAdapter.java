package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.powerone.powerone.ProductActivity;
import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.viewholder.ProductViewHolder;

import java.util.List;

/**
 * Created by aharoldk on 31/07/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private List<Product> list;
    private Activity activity;

    public ProductAdapter(List<Product> allProduct, ProductActivity productActivity) {
        this.list = allProduct;
        this.activity = productActivity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bind(list.get(position), activity);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
