package langpendlaren.webserver;

import io.javalin.Javalin;
import langpendlaren.api.JSON;
import langpendlaren.api.spotify.SpotifyAPI;
import langpendlaren.api.trafikverket.TrafikverketAPI;

public class WebServer {
    private final Javalin javalin;
    private final TrafikverketAPI trafikverketAPI;
    private final SpotifyAPI spotifyAPI;

    public WebServer() {
        javalin = Javalin.create();
        trafikverketAPI = new TrafikverketAPI();
        spotifyAPI = new SpotifyAPI();

        addGet("/api/stations", trafikverketAPI.getTrainStops(1));
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
}
