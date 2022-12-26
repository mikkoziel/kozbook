package example.win10.kozbookapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.utils.BookLayout
import java.util.function.Predicate
import java.util.stream.Collectors

class BookListViewModel {
    val chosenBook = MutableLiveData<BookLayout>()
    private val showBooks = MutableLiveData<List<Book>>()
    val chosenBookValue: BookLayout?
        get() = chosenBook.value

    fun postChosenBook(bookLayout: BookLayout?) {
        chosenBook.postValue(bookLayout)
    }

    val showBooksValue: List<Book>
        get() = showBooks.value!!

    fun postShowBooks(books: List<Book>) {
        showBooks.postValue(books)
    }

    fun searchLibrary(searchString: String?, booksToSearch: List<Book>): List<Book> {
        val byName = Predicate { book: Book -> book.name?.lowercase()?.contains(searchString!!) ?: false }
        val byAuthor = Predicate { book: Book -> book.author?.name?.lowercase()?.contains(searchString!!) ?: false }
        return filterLibrary(byName.or(byAuthor), booksToSearch)
    }

    fun filterLibrary(predicate: Predicate<Book>?, books: List<Book>): List<Book> {
        return books.stream().filter(predicate).collect(Collectors.toList())
    }

    fun toggleVisibility(view: View) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}