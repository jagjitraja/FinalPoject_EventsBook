//package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import java.util.List;
//
//import eventsbook.t00533766.eventsbook.EventData.Event;
//
//public class EventViewModel extends AndroidViewModel {
//
//    private final String TAG = EventViewModel.class.getSimpleName();
//    private EventDatabaseRepository eventDatabaseRepository;
//
//    public EventViewModel(@NonNull Application application) {
//        super(application);
//        eventDatabaseRepository = new EventDatabaseRepository(application.getApplicationContext());
//    }
//
//    public LiveData<List<Event>> getEventListLiveData(){
//        Log.delete(TAG, "getEventListLiveData: ");
//        return eventDatabaseRepository.getEventListLiveData();
//    }
//
//    public void insertEventData(Event event){
//        eventDatabaseRepository.insertEvent(event);
//    }
//}
