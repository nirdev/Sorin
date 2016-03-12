package com.example.android.sorin.Signin;

import android.os.AsyncTask;

import com.example.Nir.myapplication.backend.Models.UserBean;
import com.example.Nir.myapplication.backend.userBeanApi.UserBeanApi;

/**
 * Created by Nir on 3/12/2016.
 */
public class RegisterUserOnBackendAsync extends AsyncTask<UserBean,Void,UserBean> {


    UserBeanApi;
    UserBeanapi;
    @Override
    protected UserBean doInBackground(UserBean... params) {

        return null;
            }


    @Override
    protected void onPostExecute(UserBean userBean) {
        super.onPostExecute(userBean);
    }
}
