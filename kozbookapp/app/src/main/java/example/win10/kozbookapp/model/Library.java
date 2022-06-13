package example.win10.kozbookapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private int library_id;
    private String name;
    private List<Book> books;
    private List<Author> authors;
    private List<Location> locations;
    private List<AccessBook> accessBooks;

    public Library(int library_id, String name, List<Book> books, List<Author> authors, List<Location> locations) {
        this.library_id = library_id;
        this.name = name;
        this.books = books;
        this.authors = authors;
        this.locations = locations;
        this.accessBooks = new ArrayList<>();
    }

    public Library(String name) {
        this.name = name;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(int library_id) {
        this.library_id = library_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void removeBookFromLibrary(Book book){
        this.books.removeIf(x -> x == book);
    }

    public void removeAuthorFromLibrary(Author author){
        this.authors.removeIf(x -> x == author);
    }

    public void removeLocationFromLibrary(Location loc){
        this.locations.removeIf(x -> x == loc);
    }

    public ArrayList<Book> getBooksForAuthor(Author author){
        return this.books.stream().filter(book -> book.getAuthor().getName().contains(author.getName())).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Book> getBooksForLocation(Location loc){
        return this.books.stream().filter(book -> book.getActiveLocation().equals(loc)).collect(Collectors.toCollection(ArrayList::new));
    }


    public List<AccessBook> getAccessBooks() {
        return accessBooks;
    }

    public void setAccessBooks(List<AccessBook> accessBooks) {
        this.accessBooks = accessBooks;
    }

    public void addAccessBook(AccessBook accessBook){
        this.accessBooks.add(accessBook);
    }

    public ArrayList<AccessBook> getAccessBookForUser(User user){
        return this.accessBooks.stream().filter(ab -> ab.getUser().equals(user)).collect(Collectors.toCollection(ArrayList::new));
    }
}
