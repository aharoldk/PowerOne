package com.project.powerone.powerone.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.viewholder.ProductViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aharoldk on 31/07/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> implements Filterable {
    private List<Product> list;
    private List<Product> mFilteredList;

    public ProductAdapter(List<Product> allProduct) {
        this.list = allProduct;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = list;
                } else {

                    List<Product> filteredList = new ArrayList<>();

                    for (Product product : list) {

                        if (product.getProductName().toLowerCase().contains(charString)) {

                            filteredList.add(product);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
