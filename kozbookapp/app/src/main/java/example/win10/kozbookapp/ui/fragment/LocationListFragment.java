package example.win10.kozbookapp.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import example.win10.kozbookapp.R;
import example.win10.kozbookapp.model.Library;
import example.win10.kozbookapp.model.Location;
import example.win10.kozbookapp.viewmodel.LibraryViewModel;

public class LocationListFragment extends Fragment implements View.OnClickListener {

    private LibraryViewModel mViewModel;
    private GridLayout gridLayout;
    private Library library;

    public LocationListFragment(LibraryViewModel mViewModel){
        super();
        this.mViewModel = mViewModel;
    }

   public static LocationListFragment newInstance(LibraryViewModel mViewModel) {
       return new LocationListFragment(mViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.location_list_fragment, container, false);

        this.gridLayout = v.findViewById(R.id.locationGrid);

        Button b = v.findViewById(R.id.backBtn);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateLocations();
    }


    public void populateLocations(){
        mViewModel.getSelectedLibrary().observe(getViewLifecycleOwner(), p -> {
            library = p;
            this.gridLayout.removeAllViews();
            for (int i = 0; i < this.library.getLocations().size(); i++) {
                this.gridLayout.addView(this.createLocationLL(i), i);
            }
        });
    }

    private LinearLayout createLocationLL(int i){
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.book_border);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Location loc = this.library.getLocations().get(i);

        TextView name = new TextView(this.getContext());
        name.setText(loc.getName());
        linearLayout.addView(name);

        TextView extraInfo = new TextView(this.getContext());
        extraInfo.setText(loc.getExtra_info());
        linearLayout.addView(extraInfo);

        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(v -> onClickLoc(loc));
        linearLayout.setOnLongClickListener(v -> onLongClickLoc(loc, linearLayout));

        return linearLayout;
    }

    public void onClickLoc(Location loc){
        this.mViewModel.setSelectedLocation(loc);
        this.changeFragment(new LocationFragment(this.mViewModel));

    }

    public boolean onLongClickLoc(Location loc, LinearLayout linearLayout){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                this.library.removeLocationFromLibrary(loc);
                this.gridLayout.removeView(linearLayout);
                //Yes button clicked
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backBtn) {
            this.changeFragment(new LibraryFragment(this.mViewModel));
        }
    }

    private void changeFragment(Fragment newFragment){
        FragmentTransaction transaction = this.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}