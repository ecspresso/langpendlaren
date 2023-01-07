package langpendlaren.api.trafikverket.json.trains.stops;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This class represents the unmarshalled reply from Trafikverket
 * for the endpoint "QUERY objecttype='TrainAnnouncement'".
 */
public class StopsJson {
    /**
     * The property RESPONSE.
     */
    @JsonProperty("RESPONSE")
    private Response response;

    /**
     * Getter method for unmarshalling.
     * @return A RESPONSE object with nested data.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Setter method for unmarshalling.
     * @param response Data from JSON string representing the RESPONSE object.
     */
    public void setResponse(Response response) {
        this.response = response;
    }


    /**
     * This class represents the RESPONSE object.
     */
    static class Response {
        /**
         * The property holds the array RESULT with the corresponding TrainAnnouncement objects.
         */
        @JsonProperty("RESULT")
        private List<TrainAnnouncement> TrainAnnouncement;

        /**
         * Getter method for unmarshalling.
         * @return A list of TrainAnnouncements.
         */
        public List<TrainAnnouncement> getTrainAnnouncement() {
            return TrainAnnouncement;
        }

        /**
         * Setter method for unmarshalling.
         * @param trainAnnouncement Data from the JSON string representing the array with TrainAnnouncements.
         */
        public void setTrainAnnouncement(List<TrainAnnouncement> trainAnnouncement) {
            TrainAnnouncement = trainAnnouncement;
        }
    }

    /**
     * This class represents the TrainAnnouncement object.
     */
    static class TrainAnnouncement {
        /**
         * The property holds the array TrainAnnouncement with the corresponding Announcement objects.
         */
        @JsonProperty("TrainAnnouncement")
        private List<Announcement> announcements;

        /**
         * Getter method for unmarshalling.
         * @return A list of Announcements.
         */
        public List<Announcement> getAnnouncements() {
            return announcements;
        }

        /**
         * Setter method for unmarshalling.
         * @param announcements Data from the JSON string representing the Announcement object.
         */
        public void setAnnouncements(List<Announcement> announcements) {
            this.announcements = announcements;
        }
    }

    /**
     * This class represents the (unnamed) Announcement object.
     */
    static class Announcement {
        /**
         * The property holds the value of AdvertisedTimeAtLocation.
         */
        @JsonProperty("AdvertisedTimeAtLocation")
        private LocalDateTime advertisedTimeAtLocation;
        /**
         * The property holds the value of AdvertisedTimeAtLocation.
         */
        @JsonProperty("LocationSignature")
        private String locationSignature;

        /**
         * Getter method for unmarshalling.
         * @return The value of AdvertisedTimeAtLocation.
         */
        public LocalDateTime getAdvertisedTimeAtLocation() {
            return advertisedTimeAtLocation;
        }

        /**
         * Setter method for unmarshalling.
         * @param advertisedTimeAtLocation The value of AdvertisedTimeAtLocation.
         */
        public void setAdvertisedTimeAtLocation(String advertisedTimeAtLocation) {
            this.advertisedTimeAtLocation = LocalDateTime.parse(advertisedTimeAtLocation, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        /**
         * Getter method for unmarshalling.
         * @return The value of LocationSignature.
         */
        public String getLocationSignature() {
            return locationSignature;
        }

        /**
         * Setter method for unmarshalling.
         * @param locationSignature The value of LocationSignature.
         */
        public void setLocationSignature(String locationSignature) {
            this.locationSignature = locationSignature;
        }
    }
}
