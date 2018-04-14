package eventsbook.t00533766.eventsbook.EventData;


import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eventsbook.t00533766.eventsbook.Utilities.Utils;

/**
 * Created by T00533766 on 3/16/2018.
 */

public class Event implements Serializable{

    private String eventID;
    private User postedBy;
    private String eventName;
    private String description;
    private String eventDate;
    private double eventPrice;
    private String addressLocation;
    private int interestedUsersCount;
    private int attendingUsersCount;
    private ArrayList<String> attendingUsers;
    private ArrayList<String> interestedUsers;
    private String storageURL;
    private String imageID;



    public Event(){
        this.eventID = "";
        this.eventName = "";
        this.description = "";
        this.eventDate = null;
        this.postedBy = null;
        this.attendingUsers = new ArrayList<>();
        this.interestedUsers = new ArrayList<>();
        this.eventPrice = 0;
        this.addressLocation = "";
        storageURL = "";
        imageID = "";
    }

    public Event(String eventID, String eventName,
                 String description, String eventDate,
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

    public void setAttendingUsers(ArrayList<String> attendingUsers){
        this.attendingUsers = attendingUsers;
    }

    public void setInterestedUsers(ArrayList<String> interestedUsers){
        this.interestedUsers = interestedUsers;
    }
    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public ArrayList<String> getAttendingUsers() {
        return attendingUsers;
    }

    public int getAttendingUsersCount(){
        return attendingUsers.size();
    }

    public void addAttendingUsers(String attendingUser) {
        this.attendingUsers.add(attendingUser);
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        Log.d(postedBy+"][]][][][][][][][][", "setPostedBy: ");
        this.postedBy = postedBy;
    }

    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        if (eventDate.equals("")){
            this.eventDate = Utils.dateFormat.format(new Date());
            return;
        }
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ArrayList<String> getInterestedUsers() {
        return interestedUsers;
    }

    public void addInterestedUsers(String interestedUser) {
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

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    @Override
    public String toString() {
        return this.eventID+"**********************************************\n"+
                "Attend "+this.eventName+"\n"+" for only "+this.eventPrice+"\n"
                +"DESCRIPTION=> "+ this.description+"\n"+"Hosted By=> "+this.postedBy+"\nADDRESSS => "+
                this.addressLocation+
                this.interestedUsers+"\n ATTENDING => "+this.attendingUsers+"\n"+
                this.eventDate+"\n"+
                this.storageURL;
    }

    public void removeAttendingUser(String userID) {
        this.attendingUsers.remove(userID);
    }

    public void removeInterestedUser(String userID) {
        this.interestedUsers.remove(userID);
    }

    public void addInterestedUser(String userID) {
        this.interestedUsers.add(userID);
    }

    public String getStorageURL() {
        return storageURL;
    }
    public void setStorageURL(String storageURL) {
        this.storageURL = storageURL;
    }

    public String getImageID() {
        return imageID;
    }
    public void setImageID(String imageID) {
        this.imageID = imageID;
    }
}
