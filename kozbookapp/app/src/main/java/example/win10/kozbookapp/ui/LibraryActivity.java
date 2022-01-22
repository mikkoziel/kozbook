package example.win10.kozbookapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.ui.fragment.LibraryFragment;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class LibraryActivity extends AppCompatActivity {

    private LibraryViewModel libraryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity);
        this.libraryViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, LibraryFragment.newInstance(this.libraryViewModel))
                    .commitNow();
        }
    }
}