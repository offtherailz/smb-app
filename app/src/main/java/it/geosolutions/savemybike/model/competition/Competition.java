package it.geosolutions.savemybike.model.competition;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import it.geosolutions.savemybike.model.competition.Prize;

public class Competition {
    String name;
    String description;
    @SerializedName("start_date")
    String startDate;

    @SerializedName("end_date")
    String endDate;

    ArrayList<Prize> prizes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(ArrayList<Prize> prizes) {
        this.prizes = prizes;
    }
}
