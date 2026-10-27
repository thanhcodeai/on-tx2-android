package com.example.ontx22;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private final List<Book> books = new ArrayList<>();
    private final OnBookClickListener listener;

    public BookAdapter(OnBookClickListener listener) {
        this.listener = listener;
    }

    public void setBooks(List<Book> newBooks) {
        books.clear();
        if (newBooks != null) books.addAll(newBooks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvBookCode.setText(holder.itemView.getContext()
                .getString(R.string.book_code_value, book.bookCode));
        holder.tvBookName.setText(book.bookName);
        holder.tvPublicationDate.setText(holder.itemView.getContext()
                .getString(R.string.publication_date_value, book.publicationDate));
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onBookClick(books.get(currentPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
