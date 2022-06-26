package example.win10.kozbookapp.viewmodel;

import android.view.View;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.utils.BookLayout;

public class BookListViewModel {

    private final MutableLiveData<BookLayout> chosenBook = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> showBooks = new MutableLiveData<>();

    public BookListViewModel(){

    }

    public MutableLiveData<BookLayout> getChosenBook() { return chosenBook; }
    public BookLayout getChosenBookValue() { return chosenBook.getValue(); }
    public void postChosenBook(BookLayout bookLayout) { this.chosenBook.postValue(bookLayout);}

    public MutableLiveData<List<Book>> getShowBooks() { return showBooks; }
    public List<Book> getShowBooksValue() { return showBooks.getValue(); }
    public void postShowBooks(List<Book> books) { showBooks.postValue(books); }

    public List<Book> searchLibrary(String searchString, List<Book> booksToSearch){
        Predicate<Book> byName = book -> book.getName().toLowerCase(Locale.ROOT).contains(searchString);
        Predicate<Book> byAuthor = book -> book.getAuthor().getName().toLowerCase(Locale.ROOT).contains(searchString);

        return filterLibrary(byName.or(byAuthor), booksToSearch);

    }

    public List<Book> filterLibrary(Predicate<Book> predicate, List<Book> books) {
        return books.stream().filter(predicate).collect(Collectors.toList());
    }

    public void toggleVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
