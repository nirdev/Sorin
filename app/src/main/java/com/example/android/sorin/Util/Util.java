package com.example.android.sorin.Util;

import android.app.Activity;
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
    //Clean the phone number and add prefix
    public static String getCleanNumber(String number,String prefix){

        String mCleannumber = null;
        String mNumber = number;
        String mPrefix = prefix;
        String firstChar = "";

        if (mNumber.startsWith("+")) {
            firstChar = "+";
        }

        mNumber = mNumber.replaceAll("[^\\d]", ""); //Delete all non-numeric charts
        mNumber = firstChar + mNumber; // Append Plus only if was one



        if (mNumber.startsWith("+") && mNumber.length() < 16 ){

            mCleannumber = mNumber;

        }
        else if (mNumber.length() < 16 && mNumber.length() > 7) {

            mNumber = mNumber.replaceFirst("^0*", ""); //Delete all first Zeros
            mCleannumber = mPrefix + mNumber; //Add prefix to the clean number
        }

        return mCleannumber;
    }

    public static boolean isValidPhone(String phone,String prefix){
        Boolean mIsvalidPhone;

        mIsvalidPhone = phone.length() < 11 && phone.length() > 7;
        return mIsvalidPhone;
    }
}
