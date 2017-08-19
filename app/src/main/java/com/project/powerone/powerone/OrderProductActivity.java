package com.project.powerone.powerone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.sql.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class OrderProductActivity extends AppCompatActivity implements View.OnClickListener {

    private String siteID;
    private String custID;
    private String productID;
    private String sBig;
    private String sSmall;
    private String sDisc1;
    private String sDisc2;
    private String sDisc3;
    private int salesPrice;
    private int iBig;
    private int iSmall;
    private double iDisc1, iDisc2, iDisc3;

    private EditText qtyBig, qtySmall, pctDisc1, pctDisc2, pctDisc3;
    private Button productButton;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        databaseHelper = new DatabaseHelper(this);

        initialize();
    }

    private void initialize() {
        siteID = getIntent().getExtras().getString("siteID");
        custID = getIntent().getExtras().getString("custID");
        String productName = getIntent().getExtras().getString("productName");
        productID = getIntent().getExtras().getString("productID");
        int noOfPack = getIntent().getExtras().getInt("noOfPack");
        String smallPack = getIntent().getExtras().getString("smallPack");
        String bigpack = getIntent().getExtras().getString("bigPack");
        salesPrice = getIntent().getExtras().getInt("salesPrice");

        TextView productNameT = (TextView) findViewById(R.id.productName);
        TextView productPrice = (TextView) findViewById(R.id.productPrice);
        TextView productBig = (TextView) findViewById(R.id.productBig);
        TextView productSmall = (TextView) findViewById(R.id.productSmall);
        productButton = (Button) findViewById(R.id.productButton);
        pctDisc1 = (EditText) findViewById(R.id.pctDisc1);
        pctDisc2 = (EditText) findViewById(R.id.pctDisc2);
        pctDisc3 = (EditText) findViewById(R.id.pctDisc3);
        qtyBig = (EditText) findViewById(R.id.qtyBig);
        qtySmall = (EditText) findViewById(R.id.qtySmall);

        productNameT.setText(productName +"\n /"+ bigpack +" = "+noOfPack+" /"+ smallPack);
        productPrice.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(salesPrice));
        productSmall.setText("*insert qty /"+ smallPack);
        productBig.setText("*insert qty /"+ bigpack);

        productButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.equals(productButton)){
            sBig = qtyBig.getText().toString();
            sSmall = qtySmall.getText().toString();
            sDisc1 = pctDisc1.getText().toString();
            sDisc2 = pctDisc2.getText().toString();
            sDisc3 = pctDisc3.getText().toString();

            if(!TextUtils.isEmpty(sBig) || !TextUtils.isEmpty(sSmall)){

                set0();

                Cursor cursor = databaseHelper.loginSalesman();
                cursor.moveToFirst();
                String salesmanID = cursor.getString(0);

                int bConfirm = 0;
                int bTransfer = 0;

                boolean insertOrder = databaseHelper.insertSalesOrder(siteID, salesmanID, custID, productID, iBig, iSmall, salesPrice, iDisc1, iDisc2, iDisc3, bConfirm, bTransfer);

                if(!insertOrder){
                    Toast.makeText(this, "Please Check Your Field and Save again", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(OrderProductActivity.this, OrderActivity.class));
                    finish();
                }

            } else {
                Toast.makeText(this, "Please Fill Quanty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void set0() {
        if(TextUtils.isEmpty(sBig)){
            sBig = "0";
            iBig = Integer.parseInt(sBig);

        } else {
            iBig = Integer.parseInt(sBig);
        }

        if(TextUtils.isEmpty(sSmall)){
            sSmall = "0";
            iSmall = Integer.parseInt(sSmall);
        } else {
            iSmall = Integer.parseInt(sSmall);
        }

        if(TextUtils.isEmpty(sDisc1)){
            sDisc1 = "0";
            iDisc1 = Double.parseDouble(sDisc1);
        } else {
            iDisc1 = Double.parseDouble(sDisc1);
        }

        if(TextUtils.isEmpty(sDisc2)){
            sDisc2 = "0";
            iDisc2 = Double.parseDouble(sDisc2);
        } else {
            iDisc2 = Double.parseDouble(sDisc2);
        }

        if(TextUtils.isEmpty(sDisc3)){
            sDisc3 = "0";
            iDisc3 = Double.parseDouble(sDisc3);
        } else {
            iDisc3 = Double.parseDouble(sDisc3);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
