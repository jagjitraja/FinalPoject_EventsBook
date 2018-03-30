package eventsbook.t00533766.eventsbook.Utilities;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.Activites_Fragments.MainActivity;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;

/**
 * Created by T00533766 on 3/16/2018.
 */

public class Utils {


    private static final String TAG = Utils.class.getName();

    public static void goToActivity(final Intent intent,
                                    int delay, final Context context){

        Runnable goToActivity = new Runnable() {
            @Override
            public void run() {
                context.startActivity(intent);
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(goToActivity, delay);
    }


    public static void showSnackBar(View v, String error, String actiontext,
                                    View.OnClickListener clickListener) {
        Log.d(TAG, "showSnackBar: ");
        Snackbar snackbar = Snackbar.make(v, error, Snackbar.LENGTH_SHORT);
        if (clickListener == null||actiontext==null) {
            snackbar.show();
        } else {
            snackbar.setAction(actiontext, clickListener).show();
        }
    }

    @TypeConverter
    public static ArrayList<Event> getArrayListFromString(String arrayString){
        TypeToken<ArrayList<Event>> token = new TypeToken<ArrayList<Event>>() {};
        Gson gson = new Gson();
        return gson.fromJson(arrayString,token.getType());
    }


    @TypeConverter
    public static String getStringFromArray (ArrayList<Event> eventArrayList){
        Gson gson = new Gson();
        return gson.toJson(eventArrayList);
    }

    @TypeConverter
    public static String getUserString (User user){
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    @TypeConverter
    public static User getUserFromString (String json){
        Gson gson = new Gson();
        return gson.fromJson(json,User.class);
    }

    @TypeConverter
    public static String getEventString (Event event){
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    @TypeConverter
    public static Event getEventFromString (String json){
        Gson gson = new Gson();
        return gson.fromJson(json,Event.class);
    }



}