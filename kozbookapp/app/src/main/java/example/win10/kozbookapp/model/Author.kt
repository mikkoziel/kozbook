package example.win10.kozbookapp.model

class Author {
    var authorId: Int? = null
    var name: String? = null

    constructor()
    constructor(name: String?) {
        this.name = name
    }

    constructor(author_id: Int?, name: String?) {
        this.authorId = author_id
        this.name = name
    }

    override fun toString(): String {
        return name!!
    }
}