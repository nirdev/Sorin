package com.example.android.sorin.Signin;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.sorin.Constants;
import com.example.android.sorin.R;
import com.example.android.sorin.Util.Util;
import com.example.android.sorin.model.Country;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class SigninActivity extends AppCompatActivity implements AsyncResponseIPLocation {

    //Result code for activityforreslt google's sign-in API activity
    private static final int REQUEST_ACCOUNT_PICKER = 2;

    //Account name from google sign-in
    private String accountName;

    //Phone from EditText
    private EditText userPhone;

    //SharedPreferences initializer
    private SharedPreferences settings;

    //GoogleAccountCredential initializer
    public static GoogleAccountCredential credential;

    private Button choose_country_btn;
    private TextView country_prefix_textview;
    private Button nextB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //keyboard UI closing class
        Util.setupUI(findViewById(android.R.id.content), SigninActivity.this);


        //Start Async task to check location of the user by ip and set user prefferd country
        GetIPLocationFromURL task = new GetIPLocationFromURL(this);
        task.countryNameINTERFACE = this;
        task.execute();

        //Declaring view variables
        userPhone = (EditText) findViewById(R.id.user_phone_signup_XMLID);
        nextB = (Button) findViewById(R.id.next_button1);
        choose_country_btn = (Button) findViewById(R.id.choose_country_button_ID);
        country_prefix_textview = (TextView) findViewById(R.id.country_code_signup_XMLID);


        //Call internal storage preferences file / auto build the file if not exist
        settings = getSharedPreferences(Constants.SHAREDPREF_FILE_NAME, 0);

        //Call Google credentials to get currently signed-in user via google api
        credential = GoogleAccountCredential
                .usingAudience(SigninActivity.this, "audiences:server:client_id:385934309476-u3kl3v918fgdad1o0mu0hloap1gi4abg.apps.googleusercontent.com");

        //Set the account name from google sign-in API in Sharedpreferences and on local variable accountname
        setAccountName(settings.getString(Constants.SHAREDPREF_ACCOUNT_NAME, null));
        if (credential.getSelectedAccountName() != null) {
            // Already signed in, begin app!
            //Toast.makeText(getBaseContext(), "Logged in with : " + credential.getSelectedAccountName(), Toast.LENGTH_SHORT).show();

        } else {
            // Not signed in, show login window or request an account.
            chooseAccount();
        }

    }

    // set account name in shared prefrences and in local variable accountname
    private void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.SHAREDPREF_ACCOUNT_NAME, accountName);
        //apply - Async
        editor.apply();
        credential.setSelectedAccountName(accountName);
        this.accountName = accountName;
    }

    //Calls google sign-in activity - open fragment for user to choose his google account
    void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    //user clicked on choose different country fragment - open fragment
    public void chooseCountryFragment(View view) {
        //Declare Country Chooser fragment
        Intent intent = new Intent(this, CountrycodeListActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if = 1 - result for country code
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String countryCode = data.getStringExtra(CountrycodeListActivity.RESULT_CONTRYCODE);
            String countryName = data.getStringExtra(CountrycodeListActivity.RESULT_CONTRYNAME);
            choose_country_btn = (Button) findViewById(R.id.choose_country_button_ID);
            choose_country_btn.setText(countryName);
            country_prefix_textview.setText(countryCode);

            //result from GoogleAccountCredential.newChooseAccountIntent()
        }else if (requestCode == REQUEST_ACCOUNT_PICKER){

            //Check if google account values came back
            if (data != null && data.getExtras() != null) {
                String accountName =
                        data.getExtras().getString(
                                AccountManager.KEY_ACCOUNT_NAME);
               //Set new account name on local variable and on SharedPreferences
                if (accountName != null) {
                    setAccountName(accountName);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.SHAREDPREF_ACCOUNT_NAME, accountName);
                    editor.apply();

                    // User is authorized.
                    //Toast.makeText(getBaseContext(), "Logged in with onActivityResult: " + credential.getSelectedAccountName(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    //User finish insertphone and clicked next
    public void onClickSigninBtn(View view) {

        userPhone = (EditText) findViewById(R.id.user_phone_signup_XMLID);
        Log.e("userphoneis", userPhone.getText() + "");
    }


    //Called by GetIPLocation using AsyncResponseIP Interface - Auto Set the country and pre-fix for the user
    @Override
    public void processFinish(Country output) {

        choose_country_btn = (Button) findViewById(R.id.choose_country_button_ID);
        country_prefix_textview = (TextView) findViewById(R.id.country_code_signup_XMLID);
        choose_country_btn.setText(output.getName());
        country_prefix_textview.setText(output.getPrefix());

    }
}
