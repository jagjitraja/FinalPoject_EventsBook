package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import eventsbook.t00533766.eventsbook.EventData.User;

public class UserTypeConverters {

    @TypeConverter
    public static User getUserFromJSONString (String json){
        Gson gson = new Gson();
        return gson.fromJson(json,User.class);
    }

    @TypeConverter
    public static String getUserJSONString (User user){
        Gson gson = new Gson();
        return gson.toJson(user);
    }

}
