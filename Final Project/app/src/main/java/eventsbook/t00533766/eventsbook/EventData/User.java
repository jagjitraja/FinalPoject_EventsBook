package eventsbook.t00533766.eventsbook.EventData;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Created by T00533766 on 3/16/2018.
 */
@Entity
public class User {

    @PrimaryKey
    private int userID;
    @ColumnInfo
    private String userName;
    @ColumnInfo
    private String userEmail;
    @ColumnInfo
    private String userPassword;

    private ArrayList<Event> postedEvents;
    private ArrayList<Event> attendingEvents;
    private ArrayList<Event> savedEvents;


    public User(int userID,
                String userName,
                String userEmail,
                String userPassword,
                ArrayList<Event> postedEvents,
                ArrayList<Event> attendingEvents,
                ArrayList<Event> savedEvents) {

        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.postedEvents = postedEvents;
        this.attendingEvents = attendingEvents;
        this.savedEvents = savedEvents;
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
