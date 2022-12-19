package langpendlaren.webserver;

import io.javalin.Javalin;
import langpendlaren.api.JSON;
import langpendlaren.api.spotify.SpotifyAPI;
import langpendlaren.api.trafikverket.TrafikverketAPI;

import java.io.IOException;
import java.net.MalformedURLException;

public class WebServer {
    private final Javalin javalin;
    private final TrafikverketAPI trafikverketAPI;
    private final SpotifyAPI spotifyAPI;

    public WebServer() {
        javalin = Javalin.create();
        trafikverketAPI = new TrafikverketAPI();
        spotifyAPI = new SpotifyAPI();

        addGet("/api/trains", trafikverketAPI.getTrainStops(1));

        //Avgångar från en viss station, anges genom station signature
        addGet("/api/departures", trafikverketAPI.getDepartures("Cst"));

        //Förslag på en annan fråga för att hämta ut vilka stationer som ett tåg stannar på
        addGet("/api/trains/stops", trafikverketAPI.getTrainStopStation(261));


        addGet("/api/stations", trafikverketAPI.getTrainStops(1));
        addAuthorize("/api/spotify/authorize", spotifyAPI.authorize());
    }

    public void run() {
        javalin.start(80);
    }

    private void addGet(String path, JSON json) {
        javalin.get(path, context -> context.json(json));
    }

    private void addGet(String path, String text) {
        javalin.get(path, context -> context.json(text));
    }
    private void addAuthorize(String path, String text) {
        javalin.get(path, context -> context.json(text));
    }
}
