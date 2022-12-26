package example.win10.kozbookapp.model

import java.util.ArrayList

class User {
    var userId = 0
    var username: String? = null
    var password: String? = null
    private var libraries: MutableList<Library>? = null

    constructor(userId: Int, username: String?, password: String?) {
        this.userId = userId
        this.username = username
        this.password = password
        libraries = ArrayList()
    }

    fun getLibraries(): List<Library>? {
        return libraries
    }

    fun setLibraries(libraries: MutableList<Library>?) {
        this.libraries = libraries
    }

    fun addLibrary(library: Library) {
        libraries!!.add(library)
    }
}