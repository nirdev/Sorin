package com.example.android.sorin.friendListBuilder;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.android.sorin.Constants;
import com.example.android.sorin.Util.Util;
import com.example.nir.myapplication.backend.userBeanApi.UserBeanApi;
import com.example.nir.myapplication.backend.userBeanApi.model.LinkedHashMapWarrper;
import com.example.nir.myapplication.backend.userBeanApi.model.UserBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
    @friendListBuilderService IntentService upload all your contacts to the cloud and receive your new
    Sorin friend list -that way the user friend's list is always up to date
 **/
public class friendListBuilderService extends IntentService {

    String mUserDefaultPrefix = "+972";
    Long mAccountId = 5076495651307520l;
    String mContactPhoneNumber = null;
    String mContactName = null;
    LinkedHashMapWarrper mContactListHM = new LinkedHashMapWarrper();
    UserBean newContactListUserBean = new UserBean();


    public friendListBuilderService() {
        super("worker");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("class: ", "onHandleIntent    started --------- ");
        //Retrieve All Contacts from content provider and add only valid numbers to the array lists
        retrieveAllContacts();
        Log.wtf("LinkedHashMap Size:", " " + mContactListHM.size());


        UserBeanApi.Builder builder = new UserBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setApplicationName(Constants.GOOGLE_APP_ID)
                .setRootUrl("https://soringcloudapp.appspot.com/_ah/api/");

        UserBeanApi userBeanApi = builder.build();


        try {
            Log.wtf("here", "--------------------------------------------");
            newContactListUserBean = userBeanApi.buildFriendsList(mAccountId,mContactListHM).execute();
            Log.e("class: ", " userfriends:" + newContactListUserBean.getUserFriends());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RegisterUseron","wrong" + e);

        }





    }

    /**
     *Retrieve all contacts - http://stackoverflow.com/questions/12562151/android-get-all-contacts
     */
    private void retrieveAllContacts(){


        //initiate cursor in-order to work with content provider
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(
                ContactsContract.Contacts.CONTENT_URI, //URI of contacts as content provider
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"); //get contacts by order by name (ASC - Sqlite command)

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                mContactName = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (pCur.moveToNext()) {
                        mContactPhoneNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }

                    pCur.close();

                    //Check thah phone number or name from Contacts Contact Provider is not empty
                    if (mContactPhoneNumber != null && mContactName != null && !mContactPhoneNumber.isEmpty() && !mContactName.isEmpty()) {

                        //send the contact phone to getCleannumer function with user default prefix from shared preferences
                        mContactPhoneNumber = Util.getCleanNumber(mContactPhoneNumber, mUserDefaultPrefix);

                        //Check that phone did not return empty
                        if (mContactPhoneNumber != null && !mContactPhoneNumber.isEmpty()) {

                            //Add phone and name to arrayList which later wll be send to the server
                            mContactListHM.put(mContactPhoneNumber,mContactName);
                        }
                    }
                }
            }

        }


    }




}
