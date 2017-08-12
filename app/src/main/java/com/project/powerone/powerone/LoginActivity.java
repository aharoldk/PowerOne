package com.project.powerone.powerone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.powerone.powerone.pojo.Status;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView loginImage;
    private EditText loginPassword;
    private Button loginLogin;

    private String dateNow;
    private String timeNow;
    private String dbUserid;
    private String dbPassword;

    private Calendar calendar = Calendar.getInstance();

    private Status[] statuses;

    private Gson gson = new Gson();

    private static final int USERID = 0;
    private static final int PASSWORD = 3;

    private static final int POSITION = 0;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        loginImage = (ImageView) findViewById(R.id.loginImage);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginLogin = (Button) findViewById(R.id.loginLogin);

        loginLogin.setOnClickListener(this);

        showImages();
    }



    static final String[] EXTENSIONS = new String[]{
            "jpg" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    private void showImages() {
        File dir = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));

        File[] filelist = dir.listFiles(IMAGE_FILTER );
        for (File f : filelist) {

            String mCurrentPhotoPath = f.getAbsolutePath();

            Log.i("current", mCurrentPhotoPath);

            Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mCurrentPhotoPath);

            loginImage.setImageBitmap(photoReducedSizeBitmp);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(loginLogin)){
            DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);

            Cursor cursor = databaseHelper.loginSalesman();

            if(cursor.getCount() == 0){
                finish();

            } else {
                String lPassword = loginPassword.getText().toString();
                if(!TextUtils.isEmpty(lPassword)){

                    cursor.moveToFirst();
                    dbUserid = cursor.getString(USERID);
                    dbPassword = cursor.getString(PASSWORD);

                    if(lPassword.equals(dbPassword)){
                        parseDatabase();

                    } else {
                        Toast.makeText(LoginActivity.this, "Your Password Wrong", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please Input Your Password", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private void parseDatabase() {
        getDate();

        progressDialog.setMessage("Wait . . .");
        progressDialog.show();

        String URLlogin = "http://202.43.162.180:8082/poweronemobilewebservice/ws_update_salesman?arg_salesman="+dbUserid+"&arg_password="+dbPassword+"&arg_lastlogin="+dateNow+"%"+timeNow;

        RequestQueue sQueueLogin = Volley.newRequestQueue(LoginActivity.this);

        StringRequest sRequestLogin = new StringRequest(
                Request.Method.GET,
                URLlogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseLogin) {
                        Log.i("responseLogin", responseLogin);

                        try {
                            statuses = gson.fromJson(responseLogin, Status[].class);

                            int xStatus = statuses[POSITION].getxStatus();

                            if(xStatus == 1) {
                                finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(LoginActivity.this, "Request xStatus Login Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Toast.makeText(LoginActivity.this, "Please Check Your Connection and Relogin", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                }

                error.printStackTrace();
            }
        }
        );

        sQueueLogin.add(sRequestLogin);
    }


    private void getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy:HH:mm");

        dateNow = dateFormat.format(calendar.getTime());
        timeNow = timeFormat.format(calendar.getTime());
    }
}

