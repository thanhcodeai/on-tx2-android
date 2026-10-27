package com.example.ontx22;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder {
    final TextView tvBookCode;
    final TextView tvBookName;
    final TextView tvPublicationDate;

    public BookViewHolder(View itemView) {
        super(itemView);
        tvBookCode = itemView.findViewById(R.id.tv_book_code);
        tvBookName = itemView.findViewById(R.id.tv_book_name);
        tvPublicationDate = itemView.findViewById(R.id.tv_publication_date);
    }
}
