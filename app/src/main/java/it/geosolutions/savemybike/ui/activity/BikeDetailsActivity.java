package it.geosolutions.savemybike.ui.activity;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.server.RetrofitClient;
import it.geosolutions.savemybike.data.server.SMBRemoteServices;
import it.geosolutions.savemybike.model.Bike;
import it.geosolutions.savemybike.model.Observation;
import it.geosolutions.savemybike.ui.adapters.ViewPagerAdapter;
import it.geosolutions.savemybike.ui.callback.OnFragmentInteractionListener;
import it.geosolutions.savemybike.ui.custom.BottomSheetBehaviorGoogleMapsLike;
import it.geosolutions.savemybike.ui.fragment.BikeDetailsFragment;
import it.geosolutions.savemybike.ui.fragment.BikeListFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Lorenzo Natali, GeoSolutions S.a.s.
 * Activity for bike details view. Manages initialization of the map and various fragments for
 * the details of a bike.
 *
 */
public class BikeDetailsActivity extends SMBBaseActivity implements OnMapReadyCallback, OnFragmentInteractionListener {
    private static final String TAG = "BIKEDETAILS";
    public static final String OBSERVED_AT = "observed_at";
    private static final String PROPERTY_OBSERVED_AT = OBSERVED_AT;
    private static final String LAST_UPDATES = "LAST_UPDATES";
    public static final String ADDRESS = "address";
    public static final String REPORTER_NAME = "reporter_name";
    private GoogleMap mMap;
    GeoJsonLayer layer;
    private Bike bike;
    JSONObject geojson;
    private boolean layoutDone = false;
    public static final String BIKE = "BIKE";
    View bottomSheet;
    BottomSheetBehaviorGoogleMapsLike bottomSheetBehaviour;
    View tapActionLayout;
    Toolbar toolbar;
    BikeDetailsFragment details = new BikeDetailsFragment();

    @BindView(R.id.bike_name) TextView bikeName;
    private boolean firstRender =true;

    /**
     * Flag to skip move to re-center the map, used to
     * skip map resize when maker has been selected, clicking on a
     * feature
     */
    private boolean skipMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bike = (Bike) getIntent().getExtras().getSerializable(BIKE);
        details.setEventsCallbacks(new BikeDetailsFragment.EventCallbacks() {
            @Override
            public void onSelect(Observation o) {
                selectFeature(o.id);
            }
        });
        loadData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_details);
        View ev = findViewById(R.id.emptyView);

        bindDependencies();
        ev.getViewTreeObserver().addOnGlobalFocusChangeListener( (view, a2) -> {
            layoutDone = true;
            updatePadding();

        });
        if(ev != null) {
            ev.setVisibility(View.GONE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpBottomSheet();
        setupActionBar();
        setupViewPager();
    }

    private void selectFeature(String id) {
        GeoJsonFeature feature = getById(id);
        bottomSheetBehaviour.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
        skipMove = true;
        details.setSelected(id);
        if(mMap != null) {
            centerMapTo(id);
        }
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.bike_details_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(details, getBaseContext().getResources().getString(R.string.last_observations));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.bike_details_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.bikes_bar_layout).bringToFront();
        toolbar.bringToFront();
    }

    private void bindDependencies() {
        ButterKnife.bind(this);
        if(bottomSheet == null) {
            bottomSheet = findViewById(R.id.bike_details_bottom_sheet);
        }
        if(tapActionLayout == null) {
            tapActionLayout = findViewById(R.id.tap_action_layout);
        }
        if(toolbar == null) {
            toolbar = findViewById(R.id.bike_details_toolbar);
        }
    }
    private void setUpBottomSheet() {
        bottomSheetBehaviour = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);

        bottomSheetBehaviour.setPeekHeight(300);

        bottomSheetBehaviour.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
        bottomSheetBehaviour.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                                updatePadding();
                                break;
                            case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:

                                break;
                            case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                                updatePadding();
                                break;
                            case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                                // note; map resize in this case shold be performed only if the state change is due to a user bottom sheet interaction
                                // when this happens programmatically, updatePadding should be triggered, but the map should not be re-centered
                                // TODO: now it is i
                                updatePadding();
                                break;
                            case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                                updatePadding();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
        });
        tapActionLayout.setOnClickListener((v) -> {
            switch (bottomSheetBehaviour.getState()) {
                case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                    bottomSheetBehaviour.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
            }
        });
    }

    public void updatePadding() {
        if(mMap != null && layoutDone) {
            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            int bottomPadding = getDefaultBottomPadding();
            if( bottomSheetBehaviour.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT
                 // this is a trick to allow to correct center the map anyway if the user selects an item when the
                 // sheet is fully expanded
                 || bottomSheetBehaviour.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED   ) {
                int height = findViewById(R.id.map).getMeasuredHeight();

                bottomPadding = height - getDefaultBottomPadding() - getResources().getDimensionPixelSize(R.dimen.bottom_sheet_anchor_point);

            }
            mMap.setPadding(0, 150, 0, bottomPadding);
            if(skipMove) {
                // when the updatePadding follows a feature selection
                // that trigger map resize but we don't have to adjust it
                skipMove = false;
            } else if(firstRender) {
                mMap.moveCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 10));
                firstRender = false;
            } else {
                mMap.animateCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 10));
            }



        }
    }
    private void loadData() {
        setLoading(true);
        RetrofitClient client = RetrofitClient.getInstance(this);
        SMBRemoteServices portalServices = client.getPortalServices();
        portalServices.getBikeObservations(bike.getShort_uuid()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    geojson = new JSONObject(response.body().string());
                    Bundle args = new Bundle();
                    details.updateListData(geojson);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e(TAG, "Unable to read bikes positions");
                }
                if(bike != null && geojson != null) {
                    displayData();
                } else {
                    showNoData(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setLoading(false);
                showNoData(t instanceof SocketTimeoutException ? "timeout" : null);

            }
        });
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
        if(geojson != null) {
            View view = findViewById(R.id.session_row);
            inflateBikeDataToRecordView(bike, view);
            // update map
            if(mMap != null) {
                if(layoutDone) {
                    mMap.setPadding(0, 150, 0, getDefaultBottomPadding());
                }
                layer = new GeoJsonLayer(mMap, geojson);

                LatLngBounds.Builder builder = LatLngBounds.builder();
                Iterable<GeoJsonFeature> features = layer.getFeatures();
                GeoJsonFeature last = getLastFeature();
                for(GeoJsonFeature f: layer.getFeatures()) {
                    // calculate bounding box
                    builder.include(((GeoJsonPoint) f.getGeometry()).getCoordinates());

                    // create popup
                    String address = null;
                    String observed_at = null;
                    String reporter_name = null;
                    ArrayList<String> snipplets = new ArrayList<String>();
                    if(f.hasProperty(REPORTER_NAME) && !"".equals(f.getProperty(REPORTER_NAME))) {
                        reporter_name = f.getProperty(REPORTER_NAME);
                        if(reporter_name != null) {
                            snipplets.add(reporter_name);
                        }
                    }
                    if (f.hasProperty(ADDRESS) && !"".equals(f.getProperty(ADDRESS))) {
                        address = f.getProperty(ADDRESS);
                        if(address != null) {
                            snipplets.add(address);
                        }
                    }
                    if (f.hasProperty(OBSERVED_AT) && !"".equals(f.getProperty(OBSERVED_AT))) {
                        observed_at = f.getProperty(OBSERVED_AT);
                    }

                    GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                    if(observed_at != null) {
                        String dateFormatted = DateTimeFormat.forPattern("dd MMM, 'ore' HH:mm").print(new DateTime((observed_at)));
                        pointStyle.setTitle(dateFormatted);
                    }
                    pointStyle.setSnippet(TextUtils.join(", ", snipplets));
                    f.setPointStyle(pointStyle);
                }
                layer.addLayerToMap();
                layer.setOnFeatureClickListener((feature) -> {
                    selectFeature(feature.getId());
                });
                // zoom to bounding box
                try {
                    LatLngBounds bounds = builder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
                } catch(IllegalStateException e) {
                  Log.w(TAG, "No observation");

                    showNoData("noPoints");
                }finally {
                    setLoading(false);
                }

            }
        }
    }

    public void inflateBikeDataToRecordView(Bike bike, View view) {
        if(bike.getNickname() != null) {
            bikeName.setText(bike.getNickname());
        }
    }

    /**
     * Creates the (Geo)JSONObject required by the GeoJsonLayer
     * @return the JSONObject
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
        // NO action needed for this handler
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
    public void centerMapTo(String id) {
        if(layer != null & mMap != null) {
            for(GeoJsonFeature f : layer.getFeatures()) {
                if(id == f.getId()) {
                    GeoJsonPoint p = (GeoJsonPoint) f.getGeometry();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p.getCoordinates(), 14));
                }
            };
        }
    }
    private GeoJsonFeature getLastFeature() {
        GeoJsonFeature last = null;
        if(layer != null & mMap != null) {
            for(GeoJsonFeature f : layer.getFeatures()) {
                if(last == null || f.getProperty(PROPERTY_OBSERVED_AT).compareTo(last.getProperty(PROPERTY_OBSERVED_AT)) < 0) {
                    last = f;
                }
            }
        }
        return last;
    }
    private GeoJsonFeature getById(String id) {
        GeoJsonFeature last = null;
        if(layer != null & mMap != null) {
            for(GeoJsonFeature f : layer.getFeatures()) {
                if(f.getId() == id) {
                    return f;
                }
            }
        }
        return null;
    }

    @Override
    public void onRequestPermissionGrant(PermissionIntent permissionIntent) {
        // nothing to do, this activity don't require permission.
    }

    public int getDefaultBottomPadding() {
        return getSupportActionBar().getHeight();
    }
}