package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Customer;
import com.project.powerone.powerone.viewholder.CustomerViewHolder;

import java.util.List;

/**
 * Created by aharoldk on 01/08/17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerViewHolder> {

    private List<Customer> list;
    private Activity activity;

    public CustomerAdapter(List<Customer> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        holder.bind(list.get(position), activity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
