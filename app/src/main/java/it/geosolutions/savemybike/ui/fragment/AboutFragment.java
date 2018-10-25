package it.geosolutions.savemybike.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.model.Bike;
import it.geosolutions.savemybike.ui.BikeAdapter;
import it.geosolutions.savemybike.ui.activity.SaveMyBikeActivity;

public class AboutFragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);



        return view;
    }
}
