package example.win10.kozbookapp.model.utils;

import android.content.Context;
import android.widget.LinearLayout;

import example.win10.kozbookapp.model.Book;

public class BookLayout extends LinearLayout{
    private Book book;

    public BookLayout(Context context, Book book) {
        super(context);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
