package com.foodie.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.foodie.app.R;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.CPUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.rengwuxian.materialedittext.MaterialEditText;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private final String USER_ID = "userID";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ConstraintLayout constraintLayout;
    Snackbar snackbar;
    LoadingButton signUpBtn = null;
    private boolean firebaseMode = false;
    private MaterialEditText userNameEditText;
    private MaterialEditText emailEditText;
    private MaterialEditText pwdEditText;

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


        if(firebaseMode) {
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Intent intent = new Intent(RegisterActivity.this, BusinessActivity.class);
                        intent.putExtra(USER_ID,user.getUid());
                        startActivity(intent);
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };
        }
        this.signUpBtn = (LoadingButton) findViewById(R.id.sign_up_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                signUpBtn.startLoading();
                userNameEditText = (MaterialEditText) findViewById(R.id.userNameEditText);
                emailEditText = (MaterialEditText) findViewById(R.id.pwdEditText);
                pwdEditText = (MaterialEditText) findViewById(R.id.confPwdNameEditText);

                if (!emailEditText.getText().toString().equals(pwdEditText.getText().toString()))
                    try {
                        throw new Exception("The fields password and confirm password doesn't match");
                    } catch (Exception e) {
                        Snackbar.make(constraintLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        return;
                    }

                if(firebaseMode){
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), pwdEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userNameEditText.getText().toString())
                                            .build();
                                    user.updateProfile(profileUpdates);
                                   //user.updateProfile(new UserProfileChangeRequest())
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Snackbar.make(constraintLayout, "Failed", Snackbar.LENGTH_LONG).show();
                                        signUpBtn.loadingFailed();
                                    }
                                }
                            });


                }else {

                    CPUser user = new CPUser();
                    try {



                        user.setUserFullName(userNameEditText.getText().toString());
                        user.setUserEmail(emailEditText.getText().toString());
                        user.setUserPwd(pwdEditText.getText().toString());


                    } catch (Exception e) {

                        snackbar = Snackbar.make(constraintLayout, e.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }

                    //ToDO Insert Data.
                    //Create an AsyncData object and set the constructor
                    AsyncData<Void> data = new AsyncData<>(getApplicationContext(), CPUser.getCPUser_URI());
                    // Set the task to insert
                    data.setDatamanagerType(DataManagerType.Insert);
                    // Set the function to get status
                    data.setCallBack(new CallBack<Void>() {
                        @Override
                        public void DBstatus(DataStatus status) {
                            switch (status) {
                                case Success:
                                    signUpBtn.loadingSuccessful();
                                    snackbar = Snackbar.make(constraintLayout, "Success", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    break;
                                case Failed:
                                    snackbar = Snackbar.make(constraintLayout, "Failed", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    signUpBtn.loadingFailed();

                            }
                        }
                    });
                    // Execute the AsyncTask
                    data.execute();
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseMode) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseMode) {
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
    }
}

/*
*
*
* */




