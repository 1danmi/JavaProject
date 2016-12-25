package com.foodie.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.foodie.app.DebugHelper.DebugHelper;
import com.foodie.app.R;

import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DBquery;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

public class LoginActivity extends AppCompatActivity {

    public static final String email = "EMAIL_KEY";
    public static final String password = "PASSWORD_KEY";
    public static final String mypreference = "mypref";

    ConstraintLayout constraintLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_login); // By David
        test(); //By David

        final TextView signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Implement transition animation to register activity.
                signUpTextView.setTextColor(Color.parseColor("#BDBDBD"));

                try {

                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }

                signUpTextView.setTextColor(Color.parseColor("#FAFAFA"));


            }
        });




        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        final MaterialEditText pwdEditText = (MaterialEditText) findViewById(R.id.pwdEditText);

        setTitle(getString(R.string.title_activity_login));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mEmail = sharedPreferences.getString(email, "");
        if (mEmail.length() > 0) {
            emailEditText.setText(mEmail);
        }
        String mPassword = sharedPreferences.getString(password, "");
        if (mPassword.length() > 0) {
            pwdEditText.setText(mPassword);
        }

        final LoadingButton signInBtn = (LoadingButton) findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
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
                if (emailAddress.length() == 0) {
                    emailEditText.setError(getString(R.string.error_empty_email));
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                } else if (emailEditText.validate()) {

                    //TODO: Implement signing in here
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putString(email, emailAddress).apply();

                    Toast.makeText(getApplicationContext(), getString(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show();

                    /***************************David*****************************/

                    //Create an AsyncData object and set the constructor
                    AsyncData<CPUser> data = new AsyncData<>(getApplicationContext(), CPUser.getCPUser_URI());
                    // Set the task to insert
                    data.setDatamanagerType(DataManagerType.login);
                    // Set the function to get status
                    data.setCallBack(new CallBack<CPUser>() {
                        @Override
                        public void DBstatus(DataStatus status, CPUser... data) {
                            DebugHelper.Log("CallBack with status: " + status);

                            switch (status) {
                                case Success:
                                    snackbar = Snackbar.make(constraintLayout, "Success", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    Intent intent = new Intent(LoginActivity.this, BusinessActivity.class);
                                    startActivity(intent);
                                    break;
                                case Failed:
                                    snackbar = Snackbar.make(constraintLayout, "Failed", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    signIn.loadingFailed();
                                default:
                                    DebugHelper.Log("Default switch in callBack");
                                    signIn.loadingFailed();
                                    break;
                            }
                        }
                    });
                    // Execute the AsyncTask
                    data.execute(new DBquery(new String[]{emailEditText.getText().toString(),pwdEditText.getText().toString()}));
                    /***************************David*****************************/

                    signIn.loadingSuccessful();

                } else {
                    //emailEditText.validate();
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                }
            }
        }, 1);

    }


    void test()
    {
        CPUser user = new CPUser();
        try {


            user.setUserFullName("aa aa");
            user.setUserEmail("a@a.c");
            user.setUserPwd("12345678");




        } catch (Exception e) {

            snackbar =  Snackbar.make(constraintLayout,e.getMessage(),Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }



        //Create an AsyncData object and set the constructor
        AsyncData<CPUser> data = new AsyncData<>(getApplicationContext(), CPUser.getCPUser_URI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Insert);
        // Set the function to get status
        data.setCallBack(new CallBack<CPUser>() {
            @Override
            public void DBstatus(DataStatus status, CPUser... data) {
                DebugHelper.Log("Insert callBack finish with status: " + status);
            }
        });
        // Execute the AsyncTask
        data.execute(user.toContentValues());
    }

}

