package eventsbook.t00533766.eventsbook.Activities_Fragments;

import eventsbook.t00533766.eventsbook.EventData.Event;

//CALL BACK TO GO TO MAIN ACTIVITY INCASE USER INTERACTS WITH AN ITEM IN A RECYCLER VIEW
public interface OnEventItemClick {

    public void eventInterestedClicked(Event event);
    public void eventRegisterClicked(Event event);
    public void viewEventSelected(Event event);
}
