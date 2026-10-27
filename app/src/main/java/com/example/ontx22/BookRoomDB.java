package com.example.ontx22;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookRoomDB extends RoomDatabase {
    private static volatile BookRoomDB instance;
    public static final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    public abstract BookDAO bookDAO();

    public static BookRoomDB getInstance(Context context) {
        if (instance == null) {
            synchronized (BookRoomDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            BookRoomDB.class, "book_database").build();
                }
            }
        }
        return instance;
    }
}
