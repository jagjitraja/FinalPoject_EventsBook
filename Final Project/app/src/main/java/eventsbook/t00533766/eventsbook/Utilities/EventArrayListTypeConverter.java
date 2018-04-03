package eventsbook.t00533766.eventsbook.Utilities;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.EventData.Event;

public class EventArrayListTypeConverter {
    @TypeConverter
    public static ArrayList<Event> getEventArrayListFromJSONString(String arrayString){
        TypeToken<ArrayList<Event>> token = new TypeToken<ArrayList<Event>>() {};
        Gson gson = new Gson();
        return gson.fromJson(arrayString,token.getType());
    }

    @TypeConverter
    public static String getEventJSONStringFromArray (ArrayList<Event> eventArrayList){
        Gson gson = new Gson();
        return gson.toJson(eventArrayList);
    }

}
