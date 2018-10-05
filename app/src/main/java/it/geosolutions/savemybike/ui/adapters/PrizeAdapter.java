package it.geosolutions.savemybike.ui.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.model.Badge;
import it.geosolutions.savemybike.model.Prize;

/**
 * adapter for Badges
 */
public class PrizeAdapter extends ArrayAdapter<Prize> {

    private	int resource;
    static class ViewHolder {
        @BindView(R.id.item_badge) View view;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.badge_icon) ImageView icon;
        @BindView(R.id.icon_background) View iconBackground;
        @BindView(R.id.level) LinearLayout level;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public PrizeAdapter(final Context context, int textViewResourceId, List<Prize> badges){
        super(context, textViewResourceId, badges);

        resource = textViewResourceId;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Prize prize = getItem(position);
        // setup view
        if(prize != null) {
            // TODO
            holder.title.setText("TO DO");
        }

        return view;
    }


}