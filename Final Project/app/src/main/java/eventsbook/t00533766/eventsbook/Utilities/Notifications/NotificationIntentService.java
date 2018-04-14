package eventsbook.t00533766.eventsbook.Utilities.Notifications;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import eventsbook.t00533766.eventsbook.Activities_Fragments.EventDetailActivity;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

import static eventsbook.t00533766.eventsbook.Utilities.Utils.EDIT_INTENT_ACTION;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.INTENT_FRAGMENT_CODE;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.VIEW_FRAGMENT_CODE;

/**
 * Created by T00533766 on 4/11/2018.
 */

public class NotificationIntentService extends IntentService{

        private final String TAG = "SERVICE -=-=-=-=-=-=-";

        public NotificationIntentService(){
            super(NotificationIntentService.class.getSimpleName());

        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {

            String action = intent.getAction();
            if (action.equals(Utils.NEW_EVENT_NOTIFICATION_ACTION)){
                intent.setAction(EDIT_INTENT_ACTION);
                intent.putExtra(INTENT_FRAGMENT_CODE, VIEW_FRAGMENT_CODE);
                intent.setClass(getApplicationContext(), EventDetailActivity.class);
                startActivity(intent);
            }

        }

}
