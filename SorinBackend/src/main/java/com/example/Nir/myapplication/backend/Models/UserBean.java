package com.example.Nir.myapplication.backend.Models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by Nir on 3/12/2016.
 */

@Entity
public class UserBean {

    @Id
    String userEmail;

    @Index
    String userPhone;

    public UserBean(String userEmail, String userPhone) {
        this.userEmail = userEmail;
        this.userPhone = userPhone;
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
}
