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
        javalin.get("/trafikverket/trains", context -> context.json(trafikverketAPI.getTrainStops(1)));

        //Avgångar från en viss station, anges genom station signature
        javalin.get("/trafikverket/departures", context -> context.json(trafikverketAPI.getDepartures("Cst")));

        //Förslag på en annan fråga för att hämta ut vilka stationer som ett tåg stannar på
        javalin.get("/trafikverket/trains/stops", context -> context.json(trafikverketAPI.getTrainStopStation(1058)));

        //Hämtar alla stationer
        javalin.get("/trafikverket/stations", context -> context.json(trafikverketAPI.getStationShortNames()));
    }


}
