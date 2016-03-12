package com.example.Nir.myapplication.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by Nir on 3/9/2016.
 */
public class OfyService {
    static {
        factory().register(MyBean.class);
        factory().register(UserBean.class);

    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }
}