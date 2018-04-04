//package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.MutableLiveData;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.logging.Handler;
//
//import eventsbook.t00533766.eventsbook.EventData.Event;
//
//public class EventDatabaseRepository {
//
//    private final String TAG  = EventDatabaseRepository.class.getSimpleName();
//    private LiveData<List<Event>> listLiveData;
//    private EventDao eventDao;
//
//    public EventDatabaseRepository(Context context){
//        this.eventDao = EventRoomDatabase.getEventRoomDatabaseInstance(context).getEventDao();
//    }
//
//    public LiveData<List<Event>> getEventListLiveData() {
//        Log.d(TAG, "getEventListLiveData: --------------------");
//        if (listLiveData==null){
//            try {
//                listLiveData = new SelectAsyncTask().execute().get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//        return listLiveData;
//    }
//
//    public void insertEvent(final Event event) {
//
//        InsertAsyncTask insertAsyncTask = new InsertAsyncTask();
//        insertAsyncTask.execute(event);
//
//    }
//
//
//    public LiveData<List<Event>> getAllEvents() {
//        return null;
//    }
//
//    private class InsertAsyncTask extends AsyncTask<Event,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Event... events) {
//            if (events[0]!=null)
//                eventDao.InsertEvent(events[0]);
//            return null;
//        }
//    }
//    private class SelectAsyncTask extends AsyncTask<Void,Void,LiveData<List<Event>>>{
//
//
//        @Override
//        protected LiveData<List<Event>> doInBackground(Void... voids) {
//            return eventDao.GetAllEvents();
//        }
//    }
//}
