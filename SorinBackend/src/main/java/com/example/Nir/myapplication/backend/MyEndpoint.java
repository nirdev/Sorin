/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Nir.myapplication.backend;


import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

//TODO: Delet after building User Framework
/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.myapplication.Nir.example.com",
    ownerName = "backend.myapplication.Nir.example.com",
    packagePath=""
  )
)
public class MyEndpoint {

    private static final int DEFAULT_LIST_LIMIT = 20;

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi"  ,  scopes = {Constants.EMAIL_SCOPE},
            clientIds = {Constants.WEB_CLIENT_ID,
                    Constants.ANDROID_CLIENT_ID,
                    com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
            audiences = {Constants.ANDROID_AUDIENCE})

    public MyBean sayHi(@Named("name") String name, User user)   {

        MyBean response = new MyBean();

        if (user != null)
        {
            response.setData(user.getEmail());
        }

        return response;
    }

    @ApiMethod(
            name = "get",
            path = "myBean/{myData}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public MyBean get(@Named("myData") String myData) throws NotFoundException {

        MyBean myBean = ofy().load().type(MyBean.class).id(myData).now();
        if (myBean == null) {
            throw new NotFoundException("Could not find MyBean with ID: " + myData);
        }
        return myBean;
    }

    @ApiMethod(
            name = "list",
            path = "myBean",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<MyBean> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<MyBean> query = ofy().load().type(MyBean.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<MyBean> queryIterator = query.iterator();
        List<MyBean> myBeanList = new ArrayList<MyBean>(limit);
        while (queryIterator.hasNext()) {
            myBeanList.add(queryIterator.next());
        }
        return CollectionResponse.<MyBean>builder().setItems(myBeanList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String myData) throws NotFoundException {
        try {
            ofy().load().type(MyBean.class).id(myData).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find MyBean with ID: " + myData);
        }
    }

}
