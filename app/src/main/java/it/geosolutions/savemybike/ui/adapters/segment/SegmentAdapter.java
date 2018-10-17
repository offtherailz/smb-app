package it.geosolutions.savemybike.ui.adapters.segment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.model.Segment;
import it.geosolutions.savemybike.ui.VehicleUtils;


/**
 * adapter for Badges
 */
public class SegmentAdapter extends ArrayAdapter<Segment> {

    private int resource;

    static class ViewHolder {
        @BindView(R.id.item_line) View mItemLine;
        @BindView(R.id.vehicle_icon)
        ImageView vehicleIcon;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public SegmentAdapter(final Context context, int textViewResourceId, List<Segment> segments) {
        super(context, textViewResourceId, segments);

        resource = textViewResourceId;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        SegmentAdapter.ViewHolder holder;
        if (view != null) {
            holder = (SegmentAdapter.ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            holder = new SegmentAdapter.ViewHolder(view);
            view.setTag(holder);
        }
        Segment segment = getItem(position);
        // setup view
        if (segment != null) {
            holder.vehicleIcon.setImageResource(VehicleUtils.getDrawableForVeichle(segment.getVeihicleType()));
            if(position == 0) {
                holder.mItemLine.setBackgroundResource(R.drawable.line_bg_top);
            } else if( position == getCount() - 1) {
                holder.mItemLine.setBackgroundResource(R.drawable.line_bg_bottom);
            } else {
                holder.mItemLine.setBackgroundResource(R.drawable.line_bg_middle);
            }
        }


        return view;
    }
}
