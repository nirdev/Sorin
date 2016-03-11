package com.example.android.sorin.Signin;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.android.sorin.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountrycodeListActivity extends ListActivity {


    public static String RESULT_CONTRYCODE = "countrycode";
    public static String RESULT_CONTRYNAME = "countryname";
    private List<Country> countryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        populateCountryList();
        ArrayAdapter<Country> adapter = new CountryListArrayAdapter(this, countryList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country c = countryList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CONTRYCODE, c.getCode());
                returnIntent.putExtra(RESULT_CONTRYNAME, c.getName());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    //make the json file readable (parse the json file)
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private void populateCountryList() {
        countryList = new ArrayList<Country>();

        //read file from json buffer
        try {
            //read file from json buffer
            JSONObject jsonObjectMain = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArrayMain = jsonObjectMain.getJSONArray("countries");

            for(int i = 0; i < jsonArrayMain.length(); i++) {

                JSONObject objcountry = jsonArrayMain.getJSONObject(i);
                countryList.add(new Country(Util.getString("name", objcountry), Util.getString("code", objcountry)));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


//        countrynames = getResources().getStringArray(R.array.country_names);
//        countrycodes = getResources().getStringArray(R.array.country_codes);
//
//        for(int i = 0; i < countrycodes.length; i++){
//            countryList.add(new Country(countrynames[i], countrycodes[i]));
//        }
    }

    public class Country {
        private String name;
        private String code;

        public Country(String name, String code){
            this.name = name;
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public String getCode() {
            return code;
        }
    }

}

