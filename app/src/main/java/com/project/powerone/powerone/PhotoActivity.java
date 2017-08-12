package com.project.powerone.powerone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.powerone.powerone.service.AngelosService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Button takePhoto;

    private String mCurrentPhotoPath;

    private File photoFile = null;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        declarate();
        navigation();

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

            mCurrentPhotoPath = f.getAbsolutePath();

            Log.i("current", mCurrentPhotoPath);

            Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mCurrentPhotoPath);

            imageView.setImageBitmap(photoReducedSizeBitmp);
        }
    }

    private void declarate() {
        imageView = (ImageView) findViewById(R.id.imageView);
        takePhoto = (Button) findViewById(R.id.takePhoto);

        takePhoto.setOnClickListener(this);

        showImages();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(takePhoto)){
            dispatchTakePictureIntent();
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

        Toast.makeText(this, "Your Photo Update", Toast.LENGTH_SHORT).show();
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.importScreen){
                    startActivity(new Intent(PhotoActivity.this, ImportActivity.class));
                    finish();

                } else if(id == R.id.exportScreen){
                    startActivity(new Intent(PhotoActivity.this, ExportActivity.class));
                    finish();

                } else if(id == R.id.visitScreen){
                    startActivity(new Intent(PhotoActivity.this, SalesActivity.class));
                    finish();

                } else if(id == R.id.produkScreen){
                    startActivity(new Intent(PhotoActivity.this, ProductActivity.class));
                    finish();

                } else if(id == R.id.orderScreen){
                    startActivity(new Intent(PhotoActivity.this, OrderActivity.class));
                    finish();

                } else if(id == R.id.ARScreen){
                    startActivity(new Intent(PhotoActivity.this, ARActivity.class));
                    finish();

                } else if(id == R.id.reportScreen){
                    startActivity(new Intent(PhotoActivity.this, ReportActivity.class));
                    finish();

                } else if (id == R.id.photoScreen){
                    startActivity(new Intent(PhotoActivity.this, PhotoActivity.class));
                    finish();

                } else if(id == R.id.passwordScreen){
                    startActivity(new Intent(PhotoActivity.this, PasswordActivity.class));
                    finish();

                } else if(id == R.id.logout){
                    Intent intent = new Intent(getApplicationContext(), AngelosService.class);
                    stopService(intent);

                    finish();
                    startActivity(new Intent(PhotoActivity.this, LoginActivity.class));

                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            finish();
            startActivity(new Intent(PhotoActivity.this, MainActivity.class));
        }
    }
}
