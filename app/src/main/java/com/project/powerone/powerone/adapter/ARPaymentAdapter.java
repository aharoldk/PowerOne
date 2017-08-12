package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.ARPayment;
import com.project.powerone.powerone.viewholder.ARPaymentViewHolder;

import java.util.List;

/**
 * Created by aharoldk on 11/08/17.
 */

public class ARPaymentAdapter extends RecyclerView.Adapter<ARPaymentViewHolder> {
    private List<ARPayment> list;
    private Activity activity;

    public ARPaymentAdapter(List<ARPayment> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ARPaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_detail_ar, parent, false);
        return new ARPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ARPaymentViewHolder holder, int position) {
        holder.bind(list.get(position), activity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
