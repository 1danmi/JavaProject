package com.example.daniel.java_project.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.daniel.java_project.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getActionBar().hide();
        setTitle("Login");
        //initEmailValidation();

    }

    private void initEmailValidation() {
        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        emailEditText.addValidator(new RegexpValidator("Wrong Email Address", "^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$"));
        final Button signIn = (Button) findViewById(R.id.pwdEditText);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate
                emailEditText.validate();
            }
        });
    }
}
