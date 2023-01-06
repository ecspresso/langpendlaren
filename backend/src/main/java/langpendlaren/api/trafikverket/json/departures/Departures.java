package langpendlaren.api.trafikverket.json.departures;

import com.fasterxml.jackson.annotation.JsonProperty;
import langpendlaren.api.trafikverket.json.stationshortnames.StationShortNamesJson;

import java.util.LinkedList;
import java.util.List;

public class Departures {
    @JsonProperty("train_announcement")
    private List<Announcement> announcement = new LinkedList<>();

    public void addAnnouncements(DeparturesJson json) {
        List<DeparturesJson.Announcement> announcements = json.getResponse().getTrainAnnouncement().get(0).getAnnouncements();
        for(DeparturesJson.Announcement announcement : announcements) {
            this.announcement.add(new Announcement(
                    announcement.getAdvertisedTimeAtLocation(),
                    announcement.getAdvertisedTrainIdent(),
                    announcement.getFromLocation(),
                    announcement.getInformationOwner(),
                    announcement.getToLocation(),
                    announcement.getTrackAtLocation()
            ));
        }
    }

    public List<Announcement> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(List<Announcement> announcement) {
        this.announcement = announcement;
    }

    static class Announcement {
        @JsonProperty("advertised_time_at_location")
        private String advertisedTimeAtLocation; //: "2023-01-06T22:47:00.000+01:00",
        @JsonProperty("advertised_train_id")
        private int advertisedTrainIdent; //: "3587",
        @JsonProperty("from_location")
        private List<String> fromLocation; //[ "A" ],
        @JsonProperty("information_owner")
        private String informationOwner; //: "Västtrafik",
        @JsonProperty("to_location")
        private List<String> toLocation; //"ToLocation": [ "Lr", "G" ],
        @JsonProperty("track_at_location")
        private String trackAtLocation; //": "3"

        /**
         * Default marshalling constructor.
         */
        public Announcement() {}

        public Announcement(String advertisedTimeAtLocation, int advertisedTrainIdent, List<String> fromLocation, String informationOwner, List<String> toLocation, String trackAtLocation) {
            this.advertisedTimeAtLocation = advertisedTimeAtLocation;
            this.advertisedTrainIdent = advertisedTrainIdent;
            this.fromLocation = fromLocation;
            this.informationOwner = informationOwner;
            this.toLocation = toLocation;
            this.trackAtLocation = trackAtLocation;
        }

        public String getAdvertisedTimeAtLocation() {
            return advertisedTimeAtLocation;
        }

        public void setAdvertisedTimeAtLocation(String advertisedTimeAtLocation) {
            this.advertisedTimeAtLocation = advertisedTimeAtLocation;
        }

        public int getAdvertisedTrainIdent() {
            return advertisedTrainIdent;
        }

        public void setAdvertisedTrainIdent(int advertisedTrainIdent) {
            this.advertisedTrainIdent = advertisedTrainIdent;
        }

        public List<String> getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(List<String> fromLocation) {
            this.fromLocation = fromLocation;
        }

        public String getInformationOwner() {
            return informationOwner;
        }

        public void setInformationOwner(String informationOwner) {
            this.informationOwner = informationOwner;
        }

        public List<String> getToLocation() {
            return toLocation;
        }

        public void setToLocation(List<String> toLocation) {
            this.toLocation = toLocation;
        }

        public String getTrackAtLocation() {
            return trackAtLocation;
        }

        public void setTrackAtLocation(String trackAtLocation) {
            this.trackAtLocation = trackAtLocation;
        }
    }
}
