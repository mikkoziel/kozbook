package example.win10.kozbookapp.model

class Book {
    private var bookId = 0
    var name: String? = null
    var author: Author? = null
    private var locations: MutableList<Location>? = null

    constructor()
    constructor(name: String?, author: Author?) {
        this.name = name
        this.author = author
    }

    constructor(name: String?, author: Author?, locations: MutableList<Location>?) {
        this.name = name
        this.author = author
        this.locations = locations
    }

    constructor(book_id: Int, name: String?, author: Author?) {
        this.bookId = book_id
        this.name = name
        this.author = author
        locations = ArrayList()
    }

    fun addLocation(location: Location) {
        locations!!.add(location)
    }

    fun getLocations(): List<Location>? {
        return locations
    }

    val activeLocation: Location?
        get() = if (locations!!.size == 0) {
            null
        } else locations!![locations!!.size - 1]
}