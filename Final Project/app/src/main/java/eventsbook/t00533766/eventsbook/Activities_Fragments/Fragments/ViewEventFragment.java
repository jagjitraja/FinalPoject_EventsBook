package eventsbook.t00533766.eventsbook.Activities_Fragments.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.EventData.User;
import eventsbook.t00533766.eventsbook.R;
import eventsbook.t00533766.eventsbook.Utilities.LoggedInUserSingleton;

public class ViewEventFragment extends Fragment {


    private final String TAG = ViewEventFragment.class.getSimpleName();

    private ViewEventFragmentListener mListener;
    private TextView eventNameTextView;
    private TextView eventDescriptionTextView;
    private TextView eventDateTextView;
    private TextView eventCostTextView;
    private TextView eventLocationTextView;
    private TextView attendingUsersTextView;
    private TextView interestedUsersTextView;
    private Button editEventButton;
    private Button shareEventButton;
    private Button deleteEventButton;
    private ImageButton myLocationButton;
    private ImageView eventImage;
    private Event event;
    private User loggedInUser;
    private ViewEventFragmentListener eventFragmentListener;

    public ViewEventFragment() {
        // Required empty public constructor
    }

    public void setEventFragmentListener(ViewEventFragmentListener viewEventFragmentListener){
        this.eventFragmentListener = viewEventFragmentListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        loggedInUser = LoggedInUserSingleton.getLoggedInUser();
        return inflater.inflate(R.layout.fragment_view_event,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intializeLayoutItems();
    }

    public void setEvent(Event event){
        this.event = event;
    }

    public void intializeLayoutItems() {

        eventNameTextView = getActivity().findViewById(R.id.event_name_text_view);
        eventDescriptionTextView = getActivity().findViewById(R.id.event_description_text_view);
        eventDateTextView = getActivity().findViewById(R.id.event_date__text_view);
        eventCostTextView = getActivity().findViewById(R.id.event_price__text_view);
        eventLocationTextView = getActivity().findViewById(R.id.address_edit_text);
        editEventButton = getActivity().findViewById(R.id.edit_event_button);
        myLocationButton = getActivity().findViewById(R.id.my_location_button);
        interestedUsersTextView = getActivity().findViewById(R.id.interestedUsersValue);
        attendingUsersTextView = getActivity().findViewById(R.id.attendingUsersValue);
        eventImage = getActivity().findViewById(R.id.event_imageView);
        shareEventButton = getActivity().findViewById(R.id.share_event_button);
        deleteEventButton = getActivity().findViewById(R.id.delete_button);


        if (!loggedInUser.getUserID().equals(event.getPostedBy().getUserID())){
            editEventButton.setEnabled(false);
            editEventButton.setVisibility(View.INVISIBLE);
            deleteEventButton.setEnabled(false);
            deleteEventButton.setVisibility(View.INVISIBLE);
        }

        eventNameTextView.setText(event.getEventName());
        eventDescriptionTextView.setText(event.getDescription());
        eventDateTextView.setText(event.getEventDate());
        eventCostTextView.setText(event.getEventPrice()+"");
        eventLocationTextView.setText(event.getAddressLocation());
        attendingUsersTextView.setText(event.getAttendingUsersCount()+"");
        interestedUsersTextView.setText(event.getInterestedUsersCount()+"");

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventFragmentListener.deleteEventClicked(event);
            }
        });
        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventFragmentListener.editEventClicked(event);
            }
        });
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventFragmentListener.showInMapClicked(event);
            }
        });
        shareEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventFragmentListener.shareEventClicked(event);
            }
        });

        Glide.with(getContext())
                .load(event.getStorageURL())
                .into(eventImage);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ViewEventFragmentListener {
        void editEventClicked(Event event);
        void showInMapClicked(Event event);
        void shareEventClicked(Event event);
        void deleteEventClicked(Event event);
    }
}
