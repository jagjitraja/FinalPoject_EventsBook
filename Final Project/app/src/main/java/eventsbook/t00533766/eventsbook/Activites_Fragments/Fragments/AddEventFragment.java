package eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.Utils;


public class AddEventFragment extends Fragment {

    private AddEventFragmentListener eventFragmentListener;

    private final String TAG = AddEventFragment.class.getSimpleName();
    private Event event;

    private User user;
    private EditText eventNameEditText;
    private TextView eventDateTextView;
    private EditText eventPriceEditText;
    private EditText eventAddressEditText;
    private EditText eventDescriptionEditText;
    private Button postEventButton;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if(intent.getSerializableExtra(Utils.EDIT_EVENT_INTENT_KEY)!=null) {
            event = (Event) intent.getSerializableExtra(Utils.EDIT_EVENT_INTENT_KEY);
        }
        user = (User) intent.getSerializableExtra(Utils.FIRE_BASE_USER_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeLayout();
    }

    private void intializeLayout() {

        eventNameEditText = getActivity().findViewById(R.id.event_name_text_view);
        eventDateTextView = getActivity().findViewById(R.id.event_date_val);
        eventPriceEditText = getActivity().findViewById(R.id.event_price__text_view);
        eventAddressEditText = getActivity().findViewById(R.id.address__text_view);
        eventDescriptionEditText = getActivity().findViewById(R.id.event_description_text_view);
        postEventButton = getActivity().findViewById(R.id.post_event_button);

        Log.d(TAG, "intializeLayout: "+event);
        if (event!=null){
            eventNameEditText.setText(event.getEventName());
            eventDescriptionEditText.setText(event.getDescription());
            eventDateTextView.setText(event.getStringDate());
            eventPriceEditText.setText(event.getEventPrice()+"");
            eventAddressEditText.setText(event.getAddressLocation());
        }
        postEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEvent();
            }
        });
    }


    public void setUser(User user) {
        this.user = user;
    }
    public void setEvent(Event event) {
        Log.d(TAG, "setEvent: ");
        this.event = event;
    }

    public void setEventFragmentListener(AddEventFragmentListener eventFragmentListener) {
        this.eventFragmentListener = eventFragmentListener;
    }


    public interface AddEventFragmentListener {
        void PostEvent(Event event);
    }

    public void postEvent() {

        String eventName = eventNameEditText.getText().toString();
        String eventDate = eventDateTextView.getText().toString();
        String eventPrice = eventPriceEditText.getText().toString();
        String eventAddress = eventAddressEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();
        Event event = new Event("",eventName,eventDescription,new Date()
                ,user,0.00,eventAddress);

        Log.d(TAG, "postEvent: "+event.toString());

        eventFragmentListener.PostEvent(event);
    }
}
