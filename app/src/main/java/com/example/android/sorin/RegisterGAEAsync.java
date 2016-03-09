package com.example.android.sorin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.nir.myapplication.backend.myApi.MyApi;
import com.example.nir.myapplication.backend.myApi.model.MyBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;


/**
 * Created by Nir on 3/9/2016.
 */
public class RegisterGAEAsync extends AsyncTask<Context,Void,String> {

    Context context;
    MyBean myBean = null;
    String temp = null;


    @Override
    protected String doInBackground(Context... params) {

        context =params[0];

        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl("https://soringcloudapp.appspot.com/_ah/api/");

        MyApi myApi = builder.build();
        try {
            Log.e("pretty","good");

           temp =  myApi.sayHi("hey").execute().getData();

            Log.e("temp",temp);
            return temp;
        } catch (IOException e) {

            Log.e("e","---------"+e.getMessage());
            e.printStackTrace();
        }

        return "wrong";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Toast.makeText(context,s,Toast.LENGTH_LONG).show();

    }
}
