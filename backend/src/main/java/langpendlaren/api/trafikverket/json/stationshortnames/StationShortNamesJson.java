package langpendlaren.api.trafikverket.json.stationshortnames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * This class represents the unmarshalled reply from Trafikverket
 * for the endpoint "QUERY objecttype='TrainStation'".
 */
public class StationShortNamesJson {
    /**
     * The property RESPONSE.
     */
    @JsonProperty("RESPONSE")
    private Response response;

    /**
     * Getter method for unmarshalling.
     * @return A response object with nested data.
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
         * The property holds the array RESULT with the corresponding TrainStations objects.
         */
        @JsonProperty("RESULT")
        private List<TrainStation> trainStation;

        /**
         * Getter method for unmarshalling.
         * @return A list of TrainStations.
         */
        public List<TrainStation> getTrainStation() {
            return trainStation;
        }

        /**
         * Setter method for unmarshalling.
         * @param trainStation Data from the JSON string representing the array with TrainStations.
         */
        public void setTrainStation(List<TrainStation> trainStation) {
            this.trainStation = trainStation;
        }
    }

    /**
     * This class represents the TrainStation object.
     */
    static class TrainStation {
        /**
         * The property holds the array TrainStation with the corresponding Station objects.
         */
        @JsonProperty("TrainStation")
        private List<Station> stations;

        /**
         * Getter method for unmarshalling.
         * @return A list of Stations.
         */
        public List<Station> getStations() {
            return stations;
        }

        /**
         * Setter method for unmarshalling.
         * @param stations Data from the JSON string representing the (unnamed) Station object.
         */
        public void setStations(List<Station> stations) {
            this.stations = stations;
        }
    }

    /**
     * This class represents the (unnamed) Station object.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Station {
        /**
         * This property holds the value of AdvertisedLocationName.
         */
        @JsonProperty("AdvertisedLocationName")
        private String advertisedLocationName;
        /**
         * This property holds the value of LocationSignature.
         */
        @JsonProperty("LocationSignature")
        private String locationSignature;
        /**
         * This property holds the value of Prognosticated.
         */
        @JsonProperty("Prognosticated")
        private boolean prognosticated;

        /**
         * Getter method for unmarshalling.
         * @return The value of AdvertisedLocationName for this specific Station.
         */
        public String getAdvertisedLocationName() {
            return advertisedLocationName;
        }

        /**
         * Setter method for unmarshalling.
         * @param advertisedLocationName The value of AdvertisedLocationName for this specific Station.
         */
        public void setAdvertisedLocationName(String advertisedLocationName) {
            this.advertisedLocationName = advertisedLocationName;
        }

        /**
         * Getter method for unmarshalling.
         * @return LocationSignature for this specific Station.
         */
        public String getLocationSignature() {
            return locationSignature;
        }

        /**
         * Setter method for unmarshalling.
         * @param locationSignature The value of LocationSignature for this specific Station.
         */
        public void setLocationSignature(String locationSignature) {
            this.locationSignature = locationSignature;
        }

        /**
         * Getter method for unmarshalling.
         * @return Prognosticated for this specific Station.
         */
        public boolean isPrognosticated() {
            return prognosticated;
        }

        /**
         * Setter method for unmarshalling.
         * @param prognosticated The value of Prognosticated for this specific Station.
         */
        public void setPrognosticated(boolean prognosticated) {
            this.prognosticated = prognosticated;
        }
    }
}
