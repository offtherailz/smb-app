package it.geosolutions.savemybike.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import it.geosolutions.savemybike.GlideApp;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.Constants;
import it.geosolutions.savemybike.model.Configuration;
import it.geosolutions.savemybike.model.EmissionData;
import it.geosolutions.savemybike.model.user.Profile;
import it.geosolutions.savemybike.model.user.User;
import it.geosolutions.savemybike.model.user.UserInfo;
import it.geosolutions.savemybike.utils.UOMUtils;

/**
 * @author Lorenzo Natali, GeoSolutions S.a.s.
 * Fragment for user profile
 */

public class BaseProfileFragment extends Fragment {
    public static final String TAG = "USER_PROFILE";
    private boolean modified;
    UserInfo user;
    @BindView(R.id.name) TextView name;

    @BindView(R.id.phone_number) TextView phoneNumber;
    @BindView(R.id.userAvatar) ImageView avatar;
    @BindView(R.id.email_text) TextView email;

    @BindView(R.id.co2_text) TextView c02;
    @BindView(R.id.pm10_text) TextView pm10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.base_profile_fragment2, container, false);
        ButterKnife.bind(this, view);
        user = Configuration.getUserProfile(getContext());
        if(user != null) {
            name.setText(user.getFirstName() + " " + user.getLastName());
            email.setText(user.getEmail());
            Profile p = user.getProfile();
            phoneNumber.setText(p.getPhoneNumber());
            EmissionData emissions = user.getTotalEmissions();
            if(emissions!= null) {
                c02.setText(UOMUtils.format( emissions.getCo2Saved() , "###") + "g" );
                pm10.setText(UOMUtils.format(emissions.getPm10Saved() / 1000, "###") + "g" );
            }

            GlideApp.with(this)
                .load(Constants.PORTAL_ENDPOINT + user.getAvatar())
                .override(250, 250)
                .fitCenter() // scale to fit entire image within ImageView
                .apply(RequestOptions.circleCropTransform())
                .into(avatar);

        }
        return view;
    }
    protected void setModified(boolean modified) {
        this.modified = modified;
        // send.setEnabled(true);
    }


}
