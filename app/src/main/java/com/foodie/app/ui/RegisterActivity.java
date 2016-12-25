package com.foodie.app.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.foodie.app.DebugHelper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;

import com.rengwuxian.materialedittext.MaterialEditText;


public class RegisterActivity extends AppCompatActivity {


    ConstraintLayout constraintLayout;
    Snackbar snackbar;
    LoadingButton signUpBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_register);

        final TextView signInTextView = (TextView) findViewById(R.id.signInTextView);
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement transition animation to register activity.
                //signInTextView.setTextColor(Color.parseColor("#BDBDBD"));
                finish();
            }
        });

        this.signUpBtn = (LoadingButton) findViewById(R.id.sign_up_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                signUpBtn.startLoading();



                CPUser user = new CPUser();
                try {
                    MaterialEditText p1 = (MaterialEditText)findViewById(R.id.pwdEditText);
                    MaterialEditText p2 = (MaterialEditText)findViewById(R.id.confPwdNameEditText);

                    if(!p1.getText().toString().equals(p2.getText().toString()))
                        throw new Exception("The fields password and confirm password doesn't match");

                    user.setUserFullName(( (MaterialEditText)findViewById(R.id.userNameEditText)).getText().toString());
                    user.setUserEmail(( (MaterialEditText)findViewById(R.id.emailEditText)).getText().toString());
                    user.setUserPwd( ((MaterialEditText)findViewById(R.id.pwdEditText)).getText().toString());


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
                        DebugHelper.Log("CallBack with status: " + status);

                        switch (status) {
                                case Success:
                                    signUpBtn.loadingSuccessful();
                                    snackbar = Snackbar.make(constraintLayout, "Success", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                             //TODO animation when succeed
                                    finish();
                                    break;
                                case Failed:
                                    snackbar = Snackbar.make(constraintLayout, "Failed", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    signUpBtn.loadingFailed();
                                default:
                                    DebugHelper.Log("Default switch in callBack");
                                    break;
                            }
                    }
                });
                // Execute the AsyncTask
                data.execute(user.toContentValues());

            }
        });


    }
}
