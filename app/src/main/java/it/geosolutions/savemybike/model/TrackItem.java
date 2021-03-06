package it.geosolutions.savemybike.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Lorenzo Natali, GeoSolutions S.a.s.
 * A short version of the track from SMB API. Used in paginated results
 * Has only summarized data, no segments.
 */
public class TrackItem extends BaseTrack {

    private ArrayList<Segment> segments;

    private EmissionData emissions;

    private Cost costs;

    private HealthData health;

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public EmissionData getEmissions() {
        return emissions;
    }

    public void setEmissions(EmissionData emissions) {
        this.emissions = emissions;
    }

    public Cost getCosts() {
        return costs;
    }

    public void setCosts(Cost costs) {
        this.costs = costs;
    }

    public HealthData getHealth() {
        return health;
    }

    public void setHealth(HealthData health) {
        this.health = health;
    }

}
