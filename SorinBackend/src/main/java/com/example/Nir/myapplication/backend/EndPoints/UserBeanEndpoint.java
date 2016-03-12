package com.example.Nir.myapplication.backend.EndPoints;

import com.example.Nir.myapplication.backend.Models.UserBean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "userBeanApi",
        version = "v1",
        resource = "userBean",
        namespace = @ApiNamespace(
                ownerDomain = "Models.backend.myapplication.Nir.example.com",
                ownerName = "Models.backend.myapplication.Nir.example.com",
                packagePath = ""
        )
)
public class UserBeanEndpoint {

    private static final Logger logger = Logger.getLogger(UserBeanEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(UserBean.class);
    }

    /**
     * Returns the {@link UserBean} with the corresponding ID.
     *
     * @param userEmail the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code UserBean} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "userBean/{userEmail}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public UserBean get(@Named("userEmail") String userEmail) throws NotFoundException {
        logger.info("Getting UserBean with ID: " + userEmail);
        UserBean userBean = ofy().load().type(UserBean.class).id(userEmail).now();
        if (userBean == null) {
            throw new NotFoundException("Could not find UserBean with ID: " + userEmail);
        }
        return userBean;
    }

    /**
     * Inserts a new {@code UserBean}.
     */
    @ApiMethod(
            name = "insert",
            path = "userBean",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserBean insert(UserBean userBean) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that userBean.userEmail has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(userBean).now();
        logger.info("Created UserBean with ID: " + userBean.getUserEmail());

        return ofy().load().entity(userBean).now();
    }

    /**
     * Updates an existing {@code UserBean}.
     *
     * @param userEmail the ID of the entity to be updated
     * @param userBean  the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code userEmail} does not correspond to an existing
     *                           {@code UserBean}
     */
    @ApiMethod(
            name = "update",
            path = "userBean/{userEmail}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public UserBean update(@Named("userEmail") String userEmail, UserBean userBean) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(userEmail);
        ofy().save().entity(userBean).now();
        logger.info("Updated UserBean: " + userBean);
        return ofy().load().entity(userBean).now();
    }

    /**
     * Deletes the specified {@code UserBean}.
     *
     * @param userEmail the ID of the entity to delete
     * @throws NotFoundException if the {@code userEmail} does not correspond to an existing
     *                           {@code UserBean}
     */
    @ApiMethod(
            name = "remove",
            path = "userBean/{userEmail}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("userEmail") String userEmail) throws NotFoundException {
        checkExists(userEmail);
        ofy().delete().type(UserBean.class).id(userEmail).now();
        logger.info("Deleted UserBean with ID: " + userEmail);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "userBean",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<UserBean> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<UserBean> query = ofy().load().type(UserBean.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<UserBean> queryIterator = query.iterator();
        List<UserBean> userBeanList = new ArrayList<UserBean>(limit);
        while (queryIterator.hasNext()) {
            userBeanList.add(queryIterator.next());
        }
        return CollectionResponse.<UserBean>builder().setItems(userBeanList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String userEmail) throws NotFoundException {
        try {
            ofy().load().type(UserBean.class).id(userEmail).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find UserBean with ID: " + userEmail);
        }
    }
}