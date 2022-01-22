package example.win10.kozbookapp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.FragmentTransaction;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Author;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class AuthorFragment extends Fragment implements View.OnClickListener {

    private LibraryViewModel mViewModel;
    private Author author;

    private TextView authorName;
    private TableLayout booksTable;

    public AuthorFragment(LibraryViewModel mViewModel) {
        super();
        this.mViewModel = mViewModel;
    }

    public static AuthorFragment newInstance(LibraryViewModel mViewModel) {
        return new AuthorFragment(mViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.author_fragment, container, false);

        this.authorName = v.findViewById(R.id.authorName);
        this.booksTable = v.findViewById(R.id.booksTable);

        Button b = v.findViewById(R.id.backBtn);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mViewModel.getSelectedAuthor().observe(getViewLifecycleOwner(), author -> {
            this.author = author;

            this.authorName.setText(this.author.getName());
            this.populateBooks();
        });
    }

    private void populateBooks(){
        this.mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), library -> {
            this.booksTable.removeAllViews();
            ArrayList<Book> books = library.getBooksForAuthor(this.author);
            for(int i=0; i<books.size();i++) {
                Book book = books.get(i);
                TableRow tr_head = this.addBooksTableRow(book, i);

                this.booksTable.addView(tr_head, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        });

    }

    private TableRow addBooksTableRow(Book book, int i){
        TableRow tr_head = new TableRow(this.getContext());
        tr_head.setId(i + 1);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView tv;
        tv= this.addTextView(book.getName(), i+1);
        tr_head.addView(tv);

        tr_head.setOnClickListener(v->{
            this.mViewModel.setSelectedBook(book);
            this.changeFragment(new BookFragment(this.mViewModel));
        });

        return tr_head;
    }

    private TextView addTextView(String data, int i){
        TextView tv = new TextView(this.getContext());
        tv.setId(i + 111);
        tv.setText(data);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(5, 20, 5, 5);
        return tv;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backBtn) {
            this.changeFragment(new AuthorListFragment(this.mViewModel));
        }
    }

    private void changeFragment(Fragment newFragment){
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}