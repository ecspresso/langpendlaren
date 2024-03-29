package langpendlaren.api.trafikverket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import langpendlaren.api.http.ErrorHandler;
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
            System.out.println("hämtar stationer");
            try {
                String json = trafikverketAPI.getStationNames();
                StationShortNamesJson shortNamesJson = mapper.readValue(json, StationShortNamesJson.class);
                StationShortNames shortNames = new StationShortNames();
                shortNames.addStations(shortNamesJson);
                context.json(shortNames);
            } catch(IOException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
            System.out.println("Klar");
        });

        // Avgångar från en viss station, anges genom station signature
        javalin.get("/trafikverket/departures/{sign}", context -> {
            System.out.println("Hämtar avgångar");
            try {
                String json = trafikverketAPI.getDepartures(context.pathParam("sign"));
                DeparturesJson departuresJson = mapper.readValue(json, DeparturesJson.class);
                Departures departures = new Departures();
                departures.addAnnouncements(departuresJson);
                context.json(departures);
            } catch(IOException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
            System.out.println("Klar");
        });

        // Hämta ut vilka stationer som ett tåg stannar på
        javalin.get("/trafikverket/trains/stops/{trainIdent}", context -> {
            System.out.println("Hämtar vilka stationer tåget stannar på.");
            try {
                String json = trafikverketAPI.getTrainStopStation(context.pathParam("trainIdent"));
                StopsJson stopsJson = mapper.readValue(json, StopsJson.class);
                Stops stops = new Stops();
                stops.addAnnouncements(stopsJson);
                context.json(stops);
            } catch(IOException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
            System.out.println("Klar");
        });
    }
}
