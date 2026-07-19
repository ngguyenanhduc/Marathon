package model;

import java.math.BigDecimal;

public class DistanceKM {

    private int distanceId;
    private int raceId;

    private String raceName;
    private String distanceName;
    private BigDecimal distanceKm;
    private String raceStatus;

    private int maxParticipant;

    private int approvedRegistrationCount;
    private int pendingRegistrationCount;

    public DistanceKM() {
    }

    public int getDistanceId() {
        return distanceId;
    }

    public void setDistanceId(int distanceId) {
        this.distanceId = distanceId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getDistanceName() {
        return distanceName;
    }

    public void setDistanceName(String distanceName) {
        this.distanceName = distanceName;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getRaceStatus() {
        return raceStatus;
    }

    public void setRaceStatus(String raceStatus) {
        this.raceStatus = raceStatus;
    }

    public int getMaxParticipant() {
        return maxParticipant;
    }

    public void setMaxParticipant(int maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    
    
    
    
    public int getApprovedRegistrationCount() {
        return approvedRegistrationCount;
    }

    public void setApprovedRegistrationCount(
            int approvedRegistrationCount) {
        this.approvedRegistrationCount =
                approvedRegistrationCount;
    }

    public int getPendingRegistrationCount() {
        return pendingRegistrationCount;
    }

    public void setPendingRegistrationCount(
            int pendingRegistrationCount) {
        this.pendingRegistrationCount =
                pendingRegistrationCount;
    }

    public boolean isRaceEditable() {
        return "PENDING".equalsIgnoreCase(raceStatus)
                || "REJECTED".equalsIgnoreCase(raceStatus);
    }
}