package com.foodie.app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.backend.AppContract;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.burgeranch);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 1000, 800, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo1 = stream.toByteArray();

            String str = Arrays.toString(logo1);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
}
