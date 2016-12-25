package com.foodie.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.foodie.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import static com.foodie.app.backend.AppContract.User.USER_ID;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String email = "EMAIL_KEY";
    public static final String password = "PASSWORD_KEY";
    public static final String mypreference = "mypref";
    private ConstraintLayout constraintLayout;
    private boolean firebaseMode = false;
    private MaterialEditText emailEditText;
    private MaterialEditText pwdEditText;
    private LoadingButton signInBtn;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private TextView signUpTextView;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        pwdEditText = (MaterialEditText) findViewById(R.id.pwdEditText);
        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_login);
        sharedPref();


        if(firebaseMode) {
            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Intent intent = new Intent(LoginActivity.this, BusinessActivity.class);
                        intent.putExtra(USER_ID,user.getUid());
                        startActivity(intent);
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };
        }

        signInBtn = (LoadingButton) findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement transition animation to register activity.
                signUpTextView.setTextColor(Color.parseColor("#BDBDBD"));

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                signUpTextView.setTextColor(Color.parseColor("#FAFAFA"));


            }
        });

    }

    private void sharedPref() {
        setTitle(getString(R.string.title_activity_login));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mEmail = sharedPreferences.getString(email, "");
        if (mEmail.length() > 0) {
            emailEditText.setText(mEmail);
        }
        String mPassword = sharedPreferences.getString(password, "");
        if (mPassword.length() > 0) {
            pwdEditText.setText(mPassword);
        }
    }

    private void checkLogin() {

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

                    if(firebaseMode){
                        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), pwdEditText.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                        signIn.loadingSuccessful();
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Log.w(TAG, "signInWithEmail", task.getException());
                                            Snackbar.make(constraintLayout, "Failed", Snackbar.LENGTH_LONG).show();
                                            signInBtn.loadingFailed();
                                        }

                                        // ...
                                    }
                                });
                    }



                } else {
                    //emailEditText.validate();
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);

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

