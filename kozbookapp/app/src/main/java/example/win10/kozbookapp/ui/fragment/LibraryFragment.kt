package example.win10.kozbookapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.User
import example.win10.kozbookapp.viewmodel.LibraryViewModel

class LibraryFragment(private val mViewModel: LibraryViewModel) : Fragment() {
    private var user: User? = null
    private var libraryLayout: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.library_fragment, container, false)
        libraryLayout = v.findViewById(R.id.libraryLayout)
        return v
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.mockLibrary()
        mViewModel.user!!.observe(viewLifecycleOwner) { p: User? ->
            user = p
            for (lib in user!!.getLibraries()!!) {
                val linearLayout = LinearLayout(this.context)
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.setBackgroundResource(R.drawable.book_border)
                val libName = TextView(this.context)
                libName.text = lib.name
                linearLayout.addView(libName)

                val btnLayout = libBtnLayout(lib)
                linearLayout.addView(btnLayout)

                libraryLayout!!.addView(linearLayout)
            }
        }
    }

    private fun libBtnLayout(lib: Library): LinearLayout{
        val btnLayout = LinearLayout(this.context)

        val btn = Button(this.context)
        btn.setText(R.string.book_list)
        btn.setOnClickListener { changeFragment(lib, BookListFragment(mViewModel)) }
        btnLayout.addView(btn)
        val btn1 = Button(this.context)
        btn1.setText(R.string.author_list)
        btn1.setOnClickListener { changeFragment(lib, AuthorListFragment(mViewModel)) }
        btnLayout.addView(btn1)
        val btn2 = Button(this.context)
        btn2.setText(R.string.location_list)
        btn2.setOnClickListener { changeFragment(lib, LocationListFragment(mViewModel)) }
        btnLayout.addView(btn2)

        return btnLayout
    }

    private fun changeFragment(lib: Library, newFragment: Fragment) {
        mViewModel.setSelectedLibraryValue(lib)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(mViewModel: LibraryViewModel): LibraryFragment {
            return LibraryFragment(mViewModel)
        }
    }
}