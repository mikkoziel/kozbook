package example.win10.kozbookapp.model

import example.win10.kozbookapp.model.Library
import java.util.ArrayList

class User(var userId: Int, var username: String?, var password: String?) {
    private var libraries: MutableList<Library>? = null

    init {
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