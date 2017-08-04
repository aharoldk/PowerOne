package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public OrderAdapter(List<Order> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.bind(list.get(position), activity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
