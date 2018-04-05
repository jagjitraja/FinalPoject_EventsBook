package eventsbook.t00533766.eventsbook.Activites_Fragments.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eventsbook.t00533766.eventsbook.Activites_Fragments.OnEventItemClick;
import eventsbook.t00533766.eventsbook.EventData.Event;
import eventsbook.t00533766.eventsbook.R;

import static eventsbook.t00533766.eventsbook.Activites_Fragments.MainActivity.ADD_EVENT_REQUEST;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_ADD_SUCCESS;
import static eventsbook.t00533766.eventsbook.Utilities.Utils.EVENT_DATA;


public class EvenListFragment extends Fragment implements OnEventItemClick {




    private ArrayList<Event> eventArrayList;
    private ListFragmentListener fragmentInteractionListener;

    private final String TAG = EvenListFragment.class.getSimpleName();

    public EvenListFragment() {
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
        return inflater.inflate(R.layout.fragment_even_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (fragmentInteractionListener != null) {
//            fragmentInteractionListener.ViewFragmentEvent(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragmentListener) {
            fragmentInteractionListener = (ListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddEventFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: "+requestCode+"        "+resultCode);
        switch (requestCode){
            case ADD_EVENT_REQUEST:
                if (resultCode==EVENT_ADD_SUCCESS){
                    Log.d(TAG, "onActivityResult: ");
                    Event event = (Event) data.getSerializableExtra(EVENT_DATA);
                    eventArrayList.add(event);
                    fragmentInteractionListener.onEventAdded(event);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void eventItemClicked(String clickedButton, Event event) {

    }

    public interface ListFragmentListener {
        // TODO: Update argument type and name
        void onEventAdded(Event event);
    }
}
