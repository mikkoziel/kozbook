package example.win10.kozbookapp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.FragmentTransaction;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.AccessBook;
import example.win10.kozbookapp.model.Book;
import example.win10.kozbookapp.model.User;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class UserFragment extends Fragment implements View.OnClickListener {

    private LibraryViewModel mViewModel;
    private User user;

    private TextView userText;
    private TableLayout accessBookLayout;

    public UserFragment(LibraryViewModel mViewModel){
        super();
        this.mViewModel = mViewModel;
    }

    public static UserFragment newInstance(LibraryViewModel mViewModel) {
        return new UserFragment(mViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_fragment, container, false);

        this.userText = v.findViewById(R.id.userName);
        this.accessBookLayout = v.findViewById(R.id.booksTable);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(BookListViewModel.class);
        this.mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            this.userText.setText(user.getUsername());
            this.populateAccessBook();
        });
    }

    private void populateAccessBook(){
        this.mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), library -> {
            this.accessBookLayout.removeAllViews();
            ArrayList<AccessBook> books = library.getAccessBookForUser(this.user);
            for(int i=0; i<books.size();i++) {
                AccessBook ab = books.get(i);
                TableRow tr_head = this.addAccessBookTableRow(ab, i);

                this.accessBookLayout.addView(tr_head, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        });

    }

    private TableRow addAccessBookTableRow(AccessBook ab, int i){
        TableRow tr_head = new TableRow(this.getContext());
        tr_head.setId(i + 1);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView tv;
        tv= this.addTextView(ab.getBook().getName(), i+1);
        tr_head.addView(tv);

        tv= this.addTextView(ab.getBook().getAuthor().getName(), i+1);
        tr_head.addView(tv);

        tr_head.setOnClickListener(v->{
            this.mViewModel.setSelectedBook(ab.getBook());
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
            this.changeFragment(new LibraryFragment(this.mViewModel));
        }
    }

    private void changeFragment(Fragment newFragment){
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}