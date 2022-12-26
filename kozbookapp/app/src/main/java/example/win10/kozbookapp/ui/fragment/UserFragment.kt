package example.win10.kozbookapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.AccessBook
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.User
import example.win10.kozbookapp.viewmodel.LibraryViewModel

class UserFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var user: User? = null
    private var userText: TextView? = null
    private var accessBookLayout: TableLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.user_fragment, container, false)
        userText = v.findViewById(R.id.userName)
        accessBookLayout = v.findViewById(R.id.booksTable)
        return v
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //        mViewModel = new ViewModelProvider(this).get(BookListViewModel.class);
        mViewModel.user!!.observe(viewLifecycleOwner) { user: User? ->
            this.user = user
            userText!!.text = user?.username
            populateAccessBook()
        }
    }

    private fun populateAccessBook() {
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { library: Library ->
            accessBookLayout!!.removeAllViews()
            val books = library.getAccessBookForUser(user!!)
            for (i in books.indices) {
                val ab = books[i]
                val trHead = addAccessBookTableRow(ab, i)
                accessBookLayout!!.addView(trHead, TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT))
            }
        }
    }

    private fun addAccessBookTableRow(ab: AccessBook, i: Int): TableRow {
        val trHead = TableRow(this.context)
        trHead.id = i + 1
        trHead.setBackgroundColor(Color.GRAY)
        trHead.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        var tv: TextView = addTextView(ab.book!!.name, i + 1)
        trHead.addView(tv)
        tv = addTextView(ab.book!!.author!!.name, i + 1)
        trHead.addView(tv)
        trHead.setOnClickListener {
            mViewModel.setSelectedBook(ab.book!!)
            changeFragment(BookFragment(mViewModel))
        }
        return trHead
    }

    private fun addTextView(data: String?, i: Int): TextView {
        val tv = TextView(this.context)
        tv.id = i + 111
        tv.text = data
        tv.setTextColor(Color.WHITE)
        tv.setPadding(5, 20, 5, 5)
        return tv
    }

    override fun onClick(view: View) {
        if (view.id == R.id.backBtn) {
            changeFragment(LibraryFragment(mViewModel))
        }
    }

    private fun changeFragment(newFragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): UserFragment {
            return UserFragment(mViewModel)
        }
    }
}