package com.project.powerone.powerone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.powerone.powerone.pojo.Salesman;
import com.project.powerone.powerone.pojo.Status;
import com.project.powerone.powerone.sql.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private EditText userID, newPassword, retypePassword;
    private Button submitButton, takePhoto;

    private Salesman[] salesmen;
    private Status[] statuses;
    private Gson gson = new Gson();

    private String mCurrentPhotoPath, dateNow, timeNow;

    private DatabaseHelper databaseHelper;
    private Calendar calendar = Calendar.getInstance();

    private File photoFile = null;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String DATABASE_NAME = "dbo.db";

    private static final int POSITION = 0;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        /*this.deleteDatabase(DATABASE_NAME);*/
        if(checkDatabase(this, DATABASE_NAME)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            declarate();
        }

    }

    private static boolean checkDatabase(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();

    }

    private void declarate() {

        imageView = (ImageView) findViewById(R.id.imageView);
        takePhoto = (Button) findViewById(R.id.takePhoto);

        userID = (EditText) findViewById(R.id.userID);
        newPassword = (EditText) findViewById(R.id.newPassword);
        retypePassword = (EditText) findViewById(R.id.retypePassword);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(this);
        takePhoto.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(takePhoto)){
            dispatchTakePictureIntent();

        } else if (view.equals(submitButton)){
            checkEditText();

        }
    }

    private void checkEditText() {
        final String defaultID = userID.getText().toString().trim();
        final String defaultnewPassword = newPassword.getText().toString().trim();
        final String defaultretypePassword = retypePassword.getText().toString().trim();


        if(photoFile != null){
            if(!TextUtils.isEmpty(defaultID) && !TextUtils.isEmpty(defaultnewPassword) && !TextUtils.isEmpty(defaultretypePassword)){

                if(defaultnewPassword.equals(defaultretypePassword)){

                    progressDialog.setMessage("Wait . . .");
                    progressDialog.show();

                    String URL = "http://202.43.162.180:8082/poweronemobilewebservice/ws_retrieve_salesman?arg_salesman="+defaultID+"&arg_password=default";

                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("response", response);

                                    try{
                                        salesmen = gson.fromJson(response, Salesman[].class);

                                        final String nameIDServer = salesmen[POSITION].getSalesmanName();
                                        final String siteIDServer = salesmen[POSITION].getSiteID();

                                        getDate();

                                        String URLlogin = "http://202.43.162.180:8082/poweronemobilewebservice/ws_update_salesman?arg_salesman="+defaultID+"&arg_password="+defaultnewPassword+"&arg_lastlogin="+dateNow+"%"+timeNow;

                                        RequestQueue sQueueLogin = Volley.newRequestQueue(RegisterActivity.this);

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

                                                            if(xStatus == 1){
                                                                databaseHelper = new DatabaseHelper(RegisterActivity.this);

                                                                boolean isInserted = databaseHelper.insertSalesman(defaultID, nameIDServer, siteIDServer, defaultnewPassword, dateNow+" "+timeNow);

                                                                if(isInserted){
                                                                    progressDialog.dismiss();

                                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                                    finish();

                                                                } else {
                                                                    progressDialog.dismiss();

                                                                    Toast.makeText(RegisterActivity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
                                                                }

                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(RegisterActivity.this, "Request xStatus Login Error, Please Call Admin", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Exception e) {
                                                            progressDialog.dismiss();
                                                            e.printStackTrace();

                                                            Toast.makeText(RegisterActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();
                                                error.printStackTrace();

                                                Toast.makeText(RegisterActivity.this, "Please Try Again and Check Your Connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        );

                                        sQueueLogin.add(sRequestLogin);

                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }
                    );

                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(RegisterActivity.this, "New Password doesn't match with Retype Password", Toast.LENGTH_SHORT).show();

                }


            } else {
                Toast.makeText(RegisterActivity.this, "Please Fill All Field", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(RegisterActivity.this, "Please Take A Photo", Toast.LENGTH_SHORT).show();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent();
        takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.project.powerone.powerone.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.v("currentPhoto", mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            setReducedImageSize();
        }
    }

    private void setReducedImageSize() {
        int targetImageViewWidth = imageView.getWidth();
        int targetImageViewHeight = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        imageView.setImageBitmap(photoReducedSizeBitmp);
    }

    private void getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy:HH:mm");

        dateNow = dateFormat.format(calendar.getTime());
        timeNow = timeFormat.format(calendar.getTime());
    }
}

