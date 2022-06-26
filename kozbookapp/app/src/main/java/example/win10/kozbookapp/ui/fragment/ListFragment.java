package example.win10.kozbookapp.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class ListFragment extends Fragment {

    protected final LibraryViewModel mViewModel;

    public ListFragment(LibraryViewModel mViewModel) {
        super();
        this.mViewModel = mViewModel;
    }

    public static ListFragment newInstance(LibraryViewModel mViewModel) {
        return new ListFragment(mViewModel);
    }

    protected void changeFragment(Fragment newFragment){
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
