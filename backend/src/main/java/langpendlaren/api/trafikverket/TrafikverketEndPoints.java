package langpendlaren.api.trafikverket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.json.error.Error;
import langpendlaren.api.trafikverket.json.departures.Departures;
import langpendlaren.api.trafikverket.json.departures.DeparturesJson;
import langpendlaren.api.trafikverket.json.stationshortnames.StationShortNames;
import langpendlaren.api.trafikverket.json.stationshortnames.StationShortNamesJson;
import langpendlaren.api.trafikverket.json.trains.stops.Stops;
import langpendlaren.api.trafikverket.json.trains.stops.StopsJson;

import java.io.IOException;

public class TrafikverketEndPoints {
    private final Javalin javalin;
    private final TrafikverketAPI trafikverketAPI;
    private final ObjectMapper mapper = new ObjectMapper();

    public TrafikverketEndPoints(Javalin javalin){
        this.javalin = javalin;
        trafikverketAPI = new TrafikverketAPI();
        endPoints();
    }

    public void endPoints(){
        // Hämtar alla stationer
        javalin.get("/trafikverket/stations", context -> {
            try {
                String json = trafikverketAPI.getStationNames();
                StationShortNamesJson shortNamesJson = mapper.readValue(json, StationShortNamesJson.class);
                StationShortNames shortNames = new StationShortNames();
                shortNames.addStations(shortNamesJson);
                context.json(shortNames);
            } catch(JsonProcessingException e) {
                e.printStackTrace();
                Error error = new Error();
                error.setErrorMessage("JsonProcessingException", e.getMessage());
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json(error);
            } catch(IOException e) {
                e.printStackTrace();
                Error error = new Error();
                error.setErrorMessage("IOException", e.getMessage());
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json(error);
            }
        });

        // Avgångar från en viss station, anges genom station signature
        javalin.get("/trafikverket/departures/{sign}", context -> {
            try {
                String json = trafikverketAPI.getDepartures(context.pathParam("sign"));
                DeparturesJson departuresJson = mapper.readValue(json, DeparturesJson.class);
                Departures departures = new Departures();
                departures.addAnnouncements(departuresJson);
                context.json(departures);
            } catch(JsonProcessingException e) {
                e.printStackTrace();
                Error error = new Error();
                error.setErrorMessage("JsonProcessingException", e.getMessage());
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json(error);
            } catch(IOException e) {
                e.printStackTrace();
                Error error = new Error();
                error.setErrorMessage("IOException", e.getMessage());
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json(error);
            }
        });

        // Hämta ut vilka stationer som ett tåg stannar på
        javalin.get("/trafikverket/trains/stops/{trainIdent}", context -> {
            try {
                String json = trafikverketAPI.getTrainStopStation(context.pathParam("trainIdent"));
                StopsJson stopsJson = mapper.readValue(json, StopsJson.class);
                Stops stops = new Stops();
                stops.addAnnouncements(stopsJson);
                context.json(stops);
            } catch(JsonProcessingException e) {
                e.printStackTrace();
                Error error = new Error();
                error.setErrorMessage("JsonProcessingException", e.getMessage());
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json(error);
            } catch(IOException e) {
                e.printStackTrace();
                Error error = new Error();
                error.setErrorMessage("IOException", e.getMessage());
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json(error);
            }
        });
    }
}
