package com.example.android.sorin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.sorin.Signin.SigninActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize SharedPreferences on Sorin internal preferences file
        settings = getSharedPreferences(Constants.SHAREDPREF_FILE_NAME,0);

        //check if user already signed in - if not go to sign-in activity //TODO: delete true when finish working on Sign-in Activity
        if (settings.getString(Constants.SHAREDPREF_ACCOUNT_NAME , null) == null || true) {
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(intent);
        }
    }


    public void CheckBackend(View view) {

        new RegisterGAEAsync().execute(this);
    }

    public void GoToSignin(View view) {
        Intent intent = new Intent(MainActivity.this, SignInGoogle.class);
        startActivity(intent);
    }




}
