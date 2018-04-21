package eventsbook.t00533766.eventsbook.Utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import eventsbook.t00533766.eventsbook.EventData.User;

/**
 * Created by T00533766 on 4/11/2018.
 */

public abstract class LoggedInUserSingleton {

    //SINGLETON TO SAVE LOGGGED IN USER DETAILS

    private static User loggedInUser;

    private LoggedInUserSingleton(){

    }
    public static User getLoggedInUser(){

        if (loggedInUser ==null){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String name = firebaseAuth.getCurrentUser().getDisplayName();
            if (name==null){
                name = "Not Available";
            }
            loggedInUser = new User(firebaseAuth.getUid(), name,firebaseAuth.getCurrentUser().getEmail());
        }
        return loggedInUser;
    }
}

//TODO:USING SINGLETON