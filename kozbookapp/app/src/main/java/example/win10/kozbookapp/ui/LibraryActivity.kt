package example.win10.kozbookapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import example.win10.kozbookapp.R
import example.win10.kozbookapp.ui.fragment.LibraryFragment.Companion.newInstance
import example.win10.kozbookapp.viewmodel.LibraryViewModel

class LibraryActivity : AppCompatActivity() {
    private var libraryViewModel: LibraryViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_activity)
        libraryViewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newInstance(libraryViewModel!!))
                    .commitNow()
        }
    }
}