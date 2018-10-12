package it.geosolutions.savemybike.model.user;

import com.google.gson.annotations.SerializedName;

import it.geosolutions.savemybike.model.EmissionData;

/**
 * Extends User with the information about badges and results
 */
public class UserInfo extends User {
    @SerializedName("total_emissions")
    EmissionData totalEmissions;

    public EmissionData getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(EmissionData totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

}
