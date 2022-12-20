package langpendlaren.api.trafikverket.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

public class StationShortNames {
    @JsonProperty("RESPONSE")
    private Response response;

    private final HashMap<String, String> map = new HashMap<>();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;

        List<Station> stations = this.response.trainStation.get(0).stations;
        for(Station station : stations) {
            map.put(station.advertisedLocationName, station.locationSignature);
        }
    }

    public String getLocationSignature(String AdvertisedLocationName) {
        return map.get(AdvertisedLocationName);
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
