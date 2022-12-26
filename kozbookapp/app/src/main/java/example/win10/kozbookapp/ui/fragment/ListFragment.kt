package example.win10.kozbookapp.ui.fragment

import androidx.fragment.app.Fragment
import example.win10.kozbookapp.viewmodel.LibraryViewModel
import example.win10.kozbookapp.R

open class ListFragment(protected val mViewModel: LibraryViewModel) : Fragment() {
    protected fun changeFragment(newFragment: Fragment?) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(mViewModel: LibraryViewModel): ListFragment {
            return ListFragment(mViewModel)
        }
    }
}