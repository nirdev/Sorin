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
    String name  = null;




    //TODO: Delete after building User Framework
    @Override
    protected String doInBackground(Context... params) {

        context =params[0];

        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setApplicationName("soringcloudapp")
                .setRootUrl("https://soringcloudapp.appspot.com/_ah/api/");

        MyApi myApi = builder.build();

        try {



            return  myApi.sayHi("ahaln").execute().getData();
        } catch (IOException e) {

            Log.e("e","---------"+ e.getMessage());
            e.printStackTrace();
        }

        return "wrong";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("onpost",s);
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();

    }
}
