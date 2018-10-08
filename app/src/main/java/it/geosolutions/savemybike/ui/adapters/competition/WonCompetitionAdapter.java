package it.geosolutions.savemybike.ui.adapters.competition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.geosolutions.savemybike.model.competition.Competition;

/**
 * Adapter to display won competitions
 */
public class WonCompetitionAdapter extends BaseCompetitionAdapter {
    public WonCompetitionAdapter(Context context, int textViewResourceId, List<Competition> badges) {
        super(context, textViewResourceId, badges);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View contentView, @NonNull ViewGroup parent) {
        View view = super.getView(position, contentView, parent);
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.subtitle.setVisibility(View.GONE);
        holder.header.setVisibility(View.VISIBLE);
        return view;
    }
}
