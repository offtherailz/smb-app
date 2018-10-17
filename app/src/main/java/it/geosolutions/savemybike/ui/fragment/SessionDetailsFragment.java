package it.geosolutions.savemybike.ui.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.server.RetrofitClient;
import it.geosolutions.savemybike.data.server.SMBRemoteServices;
import it.geosolutions.savemybike.model.Cost;
import it.geosolutions.savemybike.model.DataPoint;
import it.geosolutions.savemybike.model.EmissionData;
import it.geosolutions.savemybike.model.HealthData;
import it.geosolutions.savemybike.model.Segment;
import it.geosolutions.savemybike.model.Session;
import it.geosolutions.savemybike.model.Track;
import it.geosolutions.savemybike.model.Vehicle;
import it.geosolutions.savemybike.ui.VehicleUtils;
import it.geosolutions.savemybike.ui.activity.SessionDetailsActivity;
import it.geosolutions.savemybike.ui.activity.TrackDetailsActivity;
import it.geosolutions.savemybike.ui.adapters.EmissionAdapter;
import it.geosolutions.savemybike.ui.adapters.IconDataAdapter;
import it.geosolutions.savemybike.ui.adapters.segment.SegmentAdapter;
import it.geosolutions.savemybike.ui.callback.OnFragmentInteractionListener;
import it.geosolutions.savemybike.ui.tasks.GetSessionTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Lorenzo Natali, GeoSolutions S.a.s.
 * TrackDetailsFragment Show track details in the releated fragment
 *
 */
public class SessionDetailsFragment extends Fragment {

    private Session session;
    SegmentAdapter segmentAdapter;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.segment_list) ListView segmentList;

    public SessionDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session_details, container, false);
        ButterKnife.bind(this, view);
        List<Segment> segments = new ArrayList<Segment>();
        segmentAdapter = new SegmentAdapter(getActivity(), R.layout.segment_item, segments);
        segmentList.setAdapter(segmentAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLoading(true);
        Long itemId = (Long) getActivity().getIntent().getExtras().get(SessionDetailsActivity.SESSION_ID);
        new GetSessionTask(getActivity(), new GetSessionTask.SessionCallback() {
            @Override
            public void showProgressView() {
            }
            @Override
            public void hideProgressView() {
            }
            @Override
            public void done(Session s) {
                session = s;
                setLoading(false);
                if (s == null && getActivity() != null) {

                    showNoData();
                } else if (getActivity() != null){
                    session = s;
                    showData();
                }
            }
        }, itemId).execute();
    }


    public void showData() {
        Vehicle.VehicleType currentVehicle = null;
        List<Segment> sl = new ArrayList<>();
        Segment s = new Segment();
        for(DataPoint d : session.getDataPoints()) {
            Vehicle.VehicleType type = Vehicle.VehicleType.values()[d.vehicleMode];
            if(type != currentVehicle) {
                currentVehicle = type;
                s = new Segment();
                s.setVeihicleType(Vehicle.VehicleType.values()[d.vehicleMode].toString().toLowerCase());
                segmentAdapter.add(s);
            }

        }


        segmentAdapter.notifyDataSetChanged();
    }

    public void showNoData() {

    }

    public void setLoading(boolean loading) {
        if(getActivity() != null) {
            View v = getActivity().findViewById(R.id.loading_container);
            if(v != null) {
                v.setVisibility(loading ? View.VISIBLE : View.GONE);
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

