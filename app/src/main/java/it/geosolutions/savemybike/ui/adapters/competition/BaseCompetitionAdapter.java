package it.geosolutions.savemybike.ui.adapters.competition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.model.competition.Competition;
import it.geosolutions.savemybike.model.competition.Prize;

/**
 * Base adapter for competitions
 */
public class BaseCompetitionAdapter extends ArrayAdapter<Competition> {

    protected int resource;
    static class ViewHolder {
        @BindView(R.id.header) View header;
        @BindView(R.id.item_prize) View view;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.subtitle) TextView subtitle;
        @BindView(R.id.prize_image) ImageView icon;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public BaseCompetitionAdapter(final Context context, int textViewResourceId, List<Competition> badges){
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
        Competition competition = getItem(position);
        // setup view

        if(competition != null) {
            // TODO
            holder.title.setText(competition.getName());

            // holder.icon.setImageResource(R.drawable.img_ctt_pisa);
        }

        return view;
    }


}