package langpendlaren.api.trafikverket.json.stationshortnames;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the marshalled reply form the server
 * for the endpoint "/trafikverket/stations".
 */
public class StationShortNames {
    /**
     * A list of all the Stations.
     */
    private List<Station> stations = new LinkedList<>();

    /**
     * Extracts all Stations from StationShortNamesJson.Station and saves the
     * values AdvertisedLocationName and LocationSignature.
     * @param json The object to extract data from.
     */
    public void addStations(StationShortNamesJson json) {
        List<StationShortNamesJson.Station> stations = json.getResponse().getTrainStation().get(0).getStations();
        for(StationShortNamesJson.Station station : stations) {
            this.stations.add(new Station(
                    station.getAdvertisedLocationName(),
                    station.getLocationSignature(),
                    station.isPrognosticated()
            ));
        }
    }

    /**
     * Getter method for marshalling.
     * @return A list of all the stations.
     */
    public List<Station> getStations() {
        return stations;
    }

    /**
     * Setter method for marshalling.
     * @param stations A list of stations to save.
     */
    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    /**
     * This class represent each individual Station.
     */
    class Station {
        /**
         * The full name of the station.
         */
        private String name;
        /**
         * The short name of the station, a single or a couple of letters.
         */
        @JsonProperty("short_name")
        private String shortName;
        /**
         * A value of unknown value.
         */
        private boolean prognosticated;

        /**
         * Default marshalling constructor.
         */
        private Station() {}

        /**
         * Shorthand constructor to use instead of setters.
         * @param name Full name of the station.
         * @param shortName Short name of the station, a single to a couple of letters.
         * @param prognosticated True or false, only God knows. (Jag vet att den används i koden men jag vet inte vad den betyder.)
         */
        private Station(String name, String shortName, boolean prognosticated) {
            this.name = name;
            this.shortName = shortName;
            this.prognosticated = prognosticated;
        }

        /**
         * Getter method for marshalling.
         * @return The name of the station.
         */
        public String getName() {
            return name;
        }

        /**
         * Setter method for marshalling.
         * @param name The name of the station.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Getter method for marshalling.
         * @return The short name of the station.
         */
        public String getShortName() {
            return shortName;
        }

        /**
         * Setter method for marshalling.
         * @param shortName The short name of the station.
         */
        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        /**
         * Getter method for marshalling.
         * @return The value of the unknown value...
         */
        public boolean isPrognosticated() {
            return prognosticated;
        }

        /**
         * Setter method for marshalling.
         * @param prognosticated The value of the unknown value...
         */
        public void setPrognosticated(boolean prognosticated) {
            this.prognosticated = prognosticated;
        }
    }
}
