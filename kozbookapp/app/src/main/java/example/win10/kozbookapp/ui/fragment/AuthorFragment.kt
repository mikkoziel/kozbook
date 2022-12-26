package example.win10.kozbookapp.ui.fragment

import android.graphics.Color
import example.win10.kozbookapp.viewmodel.LibraryViewModel
import example.win10.kozbookapp.model.Author
import android.widget.TextView
import android.widget.TableLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.Library

class AuthorFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var author: Author? = null
    private var authorName: TextView? = null
    private var booksTable: TableLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.author_fragment, container, false)
        authorName = v.findViewById(R.id.authorName)
        booksTable = v.findViewById(R.id.booksTable)
        val b = v.findViewById<Button>(R.id.backBtn)
        b.setOnClickListener(this)
        return v
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.selectedAuthor.observe(viewLifecycleOwner) { author: Author? ->
            this.author = author
            authorName!!.text = this.author!!.name
            populateBooks()
        }
    }

    private fun populateBooks() {
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { library: Library ->
            booksTable!!.removeAllViews()
            val books = library.getBooksForAuthor(author!!)
            for (i in books.indices) {
                val book = books[i]
                val trHead = addBooksTableRow(book, i)
                booksTable!!.addView(trHead, TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT))
            }
        }
    }

    private fun addBooksTableRow(book: Book, i: Int): TableRow {
        val trHead = TableRow(this.context)
        trHead.id = i + 1
        trHead.setBackgroundColor(Color.GRAY)
        trHead.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        val tv: TextView = addTextView(book.name, i + 1)
        trHead.addView(tv)
        trHead.setOnClickListener {
            mViewModel.setSelectedBook(book)
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
            changeFragment(AuthorListFragment(mViewModel))
        }
    }

    private fun changeFragment(newFragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): AuthorFragment {
            return AuthorFragment(mViewModel)
        }
    }
}