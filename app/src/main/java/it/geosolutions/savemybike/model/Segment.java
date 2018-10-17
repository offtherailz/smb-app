package it.geosolutions.savemybike.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Lorenzo Natali, GeoSolutions s.a.s.
 * Segment object from SMB REST API
 */
public class Segment {
    private int id;
    // geom	string

    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;

    @SerializedName("vehicle_type")
    private String veihicleType;

    private int sessionId;

    private String geom;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
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

    public String getVeihicleType() {
        return veihicleType;
    }

    public void setVeihicleType(String veihicleType) {
        this.veihicleType = veihicleType;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public void setVeihicleTypeEnum(Vehicle.VehicleType vehicleType) {
        switch (vehicleType) {
            case BIKE:
                this.veihicleType = Vehicle.StringTypes.BIKE;
                break;
            case BUS:
                this.veihicleType = Vehicle.StringTypes.BUS;
                break;
            case CAR:
                this.veihicleType = Vehicle.StringTypes.CAR;
                break;
            case FOOT:
                this.veihicleType = Vehicle.StringTypes.FOOT;
                break;
            case MOPED:
                this.veihicleType = Vehicle.StringTypes.MOPED;
                break;
            case TRAIN:
                this.veihicleType = Vehicle.StringTypes.TRAIN;
                break;
        }
    }
    public Vehicle.VehicleType getVeihicleTypeEnum() {
        switch (this.veihicleType) {
            case Vehicle.StringTypes.BIKE:
                return Vehicle.VehicleType.BIKE;

            case Vehicle.StringTypes.BUS:
                return Vehicle.VehicleType.BUS;

            case Vehicle.StringTypes.CAR:
                return Vehicle.VehicleType.CAR;

            case Vehicle.StringTypes.FOOT:
                return Vehicle.VehicleType.FOOT;

            case Vehicle.StringTypes.MOPED:
                return Vehicle.VehicleType.MOPED;

            case Vehicle.StringTypes.TRAIN:
                return Vehicle.VehicleType.TRAIN;

        }
        return null;
    }

    // ...url, track, vehicle_id, emissions, costs, health

}
