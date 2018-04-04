package eventsbook.t00533766.eventsbook.EventData;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by T00533766 on 3/16/2018.
 */

public class Event implements Serializable{

    private String eventID;
    private User postedBy;
    private String eventName;
    private String description;
    private Date eventDate;
    private double eventPrice;
    private String addressLocation;
    private ArrayList<String> attendingUsers;
    private ArrayList<String> interestedUsers;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("YY/MM/DD HH:mm");

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
    }

    public Event(String eventID, String eventName,
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
        return this.eventID+"  "+ this.eventName+"  "+this.eventPrice+"  "+this.description+"  "+this.getStringDate();
    }
}
