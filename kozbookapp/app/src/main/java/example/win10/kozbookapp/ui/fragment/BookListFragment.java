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
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.model.utils.BookLayout;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class BookListFragment extends Fragment implements View.OnClickListener {

    private final LibraryViewModel mViewModel;

    private GridLayout gridLayout;
    private LinearLayout bookOptions;
    private LinearLayout searchLayout;
    private LinearLayout filterLayout;
    private Spinner authorFilter;
    private Spinner locationFilter;

    private Library library;

    private final MutableLiveData<BookLayout> chosenBook = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> showBooks = new MutableLiveData<>();
    private int viewWidth;

    public BookListFragment(LibraryViewModel mViewModel) {
        super();
        this.mViewModel = mViewModel;
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
        this.searchLayout = v.findViewById(R.id.searchBar);
        this.filterLayout = v.findViewById(R.id.filterLayout);

        this.filterLayout.setVisibility(View.GONE);
        this.searchLayout.setVisibility(View.GONE);

        this.authorFilter = v.findViewById(R.id.authorsFilterSpinner);
        this.locationFilter = v.findViewById(R.id.locationFilterSpinner);

        Button backBtn = v.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
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
//        mViewModel = new ViewModelProvider(this).get(BookListViewModel.class);
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
        mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), p -> {
            this.library = p;
            setViews(p.getBooks());

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
        Book book = this.library.getBooks().get(i);

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
        BookLayout bookLayout = this.chosenBook.getValue();
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
        if (view.getId() == R.id.backBtn) {
            this.onBackBtn();
        }
        if (view.getId() == R.id.deleteBtn) {
            this.deleteBookBtn();
        }
        if (view.getId() == R.id.searchBttn) {
            this.searchBook();
        }
    }

    public void onBackBtn() {
        Fragment newFragment = new LibraryFragment(this.mViewModel);
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void deleteBookBtn() {
        BookLayout bookLayout = this.chosenBook.getValue();
        assert bookLayout != null;

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                this.library.removeBookFromLibrary(bookLayout.getBook());
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
        this.chosenBook.postValue(bookLayout);
    }

    public void unchoseBook() {
        this.bookOptions.setVisibility(View.GONE);

        BookLayout bookLayout = this.chosenBook.getValue();
        if (bookLayout != null) {
            bookLayout.setBackgroundResource(R.drawable.book_border);
            this.chosenBook.setValue(null);
        }
    }

    public void searchBook() {
        EditText searchEditText = this.requireView().findViewById(R.id.searchText);
        String searchText = searchEditText.getText().toString().toLowerCase(Locale.ROOT);
        if (searchText.length() >= 3) {

            Predicate<Book> byName = book -> book.getName().toLowerCase(Locale.ROOT).contains(searchText);
            Predicate<Book> byAuthor = book -> book.getAuthor().getName().toLowerCase(Locale.ROOT).contains(searchText);

            List<Book> books = this.filterLibrary(byName.or(byAuthor));
//            List<Book> books = this.library.getBooks().parallelStream().filter((byName).or(byAuthor)).collect(Collectors.toList());

            setViews(books);
        } else {
            setViews(this.library.getBooks());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_search) {
            this.toggleVisibility(this.searchLayout);
            return true;
        }
        if (id == R.id.app_bar_filter) {
            this.toggleVisibility(this.filterLayout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setFilterSpinners() {
        List<Author> authors = this.library.getAuthors();
        authors.add(0, new Author(-1, "None"));

        this.authorFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int filterAuthorId = authors.get(i).getAuthor_id();
                if (filterAuthorId == -1) {
                    setViews(library.getBooks());
                } else {

                    Predicate<Book> predicate = book -> book.getAuthor().getAuthor_id().equals(filterAuthorId);
                    List<Book> books = filterLibrary(predicate);
                    setViews(books);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setViews(library.getBooks());
            }
        });

        ArrayAdapter<Author> authorsAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, authors);
        authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.authorFilter.setAdapter(authorsAdapter);


        List<Location> locations = this.library.getLocations();
        locations.add(0, new Location(-1, "None"));

        this.locationFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int filterLocationId = locations.get(i).getLocation_id();
                if (filterLocationId == -1) {
                    setViews(library.getBooks());
                } else {
                    Predicate<Book> predicate = book -> book.getActiveLocation().getLocation_id().equals(filterLocationId);
                    List<Book> books = filterLibrary(predicate);
                    setViews(books);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setViews(library.getBooks());
            }
        });

        ArrayAdapter<Location> locationAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.locationFilter.setAdapter(locationAdapter);
    }

    private List<Book> filterLibrary(Predicate<Book> predicate) {
        return this.library.getBooks().stream().filter(predicate).collect(Collectors.toList());
    }
}