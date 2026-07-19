package model;

import java.sql.Timestamp;

public class Race {
    private int raceId;
    private int createdByUserId;
    private Integer approvedByUserId; // Có thể null
    private String raceName;
    private String description;
    private Timestamp createdDate;
    private Timestamp approvedDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp registrationDeadline;
    private String location;
    private String status; // PENDING, APPROVED, REJECTED, v.v.

    public Race() {}

    // Cung cấp các Getter và Setter cần thiết
    public int getRaceId() { return raceId; }
    public void setRaceId(int raceId) { this.raceId = raceId; }
    public int getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(int createdByUserId) { this.createdByUserId = createdByUserId; }
    public Integer getApprovedByUserId() { return approvedByUserId; }
    public void setApprovedByUserId(Integer approvedByUserId) { this.approvedByUserId = approvedByUserId; }
    public String getRaceName() { return raceName; }
    public void setRaceName(String raceName) { this.raceName = raceName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
