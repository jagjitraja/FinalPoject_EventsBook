package eventsbook.t00533766.eventsbook.EventData;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eventsbook.t00533766.eventsbook.Utilities.DateTypeConverters;
import eventsbook.t00533766.eventsbook.Utilities.UserArrayListTypeConverter;
import eventsbook.t00533766.eventsbook.Utilities.UserTypeConverters;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

/**
 * Created by T00533766 on 3/16/2018.
 */

@Entity(tableName = "Events")
public class Event implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int eventID;
    @ForeignKey(entity = User.class, parentColumns = "postedBy", childColumns = "userID")

    @ColumnInfo
    @TypeConverters(UserTypeConverters.class)
    private User postedBy;
    @ColumnInfo
    private String eventName;
    @ColumnInfo
    private String description;

    @TypeConverters(DateTypeConverters.class)
    @ColumnInfo
    private Date eventDate;
    @ColumnInfo
    private double eventPrice;
    @ColumnInfo
    private String addressLocation;
    @ColumnInfo
    private String attendingUsersJSON;
    @ColumnInfo
    private String interestedUsersJSON;

    //TODO: STORE ARRAY LIST IN DATABASE
    @Ignore
    private ArrayList<User> attendingUsers;
    @Ignore
    private ArrayList<User> interestedUsers;
    @Ignore
    private SimpleDateFormat dateFormat = new SimpleDateFormat("YY/MM/DD HH:mm");

    public Event(){
        this.eventID = 0;
        this.eventName = "";
        this.description = "";
        this.eventDate = null;
        this.postedBy = null;
        this.attendingUsers = new ArrayList<>();
        this.interestedUsers = new ArrayList<>();
        this.eventPrice = 0;
        this.addressLocation = "";

        this.attendingUsersJSON = UserArrayListTypeConverter.getUserJSONStringFromArray(this.attendingUsers);
        this.interestedUsersJSON = UserArrayListTypeConverter.getUserJSONStringFromArray(this.interestedUsers);
    }

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

        this.attendingUsersJSON = UserArrayListTypeConverter.getUserJSONStringFromArray(this.attendingUsers);
        this.interestedUsersJSON = UserArrayListTypeConverter.getUserJSONStringFromArray(this.interestedUsers);
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

    public void addAttendingUsers(User attendingUser) {
        this.attendingUsers.add(attendingUser);
        this.attendingUsersJSON = UserArrayListTypeConverter.getUserJSONStringFromArray(this.attendingUsers);
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
        this.interestedUsersJSON = UserArrayListTypeConverter.getUserJSONStringFromArray(this.interestedUsers);
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

    @Override
    public String toString() {
        return this.eventName+"  "+this.eventPrice+"  "+this.description+"  "+this.getStringDate();
    }

    public String getInterestedUsersJSON() {
        return interestedUsersJSON;
    }

    public String getAttendingUsersJSON() {
        return attendingUsersJSON;
    }

    public void setAttendingUsersJSON(String JSON){
        this.attendingUsersJSON = JSON;
    }
    public void setInterestedUsersJSON(String JSON){
        this.interestedUsersJSON = JSON;
    }
    @TypeConverter
    public static Event getEventFromJSONString (String json){
        Gson gson = new Gson();
        return gson.fromJson(json,Event.class);
    }

    @TypeConverter
    public static String getEventJSONString (Event event){
        Gson gson = new Gson();
        return gson.toJson(event);
    }

}
