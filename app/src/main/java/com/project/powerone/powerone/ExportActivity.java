package com.project.powerone.powerone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ExportActivity.this, MainActivity.class));
        finish();
    }
}
