package it.geosolutions.savemybike.ui.activity;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.Constants;
import it.geosolutions.savemybike.model.DataPoint;
import it.geosolutions.savemybike.model.Segment;
import it.geosolutions.savemybike.model.Session;
import it.geosolutions.savemybike.ui.adapters.ViewPagerAdapter;
import it.geosolutions.savemybike.ui.callback.OnFragmentInteractionListener;
import it.geosolutions.savemybike.ui.fragment.SessionDetailsFragment;
import it.geosolutions.savemybike.ui.fragment.TrackDetailsFragment;
import it.geosolutions.savemybike.ui.tasks.GetSessionTask;


/**
 * @author Lorenzo Natali, GeoSolutions S.a.s.
 * Activity for track details view. Manages initialization of the map and various fragments for
 * the details of a track.
 *
 */
public class SessionDetailsActivity extends SMBBaseActivity implements OnMapReadyCallback, OnFragmentInteractionListener {

    private GoogleMap mMap;
    private Session session;
    public static final String SESSION_ID = "SESSION_ID";
    View bottomSheet;
    BottomSheetBehavior bottomSheetBehaviour;
    View tapActionLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_details);
        View ev = findViewById(R.id.emptyView);
        if(ev != null) {
            ev.setVisibility(View.GONE);
        }
        bindDependencies();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpBottomSheet();
        setupActionBar();

        setupViewPager();

    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.track_details_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new SessionDetailsFragment(), getBaseContext().getResources().getString(R.string.track_details));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.track_details_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.tracks_bar_layout).bringToFront();
        toolbar.bringToFront();
    }

    private void bindDependencies() {
        ButterKnife.bind(this);
        if(bottomSheet == null) {
            bottomSheet = findViewById(R.id.track_details_bottom_sheet);
        }
        if(tapActionLayout == null) {
            tapActionLayout = findViewById(R.id.tap_action_layout);
        }
        if(toolbar == null) {
            toolbar = findViewById(R.id.track_details_toolbar);
        }
    }
    private void setUpBottomSheet() {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehaviour.setPeekHeight(300);
        bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        tapActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehaviour.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                {
                    bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }
    private void loadData() {
        setLoading(true);
        Long itemId = (Long) getIntent().getExtras().get(SESSION_ID);
        new GetSessionTask(getBaseContext(), new GetSessionTask.SessionCallback() {
            @Override
            public void showProgressView() {
            }
            @Override
            public void hideProgressView() {
            }
            @Override
            public void done(Session s) {
                session = s;
                if(mMap != null) {
                    displayData();
                }
            }
        }, itemId).execute();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        displayData();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void displayData() {
        // setup data in map
        // update item view
        if(session != null) {
            View view = findViewById(R.id.session_row);
            inflateTrackDataToRecordView(session, view);
            // update map
            if(mMap != null) {
                mMap.setPadding(0, 150, 0, 50);
                // update item info
                if(session != null && mMap != null) {
                    ArrayList<DataPoint> points = session.getDataPoints();
                    PolylineOptions pOpts = new PolylineOptions();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (DataPoint p : points) {
                        LatLng latLng = new LatLng(p.latitude, p.longitude);
                        pOpts.add(latLng);
                        builder.include(latLng);
                    }
                    mMap.addPolyline(pOpts);
                    LatLngBounds bounds = builder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
                }
                setLoading(false);
            }
        }
    }

    public void inflateTrackDataToRecordView(Session session, View view) {
        final TextView distanceTV = view.findViewById(R.id.dist_value);
        // final TextView dataTV = view.findViewById(R.id.data_value);
        final TextView dateTV = view.findViewById(R.id.session_start_datetime);
        final TextView durationTV = view.findViewById(R.id.session_duration_text);

        if (getConfiguration().metric) {
            distanceTV.setText(String.format(Locale.US, "%.1f %s", (session.getDistance() / 1000f), Constants.UNIT_KM));
        } else {
            distanceTV.setText(String.format(Locale.US, "%.1f %s", (session.getDistance() / 1000f) * Constants.KM_TO_MILES, Constants.UNIT_MI));
        }

        dateTV.setText(DateTimeFormat.forPattern("dd MMM, 'ore' HH:mm").print(session.getStartingTime()));
        long millis = Math.round(session.getOverallTime());
        Duration duration = new Duration(millis);
        DateTime date = new DateTime(0, 1, 1, 0, 0, 0, 0);
        durationTV.setText(DateTimeFormat.forPattern("HH:mm:ss").print(date.plus(duration)));
        view.setTag(session.getId());
        if(session.isUploaded()) {
            view.findViewById(R.id.uploaded).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.uploaded).setVisibility(View.GONE);
        }

    }

    /**
     * Creates the (Geo)JSONObject required by the GeoJsonLayer
     * @param segments
     * @return
     */
    private JSONObject createGeoJsonObject(ArrayList<Segment> segments) {
        JSONObject featureCollection = new JSONObject();
        try {
            featureCollection.put("type", "FeatureCollection");
            JSONArray arr = new JSONArray();
            for(Segment segment : segments) {
                JSONObject feature = new JSONObject();
                feature.put("type", "Feature");
                JSONObject properties = new JSONObject();
                properties.put("id", segment.getId());
                properties.put("start_date", segment.getStartDate());
                properties.put("vehicle_type", segment.getVeihicleType());
                feature.put("properties", properties);
                feature.put("geometry", new JSONObject(segment.getGeom()));
                arr.put(feature);
            }

            featureCollection.put("features", arr);
        } finally {
            return featureCollection;
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void setLoading(boolean loading) {
        View v = findViewById(R.id.loading_container);
        if(v != null) {
            v.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
    }
    public void showNoData(String errorType) {
        View v = findViewById(R.id.emptyView);
        if(v != null) {
            v.setVisibility(View.VISIBLE);
            if(errorType == "timeout") {
                ((TextView) v.findViewById(R.id.empty_description)).setText(R.string.server_took_too_long_to_respond);
            }
        }
    }

    @Override
    public void onRequestPermissionGrant(PermissionIntent permissionIntent) {
        // nothing to do, this activity don't require permission.
    }
}