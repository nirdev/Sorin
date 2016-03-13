package com.example.android.sorin.Util;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nir on 3/11/2016.
 */
public class Util {

    public static  String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setupUI(View view, final Activity activity) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, activity);
            }
        }

    }
    public static boolean isValidPhone(String phone,String prefix){
        Boolean mIsvalidPhone;

        mIsvalidPhone = phone.length() < 11 && phone.length() > 7;

        Log.e("mIsvalidPhone",mIsvalidPhone.toString());
        return mIsvalidPhone;
    }
}
