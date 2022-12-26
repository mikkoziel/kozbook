package example.win10.kozbookapp.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import example.win10.kozbookapp.R
import example.win10.kozbookapp.model.Library
import example.win10.kozbookapp.model.Location
import example.win10.kozbookapp.viewmodel.LibraryViewModel

class LocationListFragment(private val mViewModel: LibraryViewModel) : Fragment(), View.OnClickListener {
    private var gridLayout: GridLayout? = null
    private var library: Library? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.location_list_fragment, container, false)
        gridLayout = v.findViewById(R.id.locationGrid)
        val b = v.findViewById<Button>(R.id.backBtn)
        b.setOnClickListener(this)
        return v
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        populateLocations()
    }

    private fun populateLocations() {
        mViewModel.selectedLibrary.observe(viewLifecycleOwner) { p: Library? ->
            library = p
            gridLayout!!.removeAllViews()
            for (i in library!!.getLocations()!!.indices) {
                gridLayout!!.addView(createLocationLL(i), i)
            }
        }
    }

    private fun createLocationLL(i: Int): LinearLayout {
        val linearLayout = LinearLayout(this.context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundResource(R.drawable.book_border)
        linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val loc = library!!.getLocations()!![i]
        val name = TextView(this.context)
        name.text = loc.name
        linearLayout.addView(name)
        val extraInfo = TextView(this.context)
        extraInfo.text = loc.extraInfo
        linearLayout.addView(extraInfo)
        linearLayout.isClickable = true
        linearLayout.setOnClickListener { onClickLoc(loc) }
        linearLayout.setOnLongClickListener { onLongClickLoc(loc, linearLayout) }
        return linearLayout
    }

    private fun onClickLoc(loc: Location?) {
        loc?.let { mViewModel.setSelectedLocation(it) }
        changeFragment(LocationFragment(mViewModel))
    }

    private fun onLongClickLoc(loc: Location?, linearLayout: LinearLayout?): Boolean {
        val dialogClickListener = DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                library!!.removeLocationFromLibrary(loc!!)
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
        fun newInstance(mViewModel: LibraryViewModel): LocationListFragment {
            return LocationListFragment(mViewModel)
        }
    }
}