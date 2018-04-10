package eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments;

import android.app.DatePickerDialog;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
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
    private ImageButton myLocationButton;

    private final int ADDING = 1;
    private final int EDITING = 2;

    private Date selectedDate = new Date(System.currentTimeMillis());

    private int EVENT_TASK = ADDING;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        eventNameEditText = getActivity().findViewById(R.id.event_name_text_view);
        eventDateTextView = getActivity().findViewById(R.id.event_date_val);
        eventDateTextView.setOnClickListener(dateOnClickListener);
        eventPriceEditText = getActivity().findViewById(R.id.event_price__text_view);
        eventAddressEditText = getActivity().findViewById(R.id.address_edit_text);
        eventDescriptionEditText = getActivity().findViewById(R.id.event_description_text_view);
        postEventButton = getActivity().findViewById(R.id.post_event_button);
        myLocationButton = getActivity().findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(locationOnClickListener);

        intializeLayout();
    }

    private void intializeLayout() {

        if (event != null) {
            eventNameEditText.setText(event.getEventName());
            eventDescriptionEditText.setText(event.getDescription());
            eventDateTextView.setText(event.getEventDate());
            eventPriceEditText.setText(event.getEventPrice() + "");
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
        Log.d(TAG, "setUser: ===============" + this.user + "=----------------------" + user);
    }

    public void setEvent(Event event) {
        this.event = event;
        EVENT_TASK = EDITING;
    }

    public void setEventFragmentListener(AddEventFragmentListener eventFragmentListener) {
        this.eventFragmentListener = eventFragmentListener;
    }

    public void updateLocationEditText(Address address) {
        eventAddressEditText = getActivity().findViewById(R.id.address_edit_text);
        if (eventAddressEditText!=null){
            eventAddressEditText.setText(address.getAddressLine(0));
        }
    }


    public interface AddEventFragmentListener {
        void PostEvent(Event event);
        void UpdateEvent(Event event);
        String getLocationClicked();
    }

    public void postEvent() {

        String eventName = eventNameEditText.getText().toString();
        String eventDate = eventDateTextView.getText().toString();
        String eventPrice = eventPriceEditText.getText().toString();
        String eventAddress = eventAddressEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();

        if (TextUtils.isEmpty(eventName)||TextUtils.isEmpty(eventDate)||TextUtils.isEmpty(eventPrice)
                ||TextUtils.isEmpty(eventAddress)||TextUtils.isEmpty(eventDescription)){

            Utils.showToast(getContext(),"Please fill out all fields");
            return;
        }


        Event newEvent = new Event("", eventName, eventDescription, Utils.dateFormat.format(new Date())
                , user, Double.parseDouble(eventPrice), eventAddress);

        newEvent.setEventDate(eventDate);

        if (event != null)
            newEvent.setEventID(event.getEventID());

        Log.d(TAG, user + "\n\n\npostEvent: ******************************************" + newEvent);
        if (EVENT_TASK == ADDING) {
            eventFragmentListener.PostEvent(newEvent);
        } else if (EVENT_TASK == EDITING) {
            eventFragmentListener.UpdateEvent(newEvent);
        }

    }

    public void setDate(Date date) {
        this.selectedDate = date;
    }

    private View.OnClickListener dateOnClickListener = new View.OnClickListener() {

        private Date date = new Date(System.currentTimeMillis());

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.show();
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(i, i1, i2);
                    Date date = calendar.getTime();
                    eventDateTextView.setText(Utils.dateFormat.format(date));
                    setDate(date);
                }
            });
        }
    };
    private View.OnClickListener locationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String address = eventFragmentListener.getLocationClicked();
            if(address!=null){
                Log.d(TAG, "onClick: "+address);
                eventAddressEditText.setText(address);
            }else {
                Utils.showToast(getContext(),"Failed to obtain location :(");
            }
        }
    };

}


//TODO: ADDRESS SEARCH WHEN ENTERING ADDRESS
