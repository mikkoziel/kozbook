package example.win10.kozbookapp.ui.fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Author;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.model.utils.BookLayout;
import example.win10.kozbookapp.ui.dialog.AddBookDialog;
import example.win10.kozbookapp.viewmodel.BookListViewModel;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class BookListFragment extends ListFragment implements View.OnClickListener, AddBookDialog.AddBookDialogListener {

    private BookListViewModel bookListViewModel;

    // Layout variables
    private GridLayout gridLayout;
    private LinearLayout bookOptions;
    private LinearLayout filterLayout;
    private Spinner authorFilter;
    private Spinner locationFilter;

    private int viewWidth;

    public BookListFragment(LibraryViewModel mViewModel) {
        super(mViewModel);
        this.bookListViewModel = new BookListViewModel();
    }

    public static BookListFragment newInstance(LibraryViewModel mViewModel) {
        return new BookListFragment(mViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_list_fragment, container, false);

        this.gridLayout = v.findViewById(R.id.booksGrid);
        this.bookOptions = v.findViewById(R.id.bookOptions);
        this.filterLayout = v.findViewById(R.id.filterLayout);

        this.filterLayout.setVisibility(View.GONE);

        this.authorFilter = v.findViewById(R.id.authorsFilterSpinner);
        this.locationFilter = v.findViewById(R.id.locationFilterSpinner);

//        Button backBtn = v.findViewById(R.id.backBtn);
//        backBtn.setOnClickListener(this);
        Button deleteBtn = v.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        Button searchBtn = v.findViewById(R.id.searchBttn);
        searchBtn.setOnClickListener(this);

        this.unchoseBook();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ViewTreeObserver vto = this.requireView().getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    vto.removeOnGlobalLayoutListener(this);
                    viewWidth = gridLayout.getMeasuredWidth();
                    populateBooks();
                }
            });
        }
    }

    public void populateBooks() {
        this.mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), library -> {
            setViews(library.getBooks());
            this.setFilterSpinners();
        });
    }

    private void setViews(List<Book> books) {
        this.mViewModel.getColumnCount().observe(getViewLifecycleOwner(), cCount -> {
            int size = viewWidth / cCount - 24;
            this.gridLayout.removeAllViews();

            for (int i = 0; i < books.size(); i++) {
                this.gridLayout.addView(this.createBookLL(i, size), i);
            }
        });
    }

    private LinearLayout createBookLL(int i, int size) {
        Book book = this.mViewModel.getSelectedLibraryValue().getBooks().get(i);

        BookLayout bookLayout = new BookLayout(this.getContext(), book);
        bookLayout.setOrientation(LinearLayout.VERTICAL);
        bookLayout.setBackgroundResource(R.drawable.book_border);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        bookLayout.setLayoutParams(layoutParams);

        TextView name = new TextView(this.getContext());
        name.setText(book.getName());
        bookLayout.addView(name);

        TextView author = new TextView(this.getContext());
        author.setText(book.getAuthor().getName());
        bookLayout.addView(author);

        bookLayout.setClickable(true);
        bookLayout.setOnClickListener(v -> onClickBook(book));
        bookLayout.setOnLongClickListener(v -> onLongClickBook(bookLayout));

        return bookLayout;
    }

    public void onClickBook(Book book) {
        BookLayout bookLayout = this.bookListViewModel.getChosenBook().getValue();
        if (bookLayout == null) {
            this.postSelected(book);

            Fragment newFragment = new BookFragment(this.mViewModel);
            FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            if (bookLayout.getBook() == book) {
                this.unchoseBook();
            }
        }
    }

    public boolean onLongClickBook(BookLayout bookLayout) {
        this.choseBook(bookLayout);
        return true;
    }

    private void postSelected(Book book) {
        this.mViewModel.setSelectedBook(book);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.backBtn) {
//            this.onBackBtn();
//        }
        if (view.getId() == R.id.deleteBtn) {
            this.deleteBookBtn();
        }
        if (view.getId() == R.id.searchBttn) {
            this.searchBook();
        }
    }

//    public void onBackBtn() {
//        this.changeFragment(new LibraryFragment(this.mViewModel));
//    }

    public void deleteBookBtn() {
        BookLayout bookLayout = this.bookListViewModel.getChosenBook().getValue();
        assert bookLayout != null;

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                this.mViewModel.getSelectedLibraryValue().removeBookFromLibrary(bookLayout.getBook());
                this.gridLayout.removeView(bookLayout);
                this.unchoseBook();
                //Yes button clicked
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void choseBook(BookLayout bookLayout) {
        this.bookOptions.setVisibility(View.VISIBLE);
        bookLayout.setBackgroundResource(R.drawable.chosen_book_border);
        this.bookListViewModel.postChosenBook(bookLayout);
    }

    public void unchoseBook() {
        this.bookOptions.setVisibility(View.GONE);

        BookLayout bookLayout = this.bookListViewModel.getChosenBookValue();
        if (bookLayout != null) {
            bookLayout.setBackgroundResource(R.drawable.book_border);
            this.bookListViewModel.postChosenBook(null);
        }
    }

    public void searchBook() {
        EditText searchEditText = this.requireView().findViewById(R.id.searchText);
        String searchText = searchEditText.getText().toString().toLowerCase(Locale.ROOT);
        if (searchText.length() >= 3) {
            setViews(this.bookListViewModel.searchLibrary(searchText, mViewModel.getSelectedLibraryValue().getBooks()));
        } else {
            setViews(this.mViewModel.getSelectedLibraryValue().getBooks());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_search) {
            this.bookListViewModel.toggleVisibility(this.filterLayout);
            return true;
        }
        if (id == R.id.app_bar_add) {
            this.addBook();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFilterSpinners() {
        List<Author> authors = this.mViewModel.getSelectedLibraryValue().getAuthors();
        authors.add(0, new Author(-1, "None"));

        this.authorFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int filterAuthorId = authors.get(i).getAuthor_id();
                if (filterAuthorId == -1) {
                    setViews(mViewModel.getSelectedLibraryValue().getBooks());
                } else {
                    Predicate<Book> predicate = book -> book.getAuthor().getAuthor_id().equals(filterAuthorId);
                    List<Book> books = bookListViewModel.filterLibrary(predicate, mViewModel.getSelectedLibraryValue().getBooks());
                    setViews(books);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setViews(mViewModel.getSelectedLibraryValue().getBooks());
            }
        });

        ArrayAdapter<Author> authorsAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, authors);
        authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.authorFilter.setAdapter(authorsAdapter);

        List<Location> locations = this.mViewModel.getSelectedLibraryValue().getLocations();
        locations.add(0, new Location(-1, "None"));

        this.locationFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int filterLocationId = locations.get(i).getLocation_id();
                if (filterLocationId == -1) {
                    setViews(mViewModel.getSelectedLibraryValue().getBooks());
                } else {
                    Predicate<Book> predicate = book -> book.getActiveLocation().getLocation_id().equals(filterLocationId);
                    List<Book> books = bookListViewModel.filterLibrary(predicate, mViewModel.getSelectedLibraryValue().getBooks());
                    setViews(books);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setViews(mViewModel.getSelectedLibraryValue().getBooks());
            }
        });

        ArrayAdapter<Location> locationAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.locationFilter.setAdapter(locationAdapter);
    }

    private void addBook(){
        AddBookDialog dialog = AddBookDialog.newInstance(this.mViewModel.getSelectedLibrary().getValue());
        dialog.setTargetFragment(BookListFragment.this, 300);
        dialog.show(this.requireActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onFinishAddBookDialog(Book book) {
//        this.mViewModel.addNewProfile(profile, this.getContext());
    }
}
