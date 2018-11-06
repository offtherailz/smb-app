package it.geosolutions.savemybike.model;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class Observation {
    public String id;
    public String observedAt;
    public Observation(JSONObject obj) {
        try {
            id = obj.getString("id");
            JSONObject properties = obj.getJSONObject("properties");
            if (properties != null) {
                observedAt = obj.getString("observed_at");
            }
        } catch (JSONException e) {
            Log.e("Observation", "can not parse json object for observation");
        }
    }

}
