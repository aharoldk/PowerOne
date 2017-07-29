package com.project.powerone.powerone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.powerone.powerone.view.CanvasView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class SignatureActivity extends AppCompatActivity {

    private CanvasView canvasView;

    private Button scrshootCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        canvasView = (CanvasView) findViewById(R.id.canvasCanvas);
        scrshootCanvas = (Button) findViewById(R.id.scrshootCanvas);

        scrshootCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
                Toast.makeText(SignatureActivity.this, "Harold", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
            Toast.makeText(SignatureActivity.this, mPath, Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    public void clearCanvas(View view){
        canvasView.clearCanvas();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(SignatureActivity.this, VisitActivity.class));
        finish();
    }
}
