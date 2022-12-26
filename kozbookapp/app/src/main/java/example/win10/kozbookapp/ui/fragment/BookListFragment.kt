package example.win10.kozbookapp.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Author
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.Location
import example.win10.kozbookapp.model.utils.BookLayout
import example.win10.kozbookapp.ui.dialog.AddBookDialog
import example.win10.kozbookapp.ui.dialog.AddBookDialog.AddBookDialogListener
import example.win10.kozbookapp.viewmodel.BookListViewModel
import example.win10.kozbookapp.viewmodel.LibraryViewModel
import java.util.function.Predicate

class BookListFragment(mViewModel: LibraryViewModel?) : ListFragment(mViewModel!!), View.OnClickListener, AddBookDialogListener {
    private val bookListViewModel: BookListViewModel = BookListViewModel()

    // Layout variables
    private var gridLayout: GridLayout? = null
    private var bookOptions: LinearLayout? = null
    private var filterLayout: LinearLayout? = null
    private var authorFilter: Spinner? = null
    private var locationFilter: Spinner? = null
    private var viewWidth = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.book_list_fragment, container, false)
        gridLayout = v.findViewById(R.id.booksGrid)
        bookOptions = v.findViewById(R.id.bookOptions)
        filterLayout = v.findViewById(R.id.filterLayout)
        authorFilter = v.findViewById(R.id.authorsFilterSpinner)
        locationFilter = v.findViewById(R.id.locationFilterSpinner)

//        Button backBtn = v.findViewById(R.id.backBtn);
//        backBtn.setOnClickListener(this);
        val deleteBtn = v.findViewById<Button>(R.id.deleteBtn)
        deleteBtn.setOnClickListener(this)
        val searchBtn = v.findViewById<Button>(R.id.searchBttn)
        searchBtn.setOnClickListener(this)
        unchooseBook()
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterLayout!!.visibility = View.GONE
        val vto = requireView().viewTreeObserver
        if (vto.isAlive) {
            vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    vto.removeOnGlobalLayoutListener(this)
                    viewWidth = gridLayout!!.measuredWidth
                    populateBooks()
                }
            })
        }
    }

    fun populateBooks() {
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { library: Library ->
            setViews(library.getBooks())
            setFilterSpinners()
        }
    }

    private fun setViews(books: List<Book>?) {
        mViewModel.getColumnCount().observe(viewLifecycleOwner) { cCount: Int ->
            val size = viewWidth / cCount - 24
            gridLayout!!.removeAllViews()
            for (i in books!!.indices) {
                gridLayout!!.addView(createBookLL(i, size), i)
            }
        }
    }

    private fun createBookLL(i: Int, size: Int): LinearLayout {
        val book = mViewModel.getSelectedLibraryValue()!!.getBooks()!![i]
        val bookLayout = BookLayout(this.context, book)
        bookLayout.orientation = LinearLayout.VERTICAL
        bookLayout.setBackgroundResource(R.drawable.book_border)
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.width = size
        layoutParams.height = size
        bookLayout.layoutParams = layoutParams
        val name = TextView(this.context)
        name.text = book.name
        bookLayout.addView(name)
        val author = TextView(this.context)
        author.text = book.author!!.name
        bookLayout.addView(author)
        bookLayout.isClickable = true
        bookLayout.setOnClickListener { onClickBook(book) }
        bookLayout.setOnLongClickListener { onLongClickBook(bookLayout) }
        return bookLayout
    }

    private fun onClickBook(book: Book) {
        val bookLayout = bookListViewModel.chosenBook.value
        if (bookLayout == null) {
            postSelected(book)
            val newFragment: Fragment = BookFragment(mViewModel)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            if (bookLayout.book == book) {
                unchooseBook()
            }
        }
    }

    private fun onLongClickBook(bookLayout: BookLayout): Boolean {
        choseBook(bookLayout)
        return true
    }

    private fun postSelected(book: Book) {
        mViewModel.setSelectedBook(book)
    }

    override fun onClick(view: View) {
//        if (view.getId() == R.id.backBtn) {
//            this.onBackBtn();
//        }
        if (view.id == R.id.deleteBtn) {
            deleteBookBtn()
        }
        if (view.id == R.id.searchBttn) {
            searchBook()
        }
    }

    //    public void onBackBtn() {
    //        this.changeFragment(new LibraryFragment(this.mViewModel));
    //    }
    private fun deleteBookBtn() {
        val bookLayout = bookListViewModel.chosenBook.value!!
        val dialogClickListener = DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                mViewModel.getSelectedLibraryValue()!!.removeBookFromLibrary(bookLayout.book)
                gridLayout!!.removeView(bookLayout)
                unchooseBook()
                //Yes button clicked
            }
        }
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
    }

    private fun choseBook(bookLayout: BookLayout) {
        bookOptions!!.visibility = View.VISIBLE
        bookLayout.setBackgroundResource(R.drawable.chosen_book_border)
        bookListViewModel.postChosenBook(bookLayout)
    }

    private fun unchooseBook() {
        bookOptions!!.visibility = View.GONE
        val bookLayout = bookListViewModel.chosenBookValue
        if (bookLayout != null) {
            bookLayout.setBackgroundResource(R.drawable.book_border)
            bookListViewModel.postChosenBook(null)
        }
    }

    private fun searchBook() {
        val searchEditText = requireView().findViewById<EditText>(R.id.searchText)
        val searchText = searchEditText.text.toString().lowercase()
        if (searchText.length >= 3) {
            setViews(bookListViewModel.searchLibrary(searchText, mViewModel.getSelectedLibraryValue()!!.getBooks()!!))
        } else {
            setViews(mViewModel.getSelectedLibraryValue()!!.getBooks())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.app_bar_search) {
            bookListViewModel.toggleVisibility(filterLayout!!)
            return true
        }
        if (id == R.id.app_bar_add) {
            addBook()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFilterSpinners() {
        this.setAuthorFilter()
        this.setLocFilter()
    }

    private fun setAuthorFilter(){
        val authors = mViewModel.getSelectedLibraryValue()!!.getAuthors()
        authors!!.add(0, Author(-1, "None"))
        authorFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val filterAuthorId = authors[i].authorId!!
                if (filterAuthorId == -1) {
                    setViews(mViewModel.getSelectedLibraryValue()!!.getBooks())
                } else {
                    val predicate = Predicate { book: Book -> book.author!!.authorId == filterAuthorId }
                    val books = bookListViewModel.filterLibrary(predicate, mViewModel.getSelectedLibraryValue()!!.getBooks()!!)
                    setViews(books)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                setViews(mViewModel.getSelectedLibraryValue()!!.getBooks())
            }
        }
        val authorsAdapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, authors)
        authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        authorFilter!!.adapter = authorsAdapter
    }

    private fun setLocFilter(){
        val locations = mViewModel.getSelectedLibraryValue()!!.getLocations()
        locations!!.add(0, Location(-1, "None"))
        locationFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val filterLocationId = locations[i].locationId!!
                if (filterLocationId == -1) {
                    setViews(mViewModel.getSelectedLibraryValue()!!.getBooks())
                } else {
                    val predicate = Predicate { book: Book -> book.activeLocation!!.locationId == filterLocationId }
                    val books = bookListViewModel.filterLibrary(predicate, mViewModel.getSelectedLibraryValue()!!.getBooks()!!)
                    setViews(books)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                setViews(mViewModel.getSelectedLibraryValue()!!.getBooks())
            }
        }
        val locationAdapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, locations)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationFilter!!.adapter = locationAdapter
    }

    private fun addBook() {
        val dialog = AddBookDialog.newInstance(mViewModel.selectedLibrary.value!!)
        dialog.setTargetFragment(this@BookListFragment, 300)
        dialog.show(requireActivity().supportFragmentManager, "dialog")
    }

    override fun onFinishAddBookDialog(book: Book?) {
//        this.mViewModel.addNewProfile(profile, this.getContext());
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel?): BookListFragment {
            return BookListFragment(mViewModel)
        }
    }
}