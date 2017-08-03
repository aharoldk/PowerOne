package com.project.powerone.powerone.viewholder;

import android.app.Activity;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.ImportActivity;
import com.project.powerone.powerone.ProductActivity;
import com.project.powerone.powerone.R;
import com.project.powerone.powerone.pojo.Product;
import com.project.powerone.powerone.sql.DatabaseHelper;

/**
 * Created by aharoldk on 31/07/17.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView productName, groupName, subgroupName, bigPack, smallPack, prinsipalName, stockTotal;

    private DatabaseHelper databaseHelper;

    private int i = 0;

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

    public void bind(final Product product, final Activity activity) {
        productName.setText(product.getProductName());
        groupName.setText(product.getGroupProductName());
        subgroupName.setText(product.getSubGroupProductName());
        bigPack.setText(product.getBigPack());
        smallPack.setText(product.getSmallPack());
        prinsipalName.setText(product.getPrinsipalName());
        stockTotal.setText(""+ product.getQtyOnHand());


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(i == 2) {
                            databaseHelper = new DatabaseHelper(activity);

                            Cursor cursor = databaseHelper.getPrice(product.getProductID());

                            if (cursor.getCount() == 0) {
                                Toast.makeText(activity, "Please Import Price First", Toast.LENGTH_SHORT).show();
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                View mView = activity.getLayoutInflater().inflate(R.layout.detail_product, null);

                                TextView priceList = mView.findViewById(R.id.listPrice);

                                StringBuffer stringBuffer = new StringBuffer();

                                while(cursor.moveToNext()){
                                    stringBuffer.append("Product Type : "+ cursor.getString(3)+ "\n");
                                    stringBuffer.append("Sales Price : "+ cursor.getString(4)+ "\n");
                                }


                                priceList.setText(stringBuffer);

                                builder.setCancelable(true);
                                builder.setView(mView);
                                builder.setTitle("Product Price");
                                builder.show();
                            }

                            i = 0;
                        }
                    }
                }, 300);

            }
        });
    }

}
