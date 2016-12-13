package com.example.daniel.java_project.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.example.daniel.java_project.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

public class LoginActivity extends AppCompatActivity {

    public static final String email = "EMAIL_KEY";
    public static final String password = "PASSWORD_KEY";
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        final MaterialEditText pwdEditText = (MaterialEditText) findViewById(R.id.pwdEditText);

        setTitle(getString(R.string.title_activity_login));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mEmail = sharedPreferences.getString(email,"");
        if(mEmail.length()>0){
            emailEditText.setText(mEmail);
        }
        String mPassword = sharedPreferences.getString(password,"");
        if(mPassword.length()>0){
            pwdEditText.setText(mPassword);
        }

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
                Toast.makeText(getApplicationContext(), getString(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkLogin() {
        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        final MaterialEditText pwdEditText = (MaterialEditText) findViewById(R.id.pwdEditText);
        emailEditText.addValidator(new RegexpValidator(getString(R.string.error_invalid_email), "^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$"));
        final LoadingButton signIn = (LoadingButton) findViewById(R.id.sign_in_btn);
        emailEditText.setError(null);
        signIn.startLoading();

        signIn.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String emailAddress = emailEditText.getText().toString();
                if(emailAddress.length()==0) {
                    emailEditText.setError(getString(R.string.error_empty_email));
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(),getString(R.string.error_login),Toast.LENGTH_SHORT).show();
                }
                else if(emailEditText.validate()){  //TODO: Implement signing in here
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putString(email,emailAddress).apply();

                    Toast.makeText(getApplicationContext(), getString(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show();
                    signIn.loadingSuccessful();

                }
                else
                {
                    //emailEditText.validate();
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(),getString(R.string.error_login),Toast.LENGTH_SHORT).show();
                }
            }
        },3000);

    }
}
