package eventsbook.t00533766.eventsbook.EventData;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by T00533766 on 3/16/2018.
 */
public class User implements Serializable  {

    private String userID;
    private String userName;
    private String userEmail;
    private ArrayList<Event> attendingEvents;
    private ArrayList<Event> savedEvents;

    public User() {
        this.userID = "";
        this.userName = "";
        this.userEmail = "";
        this.attendingEvents = new ArrayList<>();
        this.savedEvents = new ArrayList<>();
    }
    public User(String userID,
                String userName,
                String userEmail) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
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

    @Override
    public String toString() {
        return this.userID+"  "+this.userName;
    }
}
