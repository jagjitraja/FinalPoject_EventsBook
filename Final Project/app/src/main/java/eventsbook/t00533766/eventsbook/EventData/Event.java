package eventsbook.t00533766.eventsbook.EventData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by T00533766 on 3/16/2018.
 */

public class Event {

    private String eventName;
    private String description;
    private Date eventDate;
    private User postedBy;
    private ArrayList<User> attendingUsers;
    private ArrayList<User> interestedUsers;
    private boolean publicEvent;
    private boolean hasTicketPrice;
    private double eventPrice;
    private String addressLocation;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("YY/MM/DD HH:MI");

    public Event(String eventName,
                 String description, Date eventDate,
                 User postedBy,
                 ArrayList<User> attendingUsers,
                 ArrayList<User> interestedUsers, boolean publicEvent,
                 boolean hasTicketPrice,
                 double eventPrice, String addressLocation) {

        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.postedBy = postedBy;
        this.attendingUsers = attendingUsers;
        this.interestedUsers = interestedUsers;
        this.publicEvent = publicEvent;
        this.hasTicketPrice = hasTicketPrice;
        this.eventPrice = eventPrice;
        this.addressLocation = addressLocation;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public boolean isHasTicketPrice() {
        return hasTicketPrice;
    }

    public void setHasTicketPrice(boolean hasTicketPrice) {
        this.hasTicketPrice = hasTicketPrice;
    }

    public boolean isPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(boolean publicEvent) {
        this.publicEvent = publicEvent;
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
}
