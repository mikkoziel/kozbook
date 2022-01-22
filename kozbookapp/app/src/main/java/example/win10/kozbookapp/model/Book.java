package example.win10.kozbookapp.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private int book_id;
    private String name;
    private Author author;
    private List<Location> locations;

    public Book() {
    }

    public Book(String name, Author author) {
        this.name = name;
        this.author = author;
    }

    public Book(int book_id, String name, Author author) {
        this.book_id = book_id;
        this.name = name;
        this.author = author;
        this.locations = new ArrayList<>();
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void addLocation(Location location){
        this.locations.add(location);
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location getActiveLocation() {
        if(locations.size()==0){
            return null;
        }
        return locations.get(locations.size()-1);
    }
}
