package eventsbook.t00533766.eventsbook.Utilities;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;

public class UserArrayListTypeConverter {

    @TypeConverter
    public static String getUserJSONStringFromArray (ArrayList<User> eventArrayList){
        Gson gson = new Gson();
        return gson.toJson(eventArrayList);
    }
    @TypeConverter
    public static ArrayList<Event> getUserArrayListFromJSONString(String arrayString){
        TypeToken<ArrayList<Event>> token = new TypeToken<ArrayList<Event>>() {};
        Gson gson = new Gson();
        return gson.fromJson(arrayString,token.getType());
    }

}
