package example.win10.kozbookapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Author
import example.win10.kozbookapp.model.Book
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.Location
import java.util.function.Predicate
import java.util.stream.Collectors

class AddBookDialog(private val library: Library) : DialogFragment(), View.OnClickListener {
    private var editTitle: EditText? = null
    private var editAuthor: EditText? = null
    private var editLocation: EditText? = null

    override fun onClick(view: View) {
        if (view.id == R.id.addBookBtn) {
            addBtn()
        }
    }

    interface AddBookDialogListener {
        fun onFinishAddBookDialog(book: Book?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.add_book_dialog, container)
        val b = v.findViewById<Button>(R.id.addBookBtn)
        b.setOnClickListener(this)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTitle = view.findViewById(R.id.editTitle)
        editAuthor = view.findViewById(R.id.editAuthor)
        editLocation = view.findViewById(R.id.editLocation)
    }

    private fun addBtn() {
        val listener = (targetFragment as AddBookDialogListener?)!!
        listener.onFinishAddBookDialog(createNewBook())
        dismiss()
    }

    private fun createNewBook(): Book {
        return Book(
                editTitle!!.text.toString(),
                author,
                locations
        )
    }

    private val author: Author
        get() {
            val authorName = editAuthor!!.text.toString()
            val equalName = Predicate { author: Author -> author.name!!.lowercase() == authorName.lowercase() }
            val authors = library.getAuthors()!!.stream().filter(equalName).collect(Collectors.toList())
            return if (authors.isEmpty()) {
                Author(authorName)
            } else {
                authors[0]
            }
        }

    private val locations: MutableList<Location>
        get() {
            val locations: MutableList<Location> = ArrayList()
            val locationName = editLocation!!.text.toString().split(":").toTypedArray()
            val equalName = Predicate { loc: Location -> loc.name!!.lowercase() == locationName[0].lowercase() }
            val equalInfo = Predicate { loc: Location -> loc.extraInfo!!.lowercase() == locationName[1].lowercase() }
            val locCheck = library.getLocations()!!.stream().filter(equalName.and(equalInfo)).collect(Collectors.toList())
            if (locCheck.isEmpty()) {
                locations.add(Location(locationName[0], locationName[1]))
            } else {
                locations.add(locCheck[0])
            }
            return locations
        }

    companion object {
        @JvmStatic
        fun newInstance(library: Library): AddBookDialog {
            return AddBookDialog(library)
        }
    }
}