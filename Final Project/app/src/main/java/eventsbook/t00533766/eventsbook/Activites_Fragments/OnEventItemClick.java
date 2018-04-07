package eventsbook.t00533766.eventsbook.Activites_Fragments;

import eventsbook.t00533766.eventsbook.EventData.Event;

public interface OnEventItemClick {

    public void eventInterestedClicked(Event event,boolean interested);
    public void eventRegisterClicked(Event event, boolean registering);
    public void viewEventSelected(Event event);
}
