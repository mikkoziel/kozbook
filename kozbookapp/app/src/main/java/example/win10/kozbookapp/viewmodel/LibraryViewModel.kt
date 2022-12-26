package example.win10.kozbookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import example.win10.kozbookapp.MockInfo
import example.win10.kozbookapp.model.*

class LibraryViewModel : ViewModel() {
    //    private MutableLiveData<Library> library;
    var user: MutableLiveData<User?>? = null

    //    public MutableLiveData<Library> getLibrary() {
    //        return library;
    //    }
    //
    //    public void setLibrary(MutableLiveData<Library> library) {
    //        this.library = library;
    //    }
    //
    //    public void setLibraryValue(Library library) {
    //        this.library.setValue(library);
    //    }
    val selectedLibrary = MutableLiveData<Library>()
    private val selectedBook = MutableLiveData<Book>()
    private val selectedAuthor = MutableLiveData<Author>()
    private val selectedLocation = MutableLiveData<Location>()
    private val columnCount = MutableLiveData(3)

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

    fun getSelectedLibraryValue(): Library? {
        return selectedLibrary.value
    }

    fun setSelectedLibrary(library: Library) {
        selectedLibrary.value = library
    }

    fun setSelectedLibraryValue(library: Library) {
        selectedLibrary.value = library
    }

    fun getSelectedBook(): MutableLiveData<Book> {
        return selectedBook
    }

    fun setSelectedBook(book: Book) {
        selectedBook.value = book
    }

    fun getSelectedAuthor(): MutableLiveData<Author> {
        return selectedAuthor
    }

    fun setSelectedAuthor(author: Author) {
        selectedAuthor.value = author
    }

    fun getSelectedLocation(): MutableLiveData<Location> {
        return selectedLocation
    }

    fun setSelectedLocation(location: Location) {
        selectedLocation.value = location
    }

    fun getColumnCount(): MutableLiveData<Int> {
        return columnCount
    }

    fun setColumnCount(columnCount: Int) {
        this.columnCount.value = columnCount
    }
}