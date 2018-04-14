package eventsbook.t00533766.eventsbook.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by T00533766 on 3/16/2018.
 */

public class Utils {
    public static final String EVENT_IMAGES_STORAGE = "gs://eventify-jsb.appspot.com";
    public static final String EVENT_NODE = "EVENTS";

    public static final String VIEW_EVENT_INTENT_KEY = "VIEW EVENT";
    public final static String FIRE_BASE_USER_KEY = "FIREBASE_USER";

    public static final String ADD_INTENT_ACTION = "ADD_INTENT_ACTION";
    public static final String VIEW_INTENT_ACTION = "VIEW_INTENT_ACTION";
    public static final String EDIT_INTENT_ACTION = "EDIT_INTENT_ACTION";

    public static final String EVENT_DATA = "EVENT ADDED";

    public static final String NEW_EVENT_NOTIFICATION_ACTION = "NEW EVENT ACTION";

    public static final int LOCATION_REQUEST_CODE = 40;
    public static final String USER_LOCATION_LATITUDE = "USER LOCATION LATTITUDE";
    public static final String USER_LOCATION_LONGITUDE = "USER LOCATION LONGITUDE";
    public static final String EVENT_LOCATION_LATITUDE = "EVENT LOCATION LATTITUDE";
    public static final String EVENT_LOCATION_LONGITUDE = "EVENT LOCATION LONGITUDE";

    public static final int IMAGE_CAPTURE_REQUEST_CODE = 11;
    public static final String EVENTS_IMAGES = "EVENT_IMAGES";


    public static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");

    private static final String TAG = Utils.class.getName();
    public static String ADD_FRAGMENT = "ADD_FRAGMENT";
    public static String VIEW_FRAGMENT = "VIEW_FRAGMENT";


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

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();;
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }




}
