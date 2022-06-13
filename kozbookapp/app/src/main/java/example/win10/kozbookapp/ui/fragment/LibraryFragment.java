package example.win10.kozbookapp.ui.fragment;

import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.User;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class LibraryFragment extends Fragment {

    private LibraryViewModel mViewModel;
    private User user;

    private LinearLayout libraryLayout;

    public LibraryFragment(LibraryViewModel mViewModel){
        super();
        this.mViewModel = mViewModel;
    }

    public static LibraryFragment newInstance(LibraryViewModel mViewModel) {
        return new LibraryFragment(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.library_fragment, container, false);

        libraryLayout = v.findViewById(R.id.libraryLayout);

        return v;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.mockLibrary();

        mViewModel.getUser().observe(getViewLifecycleOwner(), p -> {
            user = p;
            for(Library lib: user.getLibraries()){

                LinearLayout linearLayout = new LinearLayout(this.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setBackgroundResource(R.drawable.book_border);

                TextView libName = new TextView(this.getContext());
                libName.setText(lib.getName());

                linearLayout.addView(libName);

                LinearLayout btnLayout = new LinearLayout(this.getContext());

                Button btn = new Button(this.getContext());
                btn.setText(R.string.book_list);
                btn.setOnClickListener(view -> changeFragment(lib, new BookListFragment(this.mViewModel)));
                btnLayout.addView(btn);

                Button btn1 = new Button(this.getContext());
                btn1.setText(R.string.author_list);
                btn1.setOnClickListener(view -> changeFragment(lib, new AuthorListFragment(this.mViewModel)));
                btnLayout.addView(btn1);

                Button btn2 = new Button(this.getContext());
                btn2.setText(R.string.location_list);
                btn2.setOnClickListener(view -> changeFragment(lib, new LocationListFragment(this.mViewModel)));
                btnLayout.addView(btn2);

                linearLayout.addView(btnLayout);
                libraryLayout.addView(linearLayout);
            }
        });
    }

    private void changeFragment(Library lib, Fragment newFragment){
        this.mViewModel.setSelectedLibraryValue(lib);

        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

}