package com.foodie.app.listsDB;

import android.content.ContentValues;
import android.database.Cursor;

import com.foodie.app.Helper.HelperClass;
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.Converters;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DataStatus;
import com.foodie.app.database.IDBManager;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;

import java.util.ArrayList;
import java.util.List;


/**
 * This class will manager a local DB by using list of every entity
 * This class implements the interface IDBManager
 */
public class ListDBManager implements IDBManager {

    //lists
    public static List<User> users;
    public static List<CPUser> cpusers;
    public static List<Business> businesses;
    public static List<Activity> activities;
    public CPUser cpuserConected;
    public User userConected;

    boolean isUpdated = false;

    /**
     * The local list (private)
     */
    static {
        users = new ArrayList<>();
        cpusers = new ArrayList<>();
        businesses = new ArrayList<>();
        activities = new ArrayList<>();
    }

    /**
     * Function to reset the database
     */
    public static void resetDB()
    {
        users.clear();
        cpusers.clear();
        businesses.clear();
        activities.clear();
    }

    /**
     * This function will return the greater id
     * @param classList : The entity type to search
     * @return : The id
     */
    private int getMaxID(String classList) {

        return users.size() + cpusers.size() + businesses.size() + activities.size();
    }

    /**
     * This function will add a CPUser entity to the DB
     * @param values : The new data
     * @return : The user id added
     */
    @Override
    public String addCPUser(ContentValues values) throws Exception {
        // Get values
        CPUser cpuser = Converters.ContentValuesToCPUser(values);

        int cpuserId = getMaxID("cpuser");

        cpuser.set_ID(Integer.toString(cpuserId));

        cpusers.add(cpuser);

        isUpdated = true;
        return cpuser.get_ID();
    }
    /**
     * This function will add a Business entity to the DB
     * @param values : The new data
     * @return : The user id added
     */
    @Override
    public String addBusiness(ContentValues values) throws Exception {
        Business business = Converters.ContentValuesToBusiness(values);
        int businessId = getMaxID("Business");

        business.set_ID(Integer.toString(businessId));
        businesses.add(business);
        isUpdated = true;


        return business.get_ID();
    }
    /**
     * This function will add a Activity entity to the DB
     * @param values : The new data
     * @return : The user id added
     */
    @Override
    public String addActivity(ContentValues values) throws Exception {
        Activity activity = Converters.ContentValuesToActivity(values);
        int activityId = getMaxID("activity");


        activity.set_ID(Integer.toString(activityId));

        activities.add(activity);
        isUpdated = true;
        return activity.get_ID();
    }
    /**
     * This function will add a User entity to the DB
     * @param values : The new data
     * @return : The user id added
     */
    @Override
    public String addUser(ContentValues values) throws Exception {
        User user = Converters.ContentValuesToUser(values);
        int userId = getMaxID("user");


        user.set_ID(Integer.toString(userId));

        users.add(user);
        isUpdated = true;

        return user.get_ID();
    }

    /**
     * This function will remove a CPUSer entity from the DB
     * @param id : Entity id to remove
     * @return : A boolean to know if the operation is completed
     */
    @Override
    public boolean removeCPUser(String id) throws Exception {
        CPUser cpuserToRemove = null;
        for (CPUser item : cpusers)
            if (item.get_ID().equals(id)) {
                cpuserToRemove = item;
                isUpdated = true;
                break;
            }

        return cpusers.remove(cpuserToRemove);
    }
    /**
     * This function will remove a Business entity from the DB
     * @param id : Entity id to remove
     * @return : A boolean to know if the operation is completed
     */
    @Override
    public boolean removeBusiness(String id) throws Exception {
        Business businessToRemove = null;
        for (Business item : businesses)
            if (item.get_ID().equals(id)) {
                businessToRemove = item;
                isUpdated = true;
                break;
            }

        return businesses.remove(businessToRemove);
    }

    /**
     * This function will remove a Activity entity from the DB
     * @param id : Entity id to remove
     * @return : A boolean to know if the operation is completed
     */
    @Override
    public boolean removeActivity(String id) throws Exception {
        Activity activityToRemove = null;
        for (Activity item : activities)
            if (item.get_ID().equals(id)) {
                activityToRemove = item;
                isUpdated = true;
                break;
            }

        return activities.remove(activityToRemove);
    }

    /**
     * This function will remove a user entity from the DB
     * @param id : Entity id to remove
     * @return : A boolean to know if the operation is completed
     */
    @Override
    public boolean removeUser(String id) throws Exception {
        User userToRemove = null;
        for (User item : users)
            if (item.get_ID().equals(id)) {
                userToRemove = item;
                isUpdated = true;
                break;
            }

        return users.remove(userToRemove);
    }

    /**
     * This function will make a query to the DB and return a Cpuser entity list
     * @param args : A array of strings with the arguments (ex: 5, moises )
     * @param columnsArgs A array of strings with the the arguments columns (ex: id, name )
     * @return the list with the data
     */
    @Override
    public Cursor getCPUser(String[] args, String[] columnsArgs) {

        List<CPUser> result = new ArrayList<>();
        boolean insert = true;
        if (columnsArgs != null) {
            for (CPUser user : cpusers) {
                for (int i = 0; i < columnsArgs.length; i++) {
                    switch (columnsArgs[i]) {
                        case AppContract.CPUser.CPUSER_ID:
                            if (!user.get_ID().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.CPUser.CPUSER_FULL_NAME:
                            if (!user.getUserFullName().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.CPUser.CPUSER_EMAIL:
                            if (!user.getUserEmail().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.CPUser.CPUSER_PWD:
                            if (!user.getUserPwdHash().equals(args[i])) {

                                insert = false;
                            }
                            break;
                    }
                    if (!insert)
                        break;
                }

                if (insert)
                    result.add(user);
                else
                    insert = true;
            }
            return Converters.CPUserListToCursor(result);
        } else
            return Converters.CPUserListToCursor(cpusers);
    }
    /**
     * This function will make a query to the DB and return a business entity list
     * @param args : A array of strings with the arguments (ex: 5, moises )
     * @param columnsArgs A array of strings with the the arguments columns (ex: id, name )
     * @return the list with the data
     */
    @Override
    public Cursor getBusiness(String[] args, String[] columnsArgs) {
        //    public Business(int id,String businessName, String businessAddress, String businessPhoneNo, String businessEmail, String businessWebsite, int cpuserID, byte[] businessLogo) throws Exception {

        List<Business> result = new ArrayList<>();
        boolean insert = true;
        if (columnsArgs != null) {
            for (Business bus : businesses) {
                for (int i = 0; i < columnsArgs.length; i++) {
                    switch (columnsArgs[i]) {
                        case AppContract.Business.BUSINESS_ID:
                            if (!bus.get_ID().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Business.BUSINESS_NAME:
                            if (!bus.getBusinessName().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Business.BUSINESS_ADDRESS:
                            if (!bus.getBusinessAddress().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Business.BUSINESS_PHONE_NUMBER:
                            if (!bus.getBusinessPhoneNo().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Business.BUSINESS_EMAIL:
                            if (!bus.getBusinessEmail().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Business.BUSINESS_WEBSITE:
                            if (!bus.getBusinessWebsite().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Business.BUSINESS_CPUSER_ID:
                            if (bus.getCpuserID().equals(args[i])) {

                                insert = false;
                            }
                            break;


                    }
                    if (!insert)
                        break;
                }

                if (insert)
                    result.add(bus);
                else
                    insert = true;
            }
            return Converters.BusinessListToCursor(result);
        } else
            return Converters.BusinessListToCursor(businesses);
    }

    /**
     * This function will make a query to the DB and return an Activity entity list
     * @param args : A array of strings with the arguments (ex: 5, moises )
     * @param columnsArgs A array of strings with the the arguments columns (ex: id, name )
     * @return the list with the data
     */
    @Override
    public Cursor getActivity(String[] args, String[] columnsArgs) {

        //    public Activity(int id,String activityName, String activityDescription, double activityCost, double activityRating, int businessId, byte[] activityImages, String feature) throws Exception {

        List<Activity> result = new ArrayList<>();
        boolean insert = true;
        if (columnsArgs != null) {
            for (Activity ac : activities) {
                for (int i = 0; i < columnsArgs.length; i++) {
                    switch (columnsArgs[i]) {
                        case AppContract.Activity.ACTIVITY_ID:
                            if (!ac.get_ID().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Activity.ACTIVITY_NAME:
                            if (!ac.getActivityName().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Activity.ACTIVITY_DESCRIPTION:
                            if (!ac.getActivityDescription().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Activity.ACTIVITY_COST:
                            if (!Double.toString(ac.getActivityCost()).equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Activity.ACTIVITY_RATING:
                            if (!Double.toString(ac.getActivityRating()).equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Activity.ACTIVITY_BUSINESS_ID:
                            if (!ac.getBusinessId().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.Activity.ACTIVITY_FEATURE:
                            if (!ac.getFeature().equals(args[i])) {

                                insert = false;
                            }
                            break;
                        default:
                            return null;

                    }
                    if (!insert)
                        break;
                }

                if (insert)
                    result.add(ac);
                else
                    insert = true;
            }
            return Converters.ActivitiesListToCursor(result);
        } else
            return Converters.ActivitiesListToCursor(activities);
    }

    /**
     * This function will make a query to the DB and return an User entity list
     * @param args : A array of strings with the arguments (ex: 5, moises )
     * @param columnsArgs A array of strings with the the arguments columns (ex: id, name )
     * @return the list with the data
     */
    @Override
    public Cursor getUser(String[] args, String[] columnsArgs) {



        List<User> result = new ArrayList<>();
        boolean insert = true;
        if (columnsArgs != null) {
            for (User us : users) {
                for (int i = 0; i < columnsArgs.length; i++) {
                    switch (columnsArgs[i]) {
                        case AppContract.User.USER_ID:
                            if (!us.get_ID().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.User.USER_FULL_NAME:
                            if (!us.getUserFullName().equals(args[i])) {

                                insert = false;
                            }

                        case AppContract.User.USER_EMAIL:
                            if (!us.getUserEmail().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.User.USER_PHONE_NUMBER:
                            if (!us.getUserPhoneNumber().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.User.USER_PWD:
                            if (!us.getUserPwdHash().equals(args[i])) {

                                insert = false;
                            }
                            break;

                        case AppContract.User.USER_ADDRESS:
                            if (!us.getUserAddress().equals(args[i])) {

                                insert = false;
                            }
                            break;


                    }
                    if (!insert)
                        break;
                }

                if (insert)
                    result.add(us);
                else
                    insert = true;
            }
            return Converters.UserListToCursor(result);
        } else
            return Converters.UserListToCursor(users);
    }

    /**
     * This function will update a CPUser entity by id
     * @param id : The entity id
     * @param values : The new data
     * @return :  A boolean to know if the operation is completed
     */
    @Override
    public boolean updateCPUser(String id, ContentValues values) throws Exception {
        CPUser cpuser = Converters.ContentValuesToCPUser(values);
        for (CPUser cp : cpusers) {
            if (cp.get_ID().equals(id)) {
                cp.set_ID(cpuser.get_ID());
                cp.setUserFullName(cpuser.getUserFullName());
                cp.setUserEmail(cpuser.getUserFullName());
                cp.setUserPwdHash(cpuser.getUserPwdHash());
                isUpdated = true;
                return true;
            }
        }
        return false;
    }
    /**
     * This function will update a Business entity by id
     * @param id : The entity id
     * @param values : The new data
     * @return :  A boolean to know if the operation is completed
     */
    @Override
    public boolean updateBusiness(String id, ContentValues values) throws Exception {
        Business business = Converters.ContentValuesToBusiness(values);
        for (Business b : businesses) {
            if (b.get_ID().equals(id)) {
                b.set_ID(business.get_ID());
                b.setBusinessName(business.getBusinessName());
                b.setBusinessAddress(business.getBusinessAddress());
                b.setBusinessPhoneNo(business.getBusinessPhoneNo());
                b.setBusinessEmail(business.getBusinessEmail());
                b.setBusinessWebsite(business.getBusinessWebsite());
                b.setCpuserID(business.getCpuserID());
                b.setBusinessLogo(business.getBusinessLogo());

                isUpdated = true;
                return true;
            }
        }
        return false;
    }

    /**
     * This function will update an activity entity by id
     * @param id : The entity id
     * @param values : The new data
     * @return :  A boolean to know if the operation is completed
     */
    @Override
    public boolean updateActivity(String id, ContentValues values) throws Exception {
        Activity activity = Converters.ContentValuesToActivity(values);
        for (Activity a : activities) {
            if (a.get_ID().equals(id)) {
                a.set_ID(activity.get_ID());
                a.setActivityName(activity.getActivityName());
                //a.setActivityDate(activity.getActivityDate());
                a.setActivityDescription(activity.getActivityDescription());
                a.setActivityCost(activity.getActivityCost());
                a.setActivityRating(activity.getActivityRating());
                a.setActivityImage(activity.getActivityImage());
                a.setBusinessId(activity.getBusinessId());
                a.setFeature(activity.getFeature());

                isUpdated = true;
                return true;
            }
        }
        return false;
    }

    /**
     * This function will update an user entity by id
     * @param id : The entity id
     * @param values : The new data
     * @return :  A boolean to know if the operation is completed
     */
    @Override
    public boolean updateUser(String id, ContentValues values) throws Exception {
        User user = Converters.ContentValuesToUser(values);
        for (User u : users) {
            if (u.get_ID().equals(id)) {
                u.set_ID(user.get_ID());
                u.setUserPhoneNumber(user.getUserPhoneNumber());
                u.setUserPwdHash(user.getUserPwdHash());
                u.setUserAddress(user.getUserAddress());
                u.setUserFullName(user.getUserFullName());
                u.setUserEmail(user.getUserEmail());
                u.setUserImage(user.getUserImage());

                isUpdated = true;
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return if the data base is updated (not in used)
     */
    @Override
    public boolean isDBUpdated() {
        return isUpdated;
    }

    /**
     * This function will check if the credentials are corrects (by checking if the user/password exist)
     * @param callBack : the callback function when its done
     */
    public static void login(final String username, final String password, final CallBack<CPUser> callBack) {

        for (CPUser user : cpusers) {
            if (user.getUserEmail().equals(username) && user.getUserEmail().equals(password)) {
                {
                    DBManagerFactory.setCurrentUser(user);
                    HelperClass.runInMain(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(null);
                        }

                    });
                    return;
                }


            }
        }

        HelperClass.runInMain(new Runnable() {
            @Override
            public void run() {
                callBack.onFailed(DataStatus.Failed, "Invalid username or password");
            }
        });
    }

    /**
     *  For the firebase DB class:
     *  This function will update a CPUSer
     * @param values : The entity data
     */
    public void updateCPUser(CPUser values) {
        for (CPUser cpuser:cpusers ) {
            if(values.get_ID().equals(cpuser.get_ID()))
            {
                cpusers.remove(cpuser);
                break;
            }

        }
        cpusers.add(values);
        isUpdated = true;

    }

    /**
     *  For the firebase DB class:
     *  This function will update a business
     * @param values : The entity data
     */
    public void updateBusiness(Business values) {

        for (Business business:businesses ) {
            if(values.get_ID().equals(business.get_ID()))
            {
                businesses.remove(business);
                break;
            }

        }

        businesses.add(values);
        isUpdated = true;

    }

    /**
     *  For the firebase DB class:
     *  This function will update an Activity
     * @param values : The entity data
     */
    public void updateActivity(Activity values) {

        for (Activity activies:activities ) {
            if(values!=null && values.get_ID().equals(activies.get_ID()))
            {
                activities.remove(activies);
                break;
            }

        }

        activities.add(values);
        isUpdated = true;

    }


    /**
     * For the firebase DB class:
     * This function will update an user
     * @param user : The entity data
     */
    public void updateUser(User user) {


        for (User userlist:users ) {
            if(user.get_ID().equals(userlist.get_ID()))
            {
                users.remove(userlist);
                break;
            }

        }
        users.add(user);
        isUpdated = true;

    }

    /**
     * For the firebase DB class:
     * This function will insert a CPUser to the database
     * @param values: The entity data
     * @return :  the id inserted
     */
    public String addCPUser(CPUser values) {
        cpusers.add(values);
        isUpdated = true;
        return values.get_ID();
    }

    /**
     * For the firebase DB class:
     * This function will insert a business to the database
     * @param values: The entity data
     * @return :  the id inserted
     */
    public String addBusiness(Business values) {

        boolean update = false;
        for (Business b : businesses) {
            if (b.equals(values)) {
                b = values;
                update = true;
                break;
            }
        }
        if(!update) {
            businesses.add(values);
            isUpdated = true;
        }
        return values.get_ID();
    }

    /**
     * For the firebase DB class:
     * This function will insert an actvity to the database
     * @param values: The entity data
     * @return :  the id inserted
     */
    public String addActivity(Activity values) {


        for(Activity activity:activities)
        {
            if(activity.get_ID().equals(values.get_ID()))
                return activity.get_ID();
        }

       activities.add(values);
        return values.get_ID();
    }


    /**
     * For the firebase DB class:
     * This function will insert an user to the database
     * @param values: The entity data
     * @return :  the id inserted
     */
    public String addUser(User user) {

        users.add(user);
        isUpdated = true;
        return user.get_ID();
    }


    /**
     * This function will register a new user
     * @param user : The cpuser details
     * @param callBack : The callback function to execute after the operation
     */
    public static void signUp(CPUser user, CallBack<CPUser> callBack) {
        for (CPUser localUser : cpusers) {
            if (localUser.getUserEmail().equals(user.getUserEmail())) {
                callBack.onFailed(DataStatus.Failed, "Email already exist");
                return;
            }

        }
        cpusers.add(user);
        callBack.onSuccess(null);

    }


    /**
     * @return : the CPUser list size
     */
    public static int getCpusersListSize() {
        return cpusers.size();
    }
    /**
     * @return : the user list size
     */
    public static int getUsersListSize() {
        return users.size();
    }
    /**
     * @return : the activity list size
     */
    public static int getActivitiesListSize() {
        return activities.size();
    }
    /**
     * @return : the business list size
     */
    public static int getBusinessListSize() {
        return businesses.size();
    }


    /**
     * This function update a business picture by id
     * @param id : the id
     * @param img  : the picture
     */
    public static void UpdateBusinessPicture(String id,byte[] img)
    {
        Business business;
        int i = 0;
        for(Business b:businesses)
        {

            if(b.get_ID().equals(id))
            {
                business = b;
                b.setBusinessLogo(img);
                businesses.set(i,b);
                break;
            }
            i++;
        }
        DBManagerFactory.setDBupdated(false);
    }
    /**
     * This function update a activity picture by id
     * @param id : the id
     * @param img  : the picture
     */
    public static void UpdateActivityPicture(String id,byte[] img)
    {
        Activity activity;
        int i = 0;
        for(Activity a:activities)
        {

            if(a.get_ID().equals(id))
            {
                activity = a;
                a.setActivityImage(img);
                activities.set(i,a);
                break;
            }
            i++;
        }
        DBManagerFactory.setDBupdated(false);
    }


}
