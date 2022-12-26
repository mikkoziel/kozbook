package example.win10.kozbookapp

import example.win10.kozbookapp.model.*

class MockInfo {
    var library: Library? = null
    var user: User? = null

    init {
        setLibrary()
    }

    private fun setLibrary() {
        val author1 = Author(1, "Henryk Sienkiewicz")
        val author2 = Author(2, "Jules Verne")
        val author3 = Author(3, "")
        val authors: MutableList<Author> = ArrayList()
        authors.add(author1)
        authors.add(author2)
        authors.add(author3)
        val l1 = Location(1, "Kraków", "Mikołaja Pokój")
        val l2 = Location(1, "Kraków", "Michaliny Pokój")
        val l3 = Location(1, "Sypialnia rodziców", "Nad gramofonem")
        val l4 = Location(1, "Sypialnia rodziców", "Nad telewizorem")
        val locations: MutableList<Location> = ArrayList()
        locations.add(l1)
        locations.add(l2)
        locations.add(l3)
        locations.add(l4)
        val b1 = Book(1, "Potop", author1)
        val b2 = Book(2, "Ogniem i mieczem", author1)
        val b3 = Book(3, "Pan Wołodyjowski", author1)
        val b4 = Book(4, "20000 tys mil podmorskiej żeglugi", author2)
        val b5 = Book(5, "Wyprawa na Księzyc", author2)
        val b6 = Book(6, "W 80 dni dookoła świata", author2)
        val b7 = Book(7, "Kamasutra", author3)
        val b8 = Book(8, "Quo Vadis", author1)
        val b9 = Book(9, "Krzyżacy", author1)
        val b10 = Book(10, "Dzieci Kapitana Granta", author2)
        b1.addLocation(l1)
        b2.addLocation(l1)
        b3.addLocation(l1)
        b4.addLocation(l1)
        b5.addLocation(l1)
        b6.addLocation(l1)
        b7.addLocation(l1)
        b8.addLocation(l1)
        b9.addLocation(l1)
        b10.addLocation(l1)
        b1.addLocation(l2)
        b1.addLocation(l3)
        b1.addLocation(l4)
        val books: MutableList<Book> = ArrayList()
        books.add(b1)
        books.add(b2)
        books.add(b3)
        books.add(b4)
        books.add(b5)
        books.add(b6)
        books.add(b7)
        books.add(b8)
        books.add(b9)
        books.add(b10)
        val library = Library(1, "Kozieł", books, authors, locations)
        user = User(1, "miki", "123")
        val ac1 = AccessBook(b1, user, 2020, 1, 1)
        library.addAccessBook(ac1)
        user!!.addLibrary(library)
    }
}