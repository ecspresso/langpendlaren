package langpendlaren.api.trafikverket;

import io.javalin.Javalin;

public class TrafikverketEndPoints {
    private Javalin javalin;
    private final TrafikverketAPI trafikverketAPI;

    public TrafikverketEndPoints(Javalin javalin){
        this.javalin = javalin;
        trafikverketAPI = new TrafikverketAPI();
        endPoints();
    }

    public void endPoints(){
        // Hämtar alla stationer
        javalin.get("/trafikverket/stations", context -> context.json(trafikverketAPI.getStationNames()));

        // Avgångar från en viss station, anges genom station signature
        javalin.get("/trafikverket/departures/{sign}", context -> context.json(trafikverketAPI.getDepartures(context.pathParam("sign"))));

        // Hämta ut vilka stationer som ett tåg stannar på
        javalin.get("/trafikverket/trains/stops/{trainIdent}", context -> context.json(trafikverketAPI.getTrainStopStation(context.pathParam("trainIdent"))));
    }
}
