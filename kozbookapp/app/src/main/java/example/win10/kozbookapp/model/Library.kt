package example.win10.kozbookapp.model

import java.util.function.Supplier
import java.util.stream.Collectors

class Library {
    private var libraryId = 0
    var name: String
    private var books: MutableList<Book>? = null
    private var authors: MutableList<Author>? = null
    private var locations: MutableList<Location>? = null
    private var accessBooks: MutableList<AccessBook>? = null

    constructor(libraryId: Int, name: String, books: MutableList<Book>?, authors: MutableList<Author>?, locations: MutableList<Location>?) {
        this.libraryId = libraryId
        this.name = name
        this.books = books
        this.authors = authors
        this.locations = locations
        accessBooks = java.util.ArrayList()
    }

    constructor(name: String) {
        this.name = name
    }

    fun getBooks(): List<Book>? {
        return books
    }

    fun setBooks(books: MutableList<Book>?) {
        this.books = books
    }

    fun getAuthors(): List<Author>? {
        return authors
    }

    fun setAuthors(authors: MutableList<Author>?) {
        this.authors = authors
    }

    fun getLocations(): List<Location>? {
        return locations
    }

    fun setLocations(locations: MutableList<Location>?) {
        this.locations = locations
    }

    fun removeBookFromLibrary(book: Book) {
        books!!.removeIf { x: Book -> x === book }
    }

    fun removeAuthorFromLibrary(author: Author) {
        authors!!.removeIf { x: Author -> x === author }
    }

    fun removeLocationFromLibrary(loc: Location) {
        locations!!.removeIf { x: Location -> x == loc }
    }

    fun getBooksForAuthor(author: Author): java.util.ArrayList<Book> {
        return books!!.stream().filter { book: Book -> author.name?.let { book.author?.name?.contains(it) }
                ?: false }.collect(Collectors.toCollection { ArrayList() })
    }

    fun getBooksForLocation(loc: Location): java.util.ArrayList<Book> {
        return books!!.stream().filter { book: Book -> book.activeLocation == loc }.collect(Collectors.toCollection(Supplier { ArrayList() }))
    }

    fun getAccessBooks(): List<AccessBook>? {
        return accessBooks
    }

    fun setAccessBooks(accessBooks: MutableList<AccessBook>?) {
        this.accessBooks = accessBooks
    }

    fun addAccessBook(accessBook: AccessBook) {
        accessBooks!!.add(accessBook)
    }

    fun getAccessBookForUser(user: User): java.util.ArrayList<AccessBook> {
        return accessBooks!!.stream().filter { ab: AccessBook -> ab.user == user }.collect(Collectors.toCollection(Supplier { ArrayList() }))
    }
}