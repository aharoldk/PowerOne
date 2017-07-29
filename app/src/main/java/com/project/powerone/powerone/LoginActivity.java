package com.project.powerone.powerone;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.powerone.powerone.sql.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView loginImage;
    private EditText loginPassword;
    private DatabaseHelper databaseHelper;

    private TextView loginLogin;

    private static final int PASSWORD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginImage = (ImageView) findViewById(R.id.loginImage);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginLogin = (TextView) findViewById(R.id.loginLogin);

        loginLogin.setOnClickListener(this);

        loginImage.setImageResource(R.drawable.angelos);
    }

    @Override
    public void onClick(View view) {
        if(view == loginLogin){
            databaseHelper = new DatabaseHelper(LoginActivity.this);

            Cursor cursor = databaseHelper.loginSalesman();

            if(cursor.getCount() == 0){
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();

            } else {
                String lPassword = loginPassword.getText().toString();
                if(!TextUtils.isEmpty(lPassword)){

                    cursor.moveToFirst();
                    String dbPassword = cursor.getString(PASSWORD);
                    if(lPassword.equals(dbPassword)){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Your Password Wrong", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please Input Your Password", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }


}

