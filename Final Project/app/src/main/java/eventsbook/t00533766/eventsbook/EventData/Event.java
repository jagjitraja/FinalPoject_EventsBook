package eventsbook.t00533766.eventsbook.EventData;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by T00533766 on 3/16/2018.
 */

@Entity
public class Event implements Serializable{

    @PrimaryKey
    private int eventID;
    @ForeignKey(entity = User.class, parentColumns = "postedBy", childColumns = "userID")
    @ColumnInfo
    private User postedBy;
    @ColumnInfo
    private String eventName;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private Date eventDate;
    @ColumnInfo
    private double eventPrice;
    @ColumnInfo
    private String addressLocation;

    //TODO: STORE ARRAY LIST IN DATABASE
    private ArrayList<User> attendingUsers;
    private ArrayList<User> interestedUsers;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("YY/MM/DD HH:MI");

    public Event(int eventID, String eventName,
                 String description, Date eventDate,
                 User postedBy,
                 double eventPrice,
                 String addressLocation) {

        this.eventID = eventID;
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.postedBy = postedBy;
        this.attendingUsers = new ArrayList<>();
        this.interestedUsers = new ArrayList<>();
        this.eventPrice = eventPrice;
        this.addressLocation = addressLocation;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }


    public ArrayList<User> getAttendingUsers() {
        return attendingUsers;
    }

    public int getAttendingUsersCount(){
        return attendingUsers.size();
    }

    public void setAttendingUsers(User attendingUser) {
        this.attendingUsers.add(attendingUser);
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getStringDate(){
        return dateFormat.format(eventDate);
    }
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ArrayList<User> getInterestedUsers() {
        return interestedUsers;
    }

    public void addInterestedUsers(User interestedUser) {
        this.interestedUsers.add(interestedUser);
    }
    public int getInterestedUsersCount(){
        return interestedUsers.size();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(String addressLocation) {
        this.addressLocation = addressLocation;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
