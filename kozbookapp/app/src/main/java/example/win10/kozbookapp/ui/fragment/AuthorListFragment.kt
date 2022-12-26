package example.win10.kozbookapp.ui.fragment

import android.app.AlertDialog
import example.win10.kozbookapp.viewmodel.LibraryViewModel
import example.win10.kozbookapp.model.Library
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import example.win10.kozbookapp.R
import android.widget.LinearLayout
import example.win10.kozbookapp.model.Author
import android.widget.TextView
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.Fragment

class AuthorListFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var gridLayout: GridLayout? = null
    private var library: Library? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.author_list_fragment, container, false)
        gridLayout = v.findViewById(R.id.authorsGrid)
        val b = v.findViewById<Button>(R.id.backBtn)
        b.setOnClickListener(this)
        return v
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        populateAuthors()
    }

    private fun populateAuthors() {
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { p: Library? ->
            library = p
            gridLayout!!.removeAllViews()
            for (i in library!!.getAuthors()!!.indices) {
                gridLayout!!.addView(createAuthorLL(i), i)
            }
        }
    }

    private fun createAuthorLL(i: Int): LinearLayout {
        val linearLayout = LinearLayout(this.context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundResource(R.drawable.book_border)
        linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val author = library!!.getAuthors()!![i]
        val name = TextView(this.context)
        name.text = author.name
        linearLayout.addView(name)
        linearLayout.isClickable = true
        linearLayout.setOnClickListener { onClickAuthor(author) }
        linearLayout.setOnLongClickListener { onLongClickAuthor(author, linearLayout) }
        return linearLayout
    }

    private fun onClickAuthor(author: Author?) {
        author?.let { mViewModel.setSelectedAuthor(it) }
        val newFragment: Fragment = AuthorFragment(mViewModel)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun onLongClickAuthor(author: Author?, linearLayout: LinearLayout?): Boolean {
        val dialogClickListener = DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                library!!.removeAuthorFromLibrary(author!!)
                gridLayout!!.removeView(linearLayout)
                //Yes button clicked
            }
        }
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        return true
    }

    override fun onClick(view: View) {
        if (view.id == R.id.backBtn) {
            onBackBtn()
        }
    }

    private fun onBackBtn() {
        val newFragment: Fragment = LibraryFragment(mViewModel)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): AuthorListFragment {
            return AuthorListFragment(mViewModel)
        }
    }
}