package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eventsbook.t00533766.eventsbook.EventData.User;

public class DateTypeConverters {

    private static final String  TAG = "DateTypeConverters";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY HH:mm");

    @TypeConverter
    public static Date getDateFromString (String json){
        try {
            return simpleDateFormat.parse(json);
        } catch (ParseException e) {
            Log.d(TAG, "getDateFromString: " +e.getMessage());
            return new Date(System.currentTimeMillis());
        }
    }

    @TypeConverter
    public static String getDateString (Date date){
        return simpleDateFormat.format(date);
    }
}
