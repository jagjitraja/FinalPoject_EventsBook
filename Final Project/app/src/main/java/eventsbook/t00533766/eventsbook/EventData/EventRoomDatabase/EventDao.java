//package eventsbook.t00533766.eventsbook.EventData.EventRoomDatabase;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;
//
//import java.util.List;
//
//import eventsbook.t00533766.eventsbook.EventData.Event;
//import eventsbook.t00533766.eventsbook.EventData.User;
//
//@Dao
//public interface EventDao {
//
//    @Insert
//    public void InsertEvent(Event event);
//
//    @Update
//    public void UpdateEvent(Event event);
//
//    @Delete
//    public void DeleteEvent(Event event);
//
//    @Query("SELECT * FROM Events")
//    public LiveData<List<Event>> GetAllEvents();
//
//    //TODO: GET ALL USER EVENTS
//
//}
