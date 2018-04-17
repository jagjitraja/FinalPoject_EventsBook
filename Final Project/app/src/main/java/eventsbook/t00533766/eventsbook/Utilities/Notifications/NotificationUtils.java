package eventsbook.t00533766.eventsbook.Utilities.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import eventsbook.t00533766.eventsbook.Activities_Fragments.Activities.MainActivity;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;


public class NotificationUtils {
    private final static String NOTIFICATION_CHANNEL_ID = "eventsbook.t00533766.eventsbook";
    private final static String NOTIFICATION_CHANNEL_NAME ="EVENT NOTIFICATION";
    private final static String NOTIFICATION_CHANNEL_DESCRIPTION = "NEW EVENT";
    private static final int NOTIFICATION_ID = 1;
    private static final int INTENT_ACTIVITY_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotification(Context context, Event event, User user){

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int NOTIFICATION_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NOTIFICATION_IMPORTANCE);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setColor(Color.BLUE)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(event.getEventName())
                .setContentText(event.getDescription())
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setContentIntent(getContentIntent(context,event))
                .addAction(viewNotificationEvent(context,event,user))
                .addAction(ignoreAction(context));

        Notification notification = builder.build();
        manager.notify(NOTIFICATION_ID,notification);
    }

    private static NotificationCompat.Action ignoreAction(Context context) {

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(context,INTENT_ACTIVITY_ID,
                intent,PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(R.drawable.ic_exit_to_app_black_24dp,"CANCEL",pendingIntent);
    }

    private static NotificationCompat.Action viewNotificationEvent(Context context,Event event,User user) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(Utils.NEW_EVENT_NOTIFICATION_ACTION);
        intent.putExtra(Utils.VIEW_EVENT_INTENT_KEY,event);
        intent.putExtra(Utils.FIRE_BASE_USER_KEY,user);
        PendingIntent pendingIntent = PendingIntent.getService(context,INTENT_ACTIVITY_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(R.drawable.ic_add_circle_outline_black_24dp,"VIEW EVENT",pendingIntent);
    }

    private static PendingIntent getContentIntent(Context context,Event event) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Utils.VIEW_EVENT_INTENT_KEY,event);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,INTENT_ACTIVITY_ID,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


}
