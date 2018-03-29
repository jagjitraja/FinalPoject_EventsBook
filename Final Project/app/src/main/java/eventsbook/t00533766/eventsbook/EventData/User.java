package eventsbook.t00533766.eventsbook.EventData;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by T00533766 on 3/16/2018.
 */
@Entity
public class User implements Serializable  {

    @PrimaryKey
    private String userID;
    @ColumnInfo
    private String userName;
    @ColumnInfo
    private String userEmail;

    private ArrayList<Event> postedEvents;
    private ArrayList<Event> attendingEvents;
    private ArrayList<Event> savedEvents;


    public User(String userID,
                String userName,
                String userEmail) {

        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.postedEvents = new ArrayList<>();
        this.attendingEvents = new ArrayList<>();
        this.savedEvents = new ArrayList<>();
    }

    public ArrayList<Event> getAttendingEvents() {
        return attendingEvents;
    }

    public void addAttendingEvent(Event attendingEvent) {
        this.attendingEvents.add(attendingEvent);
    }

    public ArrayList<Event> getSavedEvents() {
        return savedEvents;
    }

    public void addSavedEvents(Event savedEvent) {
        this.savedEvents.add(savedEvent);
    }

    public ArrayList<Event> getPostedEvents() {
        return postedEvents;
    }

    public void addPostedEvents(Event postedEvent) {
        this.postedEvents.add(postedEvent);
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
