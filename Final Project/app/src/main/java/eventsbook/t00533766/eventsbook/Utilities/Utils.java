package eventsbook.t00533766.eventsbook.Utilities;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Type;
import java.util.ArrayList;

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


    public static void showSnackBar(View v, String error, String actiontext, View.OnClickListener clickListener) {
        Log.d(TAG, "showSnackBar: ");
        Snackbar snackbar = Snackbar.make(v, error, Snackbar.LENGTH_SHORT);
        if (clickListener == null||actiontext==null) {
            snackbar.show();
        } else {
            snackbar.setAction(actiontext, clickListener).show();
        }
    }

    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
