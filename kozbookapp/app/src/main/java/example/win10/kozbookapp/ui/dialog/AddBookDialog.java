package example.win10.kozbookapp.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Author;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.Location;

public class AddBookDialog extends DialogFragment implements View.OnClickListener {
    private EditText editTitle;
    private EditText editAuthor;
    private EditText editLocation;

    final private Library library;

    public AddBookDialog(Library library) {
        this.library = library;
    }

    public static AddBookDialog newInstance(Library library) {
        return new AddBookDialog(library);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addBookBtn) {
            this.add_btn();
        }
    }

    public interface AddBookDialogListener {
        void onFinishAddBookDialog(Book book);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_book_dialog, container);

        Button b = v.findViewById(R.id.addBookBtn);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.editTitle = view.findViewById(R.id.editTitle);
        this.editAuthor = view.findViewById(R.id.editAuthor);
        this.editLocation = view.findViewById(R.id.editLocation);

    }

    private void add_btn(){

        AddBookDialogListener listener = (AddBookDialogListener) getTargetFragment();
        assert listener != null;
        listener.onFinishAddBookDialog(this.createNewBook());
        dismiss();
    }

    private Book createNewBook(){
        return new Book(
                this.editTitle.getText().toString(),
                this.getAuthor(),
                this.getLocations()
        );
    }

    private Author getAuthor(){
        String authorName = this.editAuthor.getText().toString();
        Predicate<Author> equalName = author -> author.getName().toLowerCase(Locale.ROOT).equals(authorName.toLowerCase(Locale.ROOT));
        List<Author> authors = this.library.getAuthors().stream().filter(equalName).collect(Collectors.toList());
        if(authors.isEmpty()){
            return new Author(authorName);
        } else {
            return authors.get(0);
        }
    }

    private List<Location> getLocations(){
        List<Location> locations = new ArrayList<>();
        String[] locationName = this.editLocation.getText().toString().split(":");
        Predicate<Location> equalName = loc -> loc.getName().toLowerCase(Locale.ROOT).equals(locationName[0].toLowerCase(Locale.ROOT));
        Predicate<Location> equalInfo = loc -> loc.getExtra_info().toLowerCase(Locale.ROOT).equals(locationName[1].toLowerCase(Locale.ROOT));
        List<Location> loc_check = this.library.getLocations().stream().filter(equalName.and(equalInfo)).collect(Collectors.toList());
        if(loc_check.isEmpty()){
            locations.add(new Location(locationName[0], locationName[1]));
        } else {
            locations.add(loc_check.get(0));
        }
        return locations;
    }
}
