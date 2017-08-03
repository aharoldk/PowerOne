package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.ARBalance;
import com.project.powerone.powerone.viewholder.ARBalanceViewHolder;

import java.util.List;

/**
 * Created by aharoldk on 02/08/17.
 */

public class ARBalanceAdapter extends RecyclerView.Adapter<ARBalanceViewHolder> {
    private List<ARBalance> list;
    private Activity activity;

    public ARBalanceAdapter(List<ARBalance> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ARBalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_arbalance, parent, false);

        return new ARBalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ARBalanceViewHolder holder, int position) {
        holder.bind(list.get(position), activity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
