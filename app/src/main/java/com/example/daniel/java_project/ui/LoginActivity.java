package com.example.daniel.java_project.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
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
        //checkLogin();
        final LoadingButton signInBtn = (LoadingButton) findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        final TextView signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement transition to register activity.
                Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkLogin() {
        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        emailEditText.addValidator(new RegexpValidator("Wrong email address", "^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$"));
        final LoadingButton signIn = (LoadingButton) findViewById(R.id.sign_in_btn);
        emailEditText.setError(null);
        signIn.startLoading();

        signIn.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String emailAddress = emailEditText.getText().toString();
                if(emailAddress.length()==0) {
                    emailEditText.setError("Please enter your email address!");
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(),"login failed, try again",Toast.LENGTH_SHORT).show();
                }
                else if(emailEditText.validate()){  //TODO: Implement signing in here

                    Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
                    signIn.loadingSuccessful();

                }
                else
                {
                    //emailEditText.validate();
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(),"login failed,try again",Toast.LENGTH_SHORT).show();
                }
            }
        },3000);

    }
}
