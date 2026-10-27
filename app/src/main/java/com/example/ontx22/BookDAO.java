package com.example.ontx22;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookDAO {
    @Query("SELECT * FROM books ORDER BY bookCode")
    LiveData<List<Book>> getAllBooks();

    // IGNORE returns -1 for a duplicate code without replacing the existing book.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Book book);

    @Query("DELETE FROM books WHERE bookCode = :bookCode")
    int deleteByCode(String bookCode);
}
