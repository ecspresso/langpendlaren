package langpendlaren.trafikverket.json;

import java.time.ZonedDateTime;
import java.util.UUID;

public class TrainAnnouncement {
    private UUID activityId;
    private ActivityType activityType;
    private boolean advertised;
    private ZonedDateTime advertisedTimeAtLocation;
    private String advertisedTrainIdent;
    private boolean canceled;
    private boolean deleted;
    private String informationOwner;
    private String locationSignature;
    private ZonedDateTime modifiedTime;
    private ZonedDateTime scheduledDepartureDateTime;
    private String technicalTrainIdent;
    private ZonedDateTime timeAtLocation;

    public UUID getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = UUID.fromString(activityId);
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        switch(activityType) {
            case "Ankomst" -> this.activityType = ActivityType.ANKOMST;
            case "Avgang" -> this.activityType = ActivityType.AVGANG;
        }
    }

    public boolean isAdvertised() {
        return advertised;
    }

    public void setAdvertised(boolean advertised) {
        this.advertised = advertised;
    }

    public ZonedDateTime getAdvertisedTimeAtLocation() {
        return advertisedTimeAtLocation;
    }

    public void setAdvertisedTimeAtLocation(String advertisedTimeAtLocation) {
        this.advertisedTimeAtLocation = ZonedDateTime.parse(advertisedTimeAtLocation);
    }

    public String getAdvertisedTrainIdent() {
        return advertisedTrainIdent;
    }

    public void setAdvertisedTrainIdent(String advertisedTrainIdent) {
        this.advertisedTrainIdent = advertisedTrainIdent;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getInformationOwner() {
        return informationOwner;
    }

    public void setInformationOwner(String informationOwner) {
        this.informationOwner = informationOwner;
    }

    public String getLocationSignature() {
        return locationSignature;
    }

    public void setLocationSignature(String locationSignature) {
        this.locationSignature = locationSignature;
    }

    public ZonedDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = ZonedDateTime.parse(modifiedTime);
    }

    public ZonedDateTime getScheduledDepartureDateTime() {
        return scheduledDepartureDateTime;
    }

    public void setScheduledDepartureDateTime(String scheduledDepartureDateTime) {
        this.scheduledDepartureDateTime = ZonedDateTime.parse(scheduledDepartureDateTime);
    }

    public String getTechnicalTrainIdent() {
        return technicalTrainIdent;
    }

    public void setTechnicalTrainIdent(String technicalTrainIdent) {
        this.technicalTrainIdent = technicalTrainIdent;
    }

    public ZonedDateTime getTimeAtLocation() {
        return timeAtLocation;
    }

    public void setTimeAtLocation(String timeAtLocation) {
        this.timeAtLocation = ZonedDateTime.parse(timeAtLocation);
    }
}
