package eventsbook.t00533766.eventsbook.Utilities;

import com.google.firebase.auth.FirebaseUser;

import eventsbook.t00533766.eventsbook.EventData.User;

/**
 * Created by T00533766 on 4/11/2018.
 */

public abstract class LoggedInUserSingleton {


    private static User user;

    private LoggedInUserSingleton(){


    }

    public static User getLoggedInUser(FirebaseUser firebaseUser){

        if (user==null){

        }
        return user;
    }
}

//TODO:USING SINGLETON