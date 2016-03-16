package com.example.Nir.myapplication.backend;

import java.util.LinkedHashMap;

/**
 * Created by Nir on 3/16/2016.
 */
public class LinkedHashMapWarrper {

    LinkedHashMap<String,String> linkedHashMap;

    public LinkedHashMapWarrper() {
        linkedHashMap  = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, String> getLinkedHashMap() {
        return linkedHashMap;
    }

    public void setLinkedHashMap(LinkedHashMap<String, String> linkedHashMap) {
        this.linkedHashMap = linkedHashMap;
    }

    public void put(String key,String value){
        this.linkedHashMap.put(key,value);
    }

    public void remove(String key){
        this.linkedHashMap.remove(key);
    }

    public int size(){
        return this.linkedHashMap.size();
    }

    public boolean isEmpty(){
        return this.linkedHashMap.isEmpty();
    }
}
