package com.foodie.app.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.foodie.app.R;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       try {
           Intent intent = new Intent(MainActivity.this, BusinessActivity.class);
           startActivity(intent);
       }catch (Exception ex)
       {
           Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();

       }
    }
}
