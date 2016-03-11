package com.example.android.sorin.model;

/**
 * Created by Nir on 2/7/2016.
 */
public class Country {

    private String Name;
    private String Code;
    private String Prefix;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        this.Prefix = prefix;
    }
}
