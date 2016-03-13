package com.example.android.sorin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sorin.Signin.SigninActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize SharedPreferences on Sorin internal preferences file
        settings = getSharedPreferences(Constants.SHAREDPREF_FILE_NAME,Constants.SHAREDPREF_MODE);

        //check if user already signed in - if not go to sign-in activity
        if (settings.getLong(Constants.SHAREDPREF_ACCOUNT_ID , 0 ) == 0) {
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(intent);
        }


    }





}
