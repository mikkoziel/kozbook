package example.win10.kozbookapp.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Author;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class AuthorListFragment extends ListFragment implements View.OnClickListener {

//    private LibraryViewModel mViewModel;
    private GridLayout gridLayout;
    private Library library;

    public AuthorListFragment(LibraryViewModel mViewModel){
        super(mViewModel);
//        this.mViewModel = mViewModel;
    }

    public static AuthorListFragment newInstance(LibraryViewModel mViewModel) {
        return new AuthorListFragment(mViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.author_list_fragment, container, false);

        this.gridLayout = v.findViewById(R.id.authorsGrid);

        Button b = v.findViewById(R.id.backBtn);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateAuthors();
    }


    public void populateAuthors(){
        mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), p -> {
            library = p;
            this.gridLayout.removeAllViews();
            for (int i = 0; i < this.library.getAuthors().size(); i++) {
                this.gridLayout.addView(this.createAuthorLL(i), i);
            }
        });
    }

    private LinearLayout createAuthorLL(int i){
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.book_border);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Author author = this.library.getAuthors().get(i);

        TextView name = new TextView(this.getContext());
        name.setText(author.getName());
        linearLayout.addView(name);

        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(v -> onClickAuthor(author));
        linearLayout.setOnLongClickListener(v -> onLongClickAuthor(author, linearLayout));

        return linearLayout;
    }

    public void onClickAuthor(Author author){
        this.mViewModel.setSelectedAuthor(author);

        Fragment newFragment = new AuthorFragment(this.mViewModel);
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public boolean onLongClickAuthor(Author author, LinearLayout linearLayout){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                this.library.removeAuthorFromLibrary(author);
                this.gridLayout.removeView(linearLayout);
                //Yes button clicked
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backBtn) {
            this.onBackBtn();
        }
    }

    public void onBackBtn(){
        changeFragment(new LibraryFragment(this.mViewModel));
    }
}