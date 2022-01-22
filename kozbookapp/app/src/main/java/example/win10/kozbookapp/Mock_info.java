package example.win10.kozbookapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import example.win10.kozbookapp.model.AccessBook;
import example.win10.kozbookapp.model.Author;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.model.User;

public class Mock_info {
    public Library library;
    public User user;

    public Mock_info(){
        this.setLibrary();
    }

    public void setLibrary(){

        Author author1 = new Author(1, "Henryk Sienkiewicz");
        Author author2 = new Author(2, "Jules Verne");
        Author author3 = new Author(3, "");

        List<Author> authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);

        Location l1 = new Location(1, "Kraków", "Mikołaja Pokój");
        Location l2 = new Location(1, "Kraków", "Michaliny Pokój");
        Location l3 = new Location(1, "Sypialnia rodziców", "Nad gramofonem");
        Location l4 = new Location(1, "Sypialnia rodziców", "Nad telewizorem");

        List<Location> locations = new ArrayList<>();
        locations.add(l1);
        locations.add(l2);
        locations.add(l3);
        locations.add(l4);

        Book b1 = new Book(1, "Potop", author1);
        Book b2 = new Book(2,"Ogniem i mieczem", author1);
        Book b3 = new Book(3, "Pan Wołodyjowski", author1);
        Book b4 = new Book(4, "20000 tys mil podmorskiej żeglugi", author2);
        Book b5 = new Book(5, "Wyprawa na Księzyc", author2);
        Book b6 = new Book(6, "W 80 dni dookoła świata", author2);
        Book b7 = new Book(7, "Kamasutra", author3);
        Book b8 = new Book(8, "Quo Vadis", author1);
        Book b9 = new Book(9, "Krzyżacy", author1);
        Book b10 = new Book(10, "Dzieci Kapitana Granta", author2);

        b1.addLocation(l1);
        b2.addLocation(l1);
        b3.addLocation(l1);
        b4.addLocation(l1);
        b5.addLocation(l1);
        b6.addLocation(l1);
        b7.addLocation(l1);
        b8.addLocation(l1);
        b9.addLocation(l1);
        b10.addLocation(l1);
        b1.addLocation(l2);
        b1.addLocation(l3);
        b1.addLocation(l4);

        List<Book> books = new ArrayList<>();
        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);
        books.add(b5);
        books.add(b6);
        books.add(b7);
        books.add(b8);
        books.add(b9);
        books.add(b10);

        Library library = new Library(1, "Kozieł", books, authors, locations);
        this.user = new User(1, "miki", "123");

        AccessBook ac1 = new AccessBook(b1, this.user, 2020, 1, 1);
        library.addAccessBook(ac1);

        this.user.addLibrary(library);
    }

    public Library getLibrary() {
        return library;
    }

    public User getUser() {
        return user;
    }
}
