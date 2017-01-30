package com.foodie.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.entities.CPUser;
import com.foodie.app.listsDB.ContentResolverDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mEmail = sharedPreferences.getString("EMAIL_KEY", "");
        String mPassword = sharedPreferences.getString("PASSWORD_KEY", "");
        ContentResolverDatabase.loadingCounter=0;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(mEmail, mPassword);
        // Prompt the user to re-provide their sign-in credentials
        if (user == null || mEmail.isEmpty() || mPassword.isEmpty()) {
            openLogin();
            return;
        }
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try {

                            if (task.isComplete()) {
                                DebugHelper.Log("logged in: " + user.getEmail());
                                DBManagerFactory.setCurrentUser(new CPUser(user.getUid(), user.getEmail(), user.getDisplayName()));
                                Intent intent = new Intent(MainActivity.this, BusinessActivity.class);
                                startActivity(intent);
                            } else {
                                openLogin();
                            }
                        } catch (Exception ignored) {

                        }

                    }
                });
    }

    void openLogin() {
        DebugHelper.Log("No user logged in");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
