package com.example.Nir.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Nir on 3/12/2016.
 */

@Entity
public class UserBean {

    @Id
    private Long id;

    @Index
    private String userEmail;

    @Index
    private String userPhone;


    /**
     * @params
     * LinkedHashMap <String,Long> LinkedHashMap of user's friends,
     * String for friend name as it save in user phone
     * Long for friend Userbean Entity unique id in datastore
     */
    @Index
    private LinkedHashMap<String,Long> userFriends = new LinkedHashMap<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public HashMap<String, Long> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(LinkedHashMap<String, Long> userFriends) {
        this.userFriends = userFriends;
    }
}
