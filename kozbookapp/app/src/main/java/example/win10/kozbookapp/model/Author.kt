package example.win10.kozbookapp.model

class Author {
    var authorId = 0
    var name: String? = null

    constructor()
    constructor(name: String?) {
        this.name = name
    }

    constructor(authorId: Int, name: String?) {
        this.authorId = authorId
        this.name = name
    }
}