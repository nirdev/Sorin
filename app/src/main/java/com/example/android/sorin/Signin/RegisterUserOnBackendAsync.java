package com.example.android.sorin.Signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.android.sorin.Constants;
import com.example.android.sorin.MainActivity;
import com.example.nir.myapplication.backend.userBeanApi.UserBeanApi;
import com.example.nir.myapplication.backend.userBeanApi.model.UserBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by Nir on 3/12/2016.
 */
public class RegisterUserOnBackendAsync extends AsyncTask<UserBean,Void,UserBean>{

    private UserBean mUserBean = new UserBean();
    private Context mContext;

    public RegisterUserOnBackendAsync(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected UserBean doInBackground(UserBean... params) {

        mUserBean = params[0];

        UserBeanApi.Builder builder = new UserBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setApplicationName(Constants.GOOGLE_APP_ID)
                .setRootUrl("https://soringcloudapp.appspot.com/_ah/api/");

       UserBeanApi userBeanApi = builder.build();

        try {

            return userBeanApi.insert(params[0]).execute();
        } catch (IOException e) {
            e.printStackTrace();
            //Log.e("RegisterUseron","wrong" + e);

        }

        //Log.e("RegisterUseron","wrong again");
        return null;
    }

    @Override
    protected void onPostExecute(UserBean userBean) {
        super.onPostExecute(userBean);


        // if null register on server went wrong
        if (userBean != null)
        {
            //SharedPreferences initializer - save user Id, name and phone on shared preferences
            SharedPreferences settings = mContext.getSharedPreferences(Constants.SHAREDPREF_FILE_NAME, Constants.SHAREDPREF_MODE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(Constants.SHAREDPREF_ACCOUNT_ID, userBean.getId());
            editor.putString(Constants.SHAREDPREF_ACCOUNT_NAME, userBean.getUserEmail());
            editor.putString(Constants.SHAREDPREF_ACCOUNT_PHONE, userBean.getUserPhone());
            editor.commit();

            //Let user know he is registered
            Toast.makeText(mContext,"Registered",Toast.LENGTH_SHORT).show();

            //go to next activity
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);

       //     Log.e("User", " Signed In on server auto-generated Id " + userBean.getId());
        }else {
            Toast.makeText(mContext,"Unable to register user",Toast.LENGTH_LONG).show();
        }

    }
}
