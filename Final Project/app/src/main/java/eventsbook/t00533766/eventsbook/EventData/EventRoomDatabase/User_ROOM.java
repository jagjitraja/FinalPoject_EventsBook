//package eventsbook.t00533766.eventsbook.EventData;
//
//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.Ignore;
//import android.arch.persistence.room.PrimaryKey;
//import android.arch.persistence.room.TypeConverter;
//import android.arch.persistence.room.TypeConverters;
//import android.support.annotation.NonNull;
//
//import com.google.firebase.auth.FirebaseUser;
//import com.google.gson.Gson;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
//import eventsbook.t00533766.eventsbook.Utilities.EventArrayListTypeConverter;
//import eventsbook.t00533766.eventsbook.Utilities.Utils;
//
///**
// * Created by T00533766 on 3/16/2018.
// */
//@Entity
//public class User implements Serializable  {
//
//    @NonNull
//    @PrimaryKey
//    private String userID;
//    @ColumnInfo
//    private String userName;
//    @ColumnInfo
//    private String userEmail;
//    @ColumnInfo
//    private String attendingEventsJSON;
//    @ColumnInfo
//    private String savedEventsJSON;
//    @ColumnInfo
//    private String postedEventsJSON;
//
//    @Ignore
//    private ArrayList<Event> postedEvents;
//    @Ignore
//    private ArrayList<Event> attendingEvents;
//    @Ignore
//    private ArrayList<Event> savedEvents;
//
//    public User() {
//        this.userID = "";
//        this.userName = "";
//        this.userEmail = "";
//        this.postedEvents = new ArrayList<>();
//        this.attendingEvents = new ArrayList<>();
//        this.savedEvents = new ArrayList<>();
//
//        this.attendingEventsJSON = EventArrayListTypeConverter.getEventJSONStringFromArray(attendingEvents);
//        this.savedEventsJSON =  EventArrayListTypeConverter.getEventJSONStringFromArray(savedEvents);
//        this.postedEventsJSON =  EventArrayListTypeConverter.getEventJSONStringFromArray(postedEvents);
//    }
//    public User(String userID,
//                String userName,
//                String userEmail) {
//        this.userID = userID;
//        this.userName = userName;
//        this.userEmail = userEmail;
//        this.postedEvents = new ArrayList<>();
//        this.attendingEvents = new ArrayList<>();
//        this.savedEvents = new ArrayList<>();
//
//        this.attendingEventsJSON = EventArrayListTypeConverter.getEventJSONStringFromArray(attendingEvents);
//        this.savedEventsJSON =  EventArrayListTypeConverter.getEventJSONStringFromArray(savedEvents);
//        this.postedEventsJSON =  EventArrayListTypeConverter.getEventJSONStringFromArray(postedEvents);
//    }
//
//    public ArrayList<Event> getAttendingEvents() {
//        return attendingEvents;
//    }
//
//    public void addAttendingEvent(Event attendingEvent) {
//        this.attendingEvents.add(attendingEvent);
//    }
//
//    public ArrayList<Event> getSavedEvents() {
//        return savedEvents;
//    }
//
//    public void addSavedEvents(Event savedEvent) {
//        this.savedEvents.add(savedEvent);
//    }
//
//    public ArrayList<Event> getPostedEvents() {
//        return postedEvents;
//    }
//
//    public void addPostedEvents(Event postedEvent) {
//        this.postedEvents.add(postedEvent);
//    }
//
//
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserID() {
//        return userID;
//    }
//
//    public void setUserID(String userID) {
//        this.userID = userID;
//    }
//
//    public String getPostedEventsJSON() {
//        return postedEventsJSON;
//    }
//
//    public String getSavedEventsJSON() {
//        return savedEventsJSON;
//    }
//
//    public String getAttendingEventsJSON() {
//        return attendingEventsJSON;
//    }
//
//    public void setPostedEventsJSON(String JSON) {
//        this.postedEventsJSON = JSON;
//    }
//
//    public void setSavedEventsJSON(String JSON) {
//        this.savedEventsJSON = JSON;
//    }
//
//    public void setAttendingEventsJSON(String JSON) {
//        this.attendingEventsJSON = JSON;
//    }
//
//
//}
