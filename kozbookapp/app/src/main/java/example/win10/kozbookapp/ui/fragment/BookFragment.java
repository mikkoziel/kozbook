package example.win10.kozbookapp.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.fragment.app.FragmentTransaction;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.AccessBook;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class BookFragment extends Fragment implements View.OnClickListener {
    private LibraryViewModel mViewModel;
    private Book book;

    private LinearLayout bookBarLayout;
    private TextView nameText;
    private TextView authorText;
//    private ImageView bookCover;
    private TableLayout locationTable;
    private TableLayout accessTable;

    public BookFragment(LibraryViewModel mViewModel){
        super();
        this.mViewModel = mViewModel;
    }

    public static BookFragment newInstance(LibraryViewModel mViewModel) {
        return new BookFragment(mViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_fragment, container, false);

        this.bookBarLayout = v.findViewById(R.id.infoBarLayout);
        this.nameText = v.findViewById(R.id.bookName);
        this.authorText = v.findViewById(R.id.authorName);
//        this.bookCover = v.findViewById(R.id.coverView);

        this.locationTable = v.findViewById(R.id.locationTable);
        this.accessTable = v.findViewById(R.id.accessTable);

//        Button b = v.findViewById(R.id.backBtn);
//        b.setOnClickListener(this);

        return v;
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
                    int viewWidth = bookBarLayout.getMeasuredWidth();
                    getSelectedBook(viewWidth);
                }
            });
        }
    }

    private void getSelectedBook(int viewWidth){
        this.mViewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            this.book = book;

            this.populateBookInfoBar(viewWidth);
            this.populateLocationTable();
            this.populateAccessTable();
        });
    }

    private void populateBookInfoBar(int viewWidth){
        this.nameText.setText(book.getName());
        this.authorText.setText(book.getAuthor().getName());
        this.authorText.setOnClickListener(v->{
            this.mViewModel.setSelectedAuthor(this.book.getAuthor());
            this.changeFragment(new AuthorFragment(this.mViewModel));
        });

//        this.bookCover.setLayoutParams(new LinearLayout.LayoutParams(viewWidth/2, viewWidth/2));

//        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.no_cover, null);
//        this.bookCover.setImageDrawable(myDrawable);
    }

    @SuppressLint("ResourceAsColor")
    private void populateLocationTable(){
        this.locationTable.removeAllViews();
        for(int i=0; i<this.book.getLocations().size();i++) {
            Location loc = this.book.getLocations().get(i);
            TableRow tr_head = this.addLocationTableRow(loc, i);

            if(i==0){
                tr_head.setBackgroundColor(R.color.latest);
            }

            this.locationTable.addView(tr_head, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
    private TableRow addLocationTableRow(Location loc, int i){
        TableRow tr_head = new TableRow(this.getContext());
        tr_head.setId(i + 1);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView tv;
        tv= this.addTextView(loc.getName(), i+1);
        tr_head.addView(tv);
        tv= this.addTextView(loc.getExtra_info(), i+1);
        tr_head.addView(tv);

        return tr_head;
    }

    private void populateAccessTable(){
        this.accessTable.removeAllViews();

        mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), p -> {
            for (int i = 0; i < p.getAccessBooks().size(); i++) {
                AccessBook ab = p.getAccessBooks().get(i);
                TableRow tr_head = this.addAccessTableRow(ab, i);

                this.accessTable.addView(tr_head, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        });
    }

    private TableRow addAccessTableRow(AccessBook ab, int i){
        TableRow tr_head = new TableRow(this.getContext());
        tr_head.setId(i + 1);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView tv;
        tv= this.addTextView(ab.getUser().getUsername(), i+1);
        tr_head.addView(tv);
        tv= this.addTextView((String) ab.getStartDateString(), i+1);
        tr_head.addView(tv);
        if(ab.getEndDate() != null){
            tv= this.addTextView((String) ab.getEndDateString(), i+1);
        } else{
            tv= this.addTextView("", i+1);
        }
        tr_head.addView(tv);

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
            this.changeFragment(new BookListFragment(this.mViewModel));
        }
    }

    public void changeFragment(Fragment newFragment){
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}