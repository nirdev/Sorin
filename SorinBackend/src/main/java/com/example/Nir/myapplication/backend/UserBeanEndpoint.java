package com.example.Nir.myapplication.backend;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;


@Api(
        name = "userBeanApi",
        version = "v1",
        resource = "userBean",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Nir.example.com",
                ownerName = "backend.myapplication.Nir.example.com",
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
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code UserBean} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "userBean/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public UserBean get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting UserBean with ID: " + id);
        UserBean userBean = ofy().load().type(UserBean.class).id(id).now();

        if (userBean == null) {
            throw new NotFoundException("Could not find UserBean with ID: " + id);
        }
        return userBean;
    }


    @ApiMethod(
            name = "register",
            path = "userBean",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserBean register(UserBean userBean) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that userBean.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.


        //TODO: delete when finish building friend list
//        LinkedHashMap< String, Long> friendsList = new LinkedHashMap<>();
//        friendsList.put("examplegogo",123l);
//        friendsList.put("examplegogo1",123l);
//        friendsList.put("examplegogo2",123l);
//        userBean.setUserFriends(friendsList);

        ofy().save().entity(userBean).now();
        logger.info("Created UserBean with ID: " + userBean.getId());

        return ofy().load().entity(userBean).now();
    }



    /**
     * Updates an existing {@code UserBean}.
     *
     * @param id       the ID of the entity to be updated
     * @param hashMapWarrper key = Contact ,phone Value = contact Name
     * @return HashMap < Contatct name , userBeanId>
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code UserBean}
     */
    @ApiMethod(
            name = "buildFriendsList",
            path = "userBean/{id}",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserBean buildFriendsList(@Named("id") Long id, LinkedHashMapWarrper hashMapWarrper) {


        Long mFriendId;
        LinkedHashMap<String,String> newFriendListHM = new LinkedHashMap<>();


        //Set contactList from client side in temporary LHM
        LinkedHashMap<String,String> oldFriendListHM;
        oldFriendListHM = hashMapWarrper.getLinkedHashMap();

        /**
        @params - deleteDuplicatePhoneNumbers() -  because PhoneNumber is the Map key if the user have phone
        duplicates they being automatically deleted by the hashPap
        **/

        //Loop on User ContactList
        for (String key : oldFriendListHM.keySet()){

            //mFriend = result of checkExistsByPhone with Param of current phone from mContactList
            mFriendId = checkExistsByPhone(key);

           /*
            Validate that the phone is for register user in our data,
            if not the phone not added to the new "FriendsList"
            */
            if(mFriendId != null){
                newFriendListHM.put(key , mFriendId.toString());
            }
        }

        //Load User entity from Id
        UserBean userBean =  ofy().load().type(UserBean.class).id(id).safe();

        //Set the specific entity the friend list
        userBean.setUserFriends(newFriendListHM);

        //Save the entity back in the datastore
        ofy().save().entity(userBean).now();


        return ofy().load().type(UserBean.class).id(id).now();
    }



//        LinkedHashMap<String, Long> friendsList = new LinkedHashMap<>();
//        Long mFriendId;
//
//        //Loop on User ContactList
//        for (int i = 0; i < mContactsPhone.size(); i++){
//
//            //mFriend = result of checkExistsByPhone with Param of current phone from mContactList
//            mFriendId = checkExistsByPhone(mContactsPhone.get(i));
//
//           /*
//            Validate that the phone is for register user in our data,
//            if not the phone not added to the new "FriendsList"
//            */
//            if(mFriendId != null){
//                friendsList.put(mContactsName.get(i) , mFriendId);
//            }
//        }
//
//        //Load User entity from Id
//        UserBean userBean =  ofy().load().type(UserBean.class).id(id).safe();
//
//        //Set the specific entity the friend list
//        userBean.setUserFriends(friendsList);
//
//        //Save the entity back in the datastore
//        ofy().save().entity(userBean).now();
//
//        return friendsList;




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
    public CollectionResponse<UserBean> list(@Nullable @Named("cursor") String cursor,
                                             @Nullable @Named("limit") Integer limit) {
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

    private Boolean checkExistsById(Long id)  {
        try {
            ofy().load().type(UserBean.class).id(id).safe();
            return true;
        } catch (com.googlecode.objectify.NotFoundException e) {
            return false;
        }

    }
    private Long checkExistsByPhone(String phone){

        UserBean userBean =  ofy().load().type(UserBean.class).filter("userPhone", phone).first().now();
        if (userBean != null){
            return userBean.getId();
        }
        else {
            return null;
        }


    }
}