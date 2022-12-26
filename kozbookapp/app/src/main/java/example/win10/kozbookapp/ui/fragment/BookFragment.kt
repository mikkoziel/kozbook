package example.win10.kozbookapp.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.AccessBook
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.Location
import example.win10.kozbookapp.viewmodel.LibraryViewModel

class BookFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var book: Book? = null
    private var bookBarLayout: LinearLayout? = null
    private var nameText: TextView? = null
    private var authorText: TextView? = null

    //    private ImageView bookCover;
    private var locationTable: TableLayout? = null
    private var accessTable: TableLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.book_fragment, container, false)
        bookBarLayout = v.findViewById(R.id.infoBarLayout)
        nameText = v.findViewById(R.id.bookName)
        authorText = v.findViewById(R.id.authorName)
        //        this.bookCover = v.findViewById(R.id.coverView);
        locationTable = v.findViewById(R.id.locationTable)
        accessTable = v.findViewById(R.id.accessTable)

//        Button b = v.findViewById(R.id.backBtn);
//        b.setOnClickListener(this);
        return v
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vto = requireView().viewTreeObserver
        if (vto.isAlive) {
            vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    vto.removeOnGlobalLayoutListener(this)
                    val viewWidth = bookBarLayout!!.measuredWidth
                    getSelectedBook(viewWidth)
                }
            })
        }
    }

    private fun getSelectedBook(viewWidth: Int) {
        mViewModel.getSelectedBook().observe(viewLifecycleOwner) { book: Book? ->
            this.book = book
            populateBookInfoBar(viewWidth)
            populateLocationTable()
            populateAccessTable()
        }
    }

    private fun populateBookInfoBar(viewWidth: Int) {
        nameText!!.text = book!!.name
        authorText!!.text = book!!.author!!.name
        authorText!!.setOnClickListener {
            mViewModel.setSelectedAuthor(book!!.author!!)
            changeFragment(AuthorFragment(mViewModel))
        }

//        this.bookCover.setLayoutParams(new LinearLayout.LayoutParams(viewWidth/2, viewWidth/2));

//        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.no_cover, null);
//        this.bookCover.setImageDrawable(myDrawable);
    }

    @SuppressLint("ResourceAsColor")
    private fun populateLocationTable() {
        locationTable!!.removeAllViews()
        for (i in book!!.getLocations()!!.indices) {
            val loc = book!!.getLocations()!![i]
            val trHead = addLocationTableRow(loc, i)
            if (loc == book!!.activeLocation) {
                trHead.setBackgroundColor(R.color.latest)
            }
            locationTable!!.addView(trHead, TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT))
        }
    }

    private fun addLocationTableRow(loc: Location, i: Int): TableRow {
        val trHead = TableRow(this.context)
        trHead.id = i + 1
        trHead.setBackgroundColor(Color.GRAY)
        trHead.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        var tv: TextView = addTextView(loc.name, i + 1)
        trHead.addView(tv)
        tv = addTextView(loc.extraInfo, i + 1)
        trHead.addView(tv)
        return trHead
    }

    private fun populateAccessTable() {
        accessTable!!.removeAllViews()
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { p: Library ->
            for (i in p.getAccessBooks()!!.indices) {
                val ab = p.getAccessBooks()!![i]
                val trHead = addAccessTableRow(ab, i)
                accessTable!!.addView(trHead, TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT))
            }
        }
    }

    private fun addAccessTableRow(ab: AccessBook, i: Int): TableRow {
        val trHead = TableRow(this.context)
        trHead.id = i + 1
        trHead.setBackgroundColor(Color.GRAY)
        trHead.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        var tv: TextView = addTextView(ab.user!!.username, i + 1)
        trHead.addView(tv)
        tv = addTextView(ab.startDateString as String, i + 1)
        trHead.addView(tv)
        tv = if (ab.endDate != null) {
            addTextView(ab.endDateString as String, i + 1)
        } else {
            addTextView("", i + 1)
        }
        trHead.addView(tv)
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
            changeFragment(BookListFragment(mViewModel))
        }
    }

    private fun changeFragment(newFragment: Fragment?) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): BookFragment {
            return BookFragment(mViewModel)
        }
    }
}