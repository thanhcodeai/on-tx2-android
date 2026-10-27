package com.example.ontx22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout tilBookCode, tilBookName, tilPublicationDate;
    private TextInputEditText tieBookCode, tieBookName, tiePublicationDate;
    private RecyclerView rcvBooks;
    private BookAdapter bookAdapter;
    private BookDAO bookDAO;
    private LiveData<List<Book>> books;
    private String selectedBookCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        mapping();
        if (savedInstanceState != null) {
            selectedBookCode = savedInstanceState.getString("selectedBookCode");
        }
        bookDAO = BookRoomDB.getInstance(this).bookDAO();
        bookAdapter = new BookAdapter(this::showBook);
        rcvBooks.setLayoutManager(new LinearLayoutManager(this));
        rcvBooks.setAdapter(bookAdapter);
        tiePublicationDate.setKeyListener(null);
        tiePublicationDate.setOnClickListener(v -> showDatePickerDialog());
        findViewById(R.id.mbtn_add).setOnClickListener(v -> addBook());
        findViewById(R.id.mbtn_read).setOnClickListener(v -> readBooks());
        findViewById(R.id.mbtn_delete).setOnClickListener(v -> deleteBook());
        findViewById(R.id.mbtn_share).setOnClickListener(v -> shareBook());
        readBooks();
    }

    private void mapping() {
        tilBookCode = findViewById(R.id.til_book_code);
        tilBookName = findViewById(R.id.til_book_name);
        tilPublicationDate = findViewById(R.id.til_publication_date);
        tieBookCode = findViewById(R.id.tie_book_code);
        tieBookName = findViewById(R.id.tie_book_name);
        tiePublicationDate = findViewById(R.id.tie_publication_date);
        rcvBooks = findViewById(R.id.rcv_books);
    }

    private String text(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private Book getBook() {
        return new Book(text(tieBookCode), text(tieBookName), text(tiePublicationDate));
    }

    private boolean validateData(boolean requireCode) {
        boolean valid = true;
        tilBookCode.setError(null);
        tilBookName.setError(null);
        tilPublicationDate.setError(null);
        if (requireCode && text(tieBookCode).isEmpty()) {
            tilBookCode.setError(getString(R.string.required_book_code));
            valid = false;
        }
        if (text(tieBookName).isEmpty()) {
            tilBookName.setError(getString(R.string.required_book_name));
            valid = false;
        }
        if (text(tiePublicationDate).isEmpty()) {
            tilPublicationDate.setError(getString(R.string.required_publication_date));
            valid = false;
        }
        return valid;
    }

    private void showBook(Book book) {
        selectedBookCode = book.bookCode;
        tieBookCode.setText(book.bookCode);
        tieBookName.setText(book.bookName);
        tiePublicationDate.setText(book.publicationDate);
        tilBookCode.setError(null);
        tilBookName.setError(null);
        tilPublicationDate.setError(null);
    }

    private void showDatePickerDialog() {
        if (getSupportFragmentManager().findFragmentByTag("DATE_PICKER") != null) return;
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.title_pick_publication_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            tiePublicationDate.setText(sdf.format(new Date(selection)));
            tilPublicationDate.setErrorEnabled(false);
        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void readBooks() {
        // Reattach the query for the READ button, keeping a single active observer.
        if (books != null) books.removeObservers(this);
        books = bookDAO.getAllBooks();
        books.observe(this, result -> {
            bookAdapter.setBooks(result);
            if (selectedBookCode != null) {
                boolean found = false;
                for (Book book : result) {
                    if (selectedBookCode.equals(book.bookCode)) {
                        found = true;
                        break;
                    }
                }
                if (!found) selectedBookCode = null;
            }
        });
    }

    private void addBook() {
        if (!validateData(true)) return;
        Book book = getBook();
        BookRoomDB.databaseExecutor.execute(() -> {
            try {
                long result = bookDAO.insert(book);
                runOnUiThread(() -> {
                    if (result == -1) {
                        tilBookCode.setError(getString(R.string.duplicate_book_code));
                    } else {
                        toast(R.string.added);
                    }
                });
            } catch (RuntimeException e) {
                runOnUiThread(() -> toast(R.string.database_error));
            }
        });
    }

    private void deleteBook() {
        if (selectedBookCode == null) {
            toast(R.string.select_book);
            return;
        }
        // Delete the selected database key, even if the form code was edited.
        String codeToDelete = selectedBookCode;
        BookRoomDB.databaseExecutor.execute(() -> {
            try {
                int count = bookDAO.deleteByCode(codeToDelete);
                runOnUiThread(() -> {
                    if (codeToDelete.equals(selectedBookCode) || selectedBookCode == null) {
                        selectedBookCode = null;
                        tieBookCode.setText("");
                        tieBookName.setText("");
                        tiePublicationDate.setText("");
                    }
                    toast(count > 0 ? R.string.deleted : R.string.book_not_found);
                });
            } catch (RuntimeException e) {
                runOnUiThread(() -> toast(R.string.database_error));
            }
        });
    }

    private void shareBook() {
        if (!validateData(false)) return;
        Book book = getBook();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_content, book.bookName, book.publicationDate));
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    private void toast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("selectedBookCode", selectedBookCode);
        super.onSaveInstanceState(outState);
    }
}
