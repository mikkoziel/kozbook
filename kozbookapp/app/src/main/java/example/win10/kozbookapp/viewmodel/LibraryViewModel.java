package example.win10.kozbookapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import example.win10.kozbookapp.Mock_info;
import example.win10.kozbookapp.model.Author;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.model.User;

public class LibraryViewModel extends ViewModel {
//    private MutableLiveData<Library> library;
    private MutableLiveData<User> user;
    
    private final MutableLiveData<Library> selectedLibrary = new MutableLiveData<>();
    private final MutableLiveData<Book> selectedBook = new MutableLiveData<>();
    private final MutableLiveData<Author> selectedAuthor = new MutableLiveData<>();
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();

    private final MutableLiveData<Integer> columnCount = new MutableLiveData<>(3);

    public LibraryViewModel(){
    }

    public void mockLibrary(){
        Mock_info mock_info = new Mock_info();
//        this.setLibrary(new MutableLiveData<>());
        this.setUser(new MutableLiveData<>());
        this.setUserValue(mock_info.getUser());
//        this.setLibraryValue(mock_info.getLibrary());
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public void setUserValue(User user) {
        this.user.setValue(user);
    }



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



    public MutableLiveData<Library> getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(Library library){
        this.selectedLibrary.setValue(library);
    }

    public void setSelectedLibraryValue(Library library) {
        this.selectedLibrary.setValue(library);
    }

    public MutableLiveData<Book> getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book book){
        this.selectedBook.setValue(book);
    }

    public MutableLiveData<Author> getSelectedAuthor() {
        return selectedAuthor;
    }

    public void setSelectedAuthor(Author author){
        this.selectedAuthor.setValue(author);
    }

    public MutableLiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location location){
        this.selectedLocation.setValue(location);
    }


    public MutableLiveData<Integer> getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount.setValue(columnCount);
    }
}