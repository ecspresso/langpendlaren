package langpendlaren.api.trafikverket.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StationShortNamesJson {
    @JsonProperty("RESPONSE")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        @JsonProperty("RESULT")
        private List<TrainStation> trainStation;

        public List<TrainStation> getTrainStation() {
            return trainStation;
        }

        public void setTrainStation(List<TrainStation> trainStation) {
            this.trainStation = trainStation;
        }
    }

    private static class TrainStation {
        @JsonProperty("TrainStation")
        private List<Station> stations;

        public List<Station> getStations() {
            return stations;
        }

        public void setStations(List<Station> stations) {
            this.stations = stations;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Station {
        @JsonProperty("AdvertisedLocationName")
        private String advertisedLocationName;
        @JsonProperty("LocationSignature")
        private String locationSignature;

        public String getAdvertisedLocationName() {
            return advertisedLocationName;
        }

        public void setAdvertisedLocationName(String advertisedLocationName) {
            this.advertisedLocationName = advertisedLocationName;
        }

        public String getLocationSignature() {
            return locationSignature;
        }

        public void setLocationSignature(String locationSignature) {
            this.locationSignature = locationSignature;
        }
    }
}
