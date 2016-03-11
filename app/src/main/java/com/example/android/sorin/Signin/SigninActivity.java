package com.example.android.sorin.Signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.sorin.R;
import com.example.android.sorin.Util.Util;
import com.example.android.sorin.model.Country;

public class SigninActivity extends AppCompatActivity implements AsyncResponseIPLocation {


    private EditText userPhone;
    private Button nextB;
    private Button choose_country_btn;
    private TextView country_prefix_textview;

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

        }
    }


    //User finish insertphone and clicked next
    public void onClickSigninBtn(View view) {

        userPhone = (EditText) findViewById(R.id.user_phone_signup_XMLID);
        Log.e("userphoneis", userPhone.getText() + "");
    }


    @Override
    public void processFinish(Country output) {

        choose_country_btn = (Button) findViewById(R.id.choose_country_button_ID);
        country_prefix_textview = (TextView) findViewById(R.id.country_code_signup_XMLID);
        choose_country_btn.setText(output.getName());
        country_prefix_textview.setText(output.getPrefix());

    }
}
