package example.win10.kozbookapp.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Author
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.Location
import example.win10.kozbookapp.model.utils.BookLayout
import example.win10.kozbookapp.viewmodel.LibraryViewModel
import java.util.function.Predicate
import java.util.stream.Collectors

class BookListFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var gridLayout: GridLayout? = null
    private var bookOptions: LinearLayout? = null
    private var searchLayout: LinearLayout? = null
    private var filterLayout: LinearLayout? = null
    private var authorFilter: Spinner? = null
    private var locationFilter: Spinner? = null

    private var library: Library? = null

    private val chosenBook = MutableLiveData<BookLayout?>()
    private var viewWidth = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.book_list_fragment, container, false)

        gridLayout = v.findViewById(R.id.booksGrid)
        bookOptions = v.findViewById(R.id.bookOptions)
        searchLayout = v.findViewById(R.id.searchBar)
        filterLayout = v.findViewById(R.id.filterLayout)

        authorFilter = v.findViewById(R.id.authorsFilterSpinner)
        locationFilter = v.findViewById(R.id.locationFilterSpinner)

        val backBtn = v.findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener(this)
        val deleteBtn = v.findViewById<Button>(R.id.deleteBtn)
        deleteBtn.setOnClickListener(this)
        val searchBtn = v.findViewById<Button>(R.id.searchBttn)
        searchBtn.setOnClickListener(this)

        this.unchooseBook()
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
        //        mViewModel = new ViewModelProvider(this).get(BookListViewModel.class);

        filterLayout!!.visibility = View.GONE
        searchLayout!!.visibility = View.GONE

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
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { p: Library ->
            library = p
            setViews(p.getBooks())
            this.setFilterSpinners()
        }
    }

    private fun setViews(books: List<Book>?) {
        mViewModel.columnCount.observe(viewLifecycleOwner) { cCount: Int ->
            val size = viewWidth / cCount - 24
            gridLayout!!.removeAllViews()
            for (i in books!!.indices) {
                gridLayout!!.addView(this.createBookLL(i, size), i)
            }
        }
    }

    private fun createBookLL(i: Int, size: Int): LinearLayout {
        val book = library!!.getBooks()!![i]
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
        val bookLayout = chosenBook.value
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
        if (view.id == R.id.backBtn) {
            onBackBtn()
        }
        if (view.id == R.id.deleteBtn) {
            deleteBookBtn()
        }
        if (view.id == R.id.searchBttn) {
            this.searchBook()
        }
    }

    private fun onBackBtn() {
        val newFragment: Fragment = LibraryFragment(mViewModel)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun deleteBookBtn() {
        val bookLayout = chosenBook.value!!
        val dialogClickListener = DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                library!!.removeBookFromLibrary(bookLayout.book)
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
        chosenBook.postValue(bookLayout)
    }

    private fun unchooseBook() {
        bookOptions!!.visibility = View.GONE
        val bookLayout = chosenBook.value
        if (bookLayout != null) {
            bookLayout.setBackgroundResource(R.drawable.book_border)
            chosenBook.value = null
        }
    }

    private fun searchBook() {
        val searchEditText = requireView().findViewById<EditText>(R.id.searchText)
        val searchText = searchEditText.text.toString().lowercase()
        if (searchText.length >= 3) {
            val byName = Predicate { book: Book -> book.name!!.lowercase().contains(searchText) }
            val byAuthor = Predicate { book: Book -> book.author!!.name!!.lowercase().contains(searchText) }
            val books: List<Book> = this.filterLibrary(byName.or(byAuthor))
            //            List<Book> books = this.library.getBooks().parallelStream().filter((byName).or(byAuthor)).collect(Collectors.toList());
            setViews(books)
        } else {
            setViews(library!!.getBooks())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.app_bar_search) {
            searchLayout?.let { toggleVisibility(it) }
            return true
        }
        if (id == R.id.app_bar_filter) {
            filterLayout?.let { toggleVisibility(it) }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleVisibility(view: View) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    private fun filterLibrary(predicate: Predicate<Book>): List<Book> {
        return library?.getBooks()?.stream()?.filter(predicate)?.collect(Collectors.toList()) as List<Book>
    }

    private fun setFilterSpinners(){
        this.setAuthorFilter()
        this.setLocationFilter()

    }

    private fun setAuthorFilter(){
        val authors = this.library?.getAuthors()?.stream()?.map(Author::name)?.collect(Collectors.toList())
        authors!!.add(0, "None")

        authorFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val filterText = authors[i]!!
                if (filterText == "None") {
                    setViews(library!!.getBooks())
                } else {
                    val predicate = Predicate { book: Book -> book.author!!.name!!.contains(filterText) }
                    val books = filterLibrary(predicate)
                    setViews(books)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                setViews(library!!.getBooks())
            }
        }

        val authorsAD = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, authors)
        authorsAD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        authorFilter!!.adapter = authorsAD

    }

    private fun setLocationFilter(){
        val locations = library?.getLocations()?.stream()?.map { loc: Location -> loc.name + ";" + loc.extraInfo }?.collect(Collectors.toList())
        locations!!.add(0, "None")

        locationFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val filterText = locations[i]
                if (filterText == "None") {
                    setViews(library!!.getBooks())
                } else {
                    val locArr = filterText.split(";").toTypedArray()
                    val predicate0 = Predicate { book: Book -> book.activeLocation!!.name!!.contains(locArr[0]) }
                    val predicate1 = Predicate { book: Book -> book.activeLocation!!.extraInfo!!.contains(locArr[1]) }
                    val books = filterLibrary(predicate0.and(predicate1))
                    setViews(books)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                setViews(library!!.getBooks())
            }
        }

        val locationAD = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, locations)
        locationAD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationFilter!!.adapter = locationAD
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): BookListFragment {
            return BookListFragment(mViewModel)
        }
    }
}