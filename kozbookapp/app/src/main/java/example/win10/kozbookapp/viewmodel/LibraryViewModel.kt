package example.win10.kozbookapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.win10.kozbookapp.MockInfo
import example.win10.kozbookapp.model.*

class LibraryViewModel : ViewModel() {
    var library: MutableLiveData<Library>? = null
    var user: MutableLiveData<User?>? = null
    val selectedLibrary = MutableLiveData<Library>()
    val selectedBook = MutableLiveData<Book>()
    val selectedAuthor = MutableLiveData<Author>()
    val selectedLocation = MutableLiveData<Location>()
    val columnCount = MutableLiveData(3)

    fun mockLibrary() {
        val mockInfo = MockInfo()
        //        this.setLibrary(new MutableLiveData<>());
        user = MutableLiveData()
        setUserValue(mockInfo.user)
        //        this.setLibraryValue(mock_info.getLibrary());
    }

    private fun setUserValue(user: User?) {
        this.user!!.value = user
    }

    fun setLibraryValue(library: Library) {
        this.library!!.value = library
    }

    fun setSelectedLibrary(library: Library) {
        selectedLibrary.value = library
    }

    fun setSelectedLibraryValue(library: Library) {
        selectedLibrary.value = library
    }

    fun setSelectedBook(book: Book) {
        selectedBook.value = book
    }

    fun setSelectedAuthor(author: Author) {
        selectedAuthor.value = author
    }

    fun setSelectedLocation(location: Location) {
        selectedLocation.value = location
    }

    fun setColumnCount(columnCount: Int) {
        this.columnCount.value = columnCount
    }
}