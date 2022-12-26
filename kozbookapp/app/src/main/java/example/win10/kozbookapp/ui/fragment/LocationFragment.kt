package example.win10.kozbookapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.Location
import example.win10.kozbookapp.viewmodel.LibraryViewModel

class LocationFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var location: Location? = null
    private var locationName: TextView? = null
    private var locationsTable: TableLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.location_fragment, container, false)
        locationName = v.findViewById(R.id.authorName)
        locationsTable = v.findViewById(R.id.booksTable)
        val b = v.findViewById<Button>(R.id.backBtn)
        b.setOnClickListener(this)
        return v
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.selectedLocation.observe(viewLifecycleOwner) { location: Location? ->
            this.location = location
            locationName!!.text = this.location!!.name
            populateBooks()
        }
    }

    private fun populateBooks() {
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { library: Library ->
            locationsTable!!.removeAllViews()
            val books = library.getBooksForLocation(location!!)
            for (i in books.indices) {
                val book = books[i]
                val trHead = addBooksTableRow(book, i)
                locationsTable!!.addView(trHead, TableLayout.LayoutParams(
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
        var tv: TextView = addTextView(book.name, i + 1)
        trHead.addView(tv)
        tv = addTextView(book.author!!.name, i + 1)
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
            changeFragment(LocationListFragment(mViewModel))
        }
    }

    private fun changeFragment(newFragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): LocationFragment {
            return LocationFragment(mViewModel)
        }
    }
}