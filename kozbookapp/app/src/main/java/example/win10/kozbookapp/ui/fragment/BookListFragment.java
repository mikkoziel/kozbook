package example.win10.kozbookapp.ui.fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class BookListFragment extends Fragment implements View.OnClickListener {

    private LibraryViewModel mViewModel;
    private GridLayout gridLayout;
    private Library library;

    public BookListFragment(LibraryViewModel mViewModel){
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

        Button b = v.findViewById(R.id.backBtn);
        b.setOnClickListener(this);

        return v;
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
                    int viewWidth = gridLayout.getMeasuredWidth();
                    populateBooks(viewWidth);
                }
            });
        }
    }

    public void populateBooks(int viewWidth){
        mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), p -> {
            library = p;
            setViews(viewWidth);
        });
    }

    private void setViews(int layoutWidth) {
        this.mViewModel.getColumnCount().observe(getViewLifecycleOwner(), cCount -> {
            int size = layoutWidth / cCount - 24;
            this.gridLayout.removeAllViews();

            for (int i = 0; i < this.library.getBooks().size(); i++) {
                this.gridLayout.addView(this.createBookLL(i, size), i);
            }
        });
    }

    private LinearLayout createBookLL(int i, int size){
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.custom_border);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        linearLayout.setLayoutParams(layoutParams);

        Book book = this.library.getBooks().get(i);

        TextView name = new TextView(this.getContext());
        name.setText(book.getName());
        linearLayout.addView(name);

        TextView author = new TextView(this.getContext());
        author.setText(book.getAuthor().getName());
        linearLayout.addView(author);

        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(v -> onClickBook(book));
        linearLayout.setOnLongClickListener(v -> onLongClickBook(book, linearLayout));

        return linearLayout;
    }

    public void onClickBook(Book book){
        this.postSelected(book);

        Fragment newFragment = new BookFragment(this.mViewModel);
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public boolean onLongClickBook(Book book, LinearLayout linearLayout){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                this.library.removeBookFromLibrary(book);
                this.gridLayout.removeView(linearLayout);
                //Yes button clicked
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return true;
    }

    private void postSelected(Book book){
        this.mViewModel.setSelectedBook(book);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backBtn) {
            this.onBackBtn();
        }
    }

    public void onBackBtn(){
        Fragment newFragment = new LibraryFragment(this.mViewModel);
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}