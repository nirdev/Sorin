package com.example.android.sorin.Signin;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.sorin.Constants;
import com.example.nir.myapplication.backend.models.userBeanApi.UserBeanApi;
import com.example.nir.myapplication.backend.models.userBeanApi.model.UserBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by Nir on 3/12/2016.
 */
public class RegisterUserOnBackendAsync extends AsyncTask<UserBean,Void,UserBean>{

    private UserBean mUserBean = new UserBean();



    @Override
    protected UserBean doInBackground(UserBean... params) {

        mUserBean = params[0];
        Log.e("muserbean",mUserBean.getUserEmail());
        UserBeanApi.Builder builder = new UserBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setApplicationName(Constants.GOOGLE_APP_ID)
                .setRootUrl("https://soringcloudapp.appspot.com/_ah/api/");

       UserBeanApi userBeanApi = builder.build();

        try {

            return userBeanApi.insert(params[0]).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RegisterUseron","wrong" + e);
        }

        Log.e("RegisterUseron","wrong again");
        return null;
    }

    @Override
    protected void onPostExecute(UserBean userBean) {
        super.onPostExecute(userBean);
        Log.e("User"," Signed In on server,auto-genrated");
    }
}
