package langpendlaren.api.trafikverket.json.departures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 
 */
public class DeparturesJson {
    @JsonProperty("RESPONSE")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    static class Response {
        @JsonProperty("RESULT")
        private List<TrainAnnouncement> trainAnnouncement;

        public List<TrainAnnouncement> getTrainAnnouncement() {
            return trainAnnouncement;
        }

        public void setTrainAnnouncement(List<TrainAnnouncement> trainAnnouncement) {
            this.trainAnnouncement = trainAnnouncement;
        }
    }

    static class TrainAnnouncement {
        @JsonProperty("TrainAnnouncement")
        private List<Announcement> announcements;

        public List<Announcement> getAnnouncements() {
            return announcements;
        }

        public void setAnnouncements(List<Announcement> announcements) {
            this.announcements = announcements;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Announcement {
        @JsonProperty("AdvertisedTimeAtLocation")
        private String advertisedTimeAtLocation; //: "2023-01-06T22:47:00.000+01:00",
        @JsonProperty("AdvertisedTrainIdent")
        private int advertisedTrainIdent; //: "3587",
        @JsonProperty("FromLocation")
        private List<String> fromLocation; //[ "A" ],
        @JsonProperty("InformationOwner")
        private String informationOwner; //: "Västtrafik",
        @JsonProperty("ToLocation")
        private List<String> toLocation; //"ToLocation": [ "Lr", "G" ],
        @JsonProperty("TrackAtLocation")
        private String trackAtLocation; //": "3"

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
