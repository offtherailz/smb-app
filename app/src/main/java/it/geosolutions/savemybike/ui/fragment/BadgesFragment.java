package it.geosolutions.savemybike.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.Constants;
import it.geosolutions.savemybike.data.server.RetrofitClient;
import it.geosolutions.savemybike.data.server.SMBRemoteServices;
import it.geosolutions.savemybike.model.Badge;
import it.geosolutions.savemybike.model.Bike;
import it.geosolutions.savemybike.model.CurrentStatus;
import it.geosolutions.savemybike.model.PaginatedResult;
import it.geosolutions.savemybike.model.TrackItem;
import it.geosolutions.savemybike.ui.BikeAdapter;
import it.geosolutions.savemybike.ui.activity.SaveMyBikeActivity;
import it.geosolutions.savemybike.ui.adapters.BadgeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Robert Oehler on 26.10.17.
 *
 * A fragment showing a list of bikes
 * Update by Lorenzo Pini on 09.07.2018
 */

public class BadgesFragment extends Fragment {
    public static final String TAG = "BIKELIST";
    @BindView(R.id.bikes_list) ListView listView;

    BadgeAdapter badgeAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_badges, container, false);
        ButterKnife.bind(this, view);
        SaveMyBikeActivity activity = ((SaveMyBikeActivity)getActivity());

        // setup adapter
        ArrayList badges = new ArrayList<Badge>();

        badges.add(new Badge("new_user"));
        badges.add(new Badge("data_collector_level0"));
        badges.add(new Badge("data_collector_level1"));
        badges.add(new Badge("data_collector_level2"));
        badges.add(new Badge("data_collector_level3"));
        badges.add(new Badge("biker_level1"));
        badges.add(new Badge("biker_level2"));
        badges.add(new Badge("biker_level3"));
        badges.add(new Badge("bike_surfer_level1"));
        badges.add(new Badge("bike_surfer_level2"));
        badges.add(new Badge("bike_surfer_level3"));
        badges.add(new Badge("public_mobility_level1"));
        badges.add(new Badge("public_mobility_level2"));
        badges.add(new Badge("public_mobility_level3"));
        badges.add(new Badge("tpl_surfer_level1"));
        badges.add(new Badge("tpl_surfer_level2"));
        badges.add(new Badge("tpl_surfer_level3"));
        badges.add(new Badge("multi_surfer_level1"));
        badges.add(new Badge("multi_surfer_level2"));
        badges.add(new Badge("multi_surfer_level3"));
        badges.add(new Badge("ecologist_level1"));
        badges.add(new Badge("ecologist_level2"));
        badges.add(new Badge("ecologist_level3"));
        badges.add(new Badge("healthy_level1"));
        badges.add(new Badge("healthy_level2"));
        badges.add(new Badge("healthy_level3"));
        badges.add(new Badge("money_saver_level1"));
        badges.add(new Badge("money_saver_level2"));
        badges.add(new Badge("money_saver_level3"));

        badgeAdapter = new BadgeAdapter(activity, R.layout.item_badge, badges);
        listView.setAdapter(badgeAdapter);

        // set up empty view
        View empty = view.findViewById(R.id.empty_badges);
        if(empty != null && badges != null && badges.size() > 0) {
            empty.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    /**
     * loads the locally available sessions from the database and invalidates the UI
     */
    /*
    private void getBadges() {
        RetrofitClient client = RetrofitClient.getInstance(this.getContext());
        SMBRemoteServices portalServices = client.getPortalServices();
        portalServices.getBadges().enqueue(new Callback<PaginatedResult<Badge>>() {
            @Override
            public void onResponse(Call<PaginatedResult<Badge>> call, Response<PaginatedResult<Badge>> response) {
                showProgress(false);
                PaginatedResult<TrackItem> result = response.body();
                if(result != null && result.getResults() != null) {
                    adapter.clear();
                    adapter.addAll(response.body().getResults());
                    showEmpty(response.body().getResults().size() == 0);
                } else {
                    adapter.clear();
                    adapter.addAll(new ArrayList<>());
                    showEmpty(true);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PaginatedResult<TrackItem>> call, Throwable t) {
                showProgress(false);
                showEmpty(true);
            }
        });
    }*/

}
