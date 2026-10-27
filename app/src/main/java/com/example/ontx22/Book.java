package com.example.ontx22;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey
    @NonNull
    public String bookCode;
    public String bookName;
    public String publicationDate;

    public Book(@NonNull String bookCode, String bookName, String publicationDate) {
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.publicationDate = publicationDate;
    }
}
