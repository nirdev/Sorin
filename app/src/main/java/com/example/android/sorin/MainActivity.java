package com.example.android.sorin;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.sorin.Signin.SigninActivity;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACCOUNT_PICKER = 2;
    private SharedPreferences settings;
    private String accountName;
    public static GoogleAccountCredential credential;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(intent);

        //Account stuff
        settings = getSharedPreferences("FamousQuotesAndroid", 0);
        credential = GoogleAccountCredential
                .usingAudience(MainActivity.this, "audiences:server:client_id:385934309476-u3kl3v918fgdad1o0mu0hloap1gi4abg.apps.googleusercontent.com");
        setAccountName(settings.getString("ACCOUNT_NAME", null));
        if (credential.getSelectedAccountName() != null) {
            // Already signed in, begin app!
            Toast.makeText(getBaseContext(), "Logged in with : " + credential.getSelectedAccountName(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext()),Toast.LENGTH_SHORT).show();
        } else {
            // Not signed in, show login window or request an account.
            chooseAccount();
        }

    }

    // setAccountName definition
    private void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ACCOUNT_NAME", accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
        this.accountName = accountName;
    }

    void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName =
                            data.getExtras().getString(
                                    AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        setAccountName(accountName);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("ACCOUNT_NAME", accountName);
                        editor.commit();
                        // User is authorized.
                        Toast.makeText(getBaseContext(), "Logged in with onActivityResult: " + credential.getSelectedAccountName(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
