package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Order;
import com.project.powerone.powerone.viewholder.OrderViewHolder;

import java.util.List;

/**
 * Created by aharoldk on 04/08/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    private List<Order> list;
    private Activity activity;
    private  TextView orderTotal;

    public OrderAdapter(List<Order> list, Activity activity, TextView orderTotal) {
        this.list = list;
        this.activity = activity;
        this.orderTotal = orderTotal;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.bind(list.get(position), activity, orderTotal);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
