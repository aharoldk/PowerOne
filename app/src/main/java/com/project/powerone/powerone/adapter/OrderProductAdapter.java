package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.OrderProduct;
import com.project.powerone.powerone.viewholder.OrderProductViewHolder;

import java.util.List;

/**
 * Created by aharoldk on 04/08/17.
 */

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductViewHolder> {
    private List<OrderProduct> list;
    private Activity activity;
    private String custID;

    public OrderProductAdapter(List<OrderProduct> list, Activity activity, String custID) {
        this.list = list;
        this.activity = activity;
        this.custID = custID;
    }

    @Override
    public OrderProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);

        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderProductViewHolder holder, int position) {
        holder.bind(list.get(position), activity, custID);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
