package eventsbook.t00533766.eventsbook.Activites_Fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;

public class ViewEventActivity extends AppCompatActivity {

    private final String TAG = ViewEventActivity.class.getSimpleName();

    private TextView eventNameTextView;
    private TextView eventDescriptionTextView;
    private TextView eventDateTextView;
    private TextView eventCostTextView;
    private TextView eventLocationTextView;

    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Intent intent = getIntent();
        this.event = (Event) intent.getSerializableExtra(Utils.VIEW_EVENT_INTENT_KEY);

        intializeLayoutItems();


    }

    private void intializeLayoutItems() {
        eventNameTextView = findViewById(R.id.event_name_text_view);
        eventDescriptionTextView = findViewById(R.id.event_description_text_view);
        eventDateTextView = findViewById(R.id.event_date__text_view);
        eventCostTextView = findViewById(R.id.event_price__text_view);
        eventLocationTextView = findViewById(R.id.address__text_view);

        eventNameTextView.setText(event.getEventName());
        eventDescriptionTextView.setText(event.getDescription());
        eventDateTextView.setText(event.getStringDate());
        eventCostTextView.setText(event.getEventPrice()+"");
        eventLocationTextView.setText(event.getAddressLocation());
    }
`
    public void editEvent(View view) {
        Intent intent = new Intent(this,AddEventActivity.class);
        intent.putExtra(Utils.EDIT_EVENT_INTENT_KEY,intent);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        if (name==null){
            name = "Not Available";
        }
        User user = new User(firebaseAuth.getUid(), name
                ,firebaseAuth.getCurrentUser().getEmail());
       // intent.putExtra(Utils.FIRE_BASE_USER_KEY,user);
        Utils.goToActivity(intent,0,getApplicationContext());


    }
}
