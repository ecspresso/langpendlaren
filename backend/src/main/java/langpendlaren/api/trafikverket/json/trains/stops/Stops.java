package langpendlaren.api.trafikverket.json.trains.stops;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the marshalled reply form the server
 * for the endpoint "/trafikverket/trains/stops/{id}".
 */
public class Stops {
    /**
     * A list of all the announcements.
     */
    private List<StationStops> stationStops = new LinkedList<>();

    public void addAnnouncements(StopsJson json) {
        List<StopsJson.Announcement> announcements = json.getResponse().getTrainAnnouncement().get(0).getAnnouncements();
        for(StopsJson.Announcement announcement : announcements) {
            this.stationStops.add(new StationStops(
                    announcement.getAdvertisedTimeAtLocation(),
                    announcement.getLocationSignature()
            ));
        }
    }

    /**
     * Getter method for marshalling.
     * @return A list of all the stops.
     */
    public List<StationStops> getStationStops() {
        return stationStops;
    }

    /**
     * Setter method for marshalling.
     * @param stationStops A list of all the stops to save.
     */
    public void setStationStops(List<StationStops> stationStops) {
        this.stationStops = stationStops;
    }

    /**
     * This class represents the individual announcements.
     */
    static class StationStops {
        /**
         * The property holds the value of advertised_time_at_location.
         */
        @JsonProperty("advertised_time_at_location")
        private LocalDateTime advertisedTimeAtLocation;
        /**
         * The property holds the value of location_signature.
         */
        @JsonProperty("location_signature")
        private String locationSignature;

        /**
         * Default marshalling constructor.
         */
        public StationStops() {}

        public StationStops(LocalDateTime advertisedTimeAtLocation, String locationSignature) {
            this.advertisedTimeAtLocation = advertisedTimeAtLocation;
            this.locationSignature = locationSignature;
        }

        /**
         * Getter method for marshalling.
         * @return The value of advertised_time_at_location.
         */
        public LocalDateTime getAdvertisedTimeAtLocation() {
            return advertisedTimeAtLocation;
        }

        /**
         * Setter method for marshalling.
         * @param advertisedTimeAtLocation The value of advertised_time_at_location as LocalDateTime.
         */
        public void setAdvertisedTimeAtLocation(LocalDateTime advertisedTimeAtLocation) {
            this.advertisedTimeAtLocation = advertisedTimeAtLocation;
        }

        /**
         * Setter method for marshalling.
         * @param advertisedTimeAtLocation The value of advertised_time_at_location as String.
         */
        public void setAdvertisedTimeAtLocation(String advertisedTimeAtLocation) {
            this.advertisedTimeAtLocation = LocalDateTime.parse(advertisedTimeAtLocation, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        /**
         * Getter method for marshalling.
         * @return The value of location_signature.
         */
        public String getLocationSignature() {
            return locationSignature;
        }

        /**
         * Setter method for marshalling.
         * @param locationSignature The value of location_signature.
         */
        public void setLocationSignature(String locationSignature) {
            this.locationSignature = locationSignature;
        }
    }
}
