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

        addGet("/api/trains", trafikverketAPI.getTrainStops(1));

        //Avgångar från en viss station, anges genom station signature
        addGet("/api/departures", trafikverketAPI.getDepartures("Cst"));

        //Förslag på en annan fråga för att hämta ut vilka stationer som ett tåg stannar på
        addGet("/api/trains/stops", trafikverketAPI.getTrainStopStation(261));


        addGet("/api/stations", trafikverketAPI.getTrainStops(1));


        // Get play lists
        addGet("/spotify/playlists", spotifyAPI.getCurrentPlayList());
        // Get play list
        addGetById("/spotify/playlist/{id}");
        // Create play list
        addPut("/spotify/createplaylist/{name}");

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
    private void addGetById(String path) {
        javalin.get(path, context -> {
            if (path.contains("spotify")) {
                String playListId = context.pathParam("id");
                this.spotifyAPI.getPlayList(playListId);
            }
        });
    }

    private void addPut(String path) {
        javalin.put(path, context -> {
            String playListName = context.pathParam("name");
            String playListDesc = context.pathParam("desc");
            spotifyAPI.createPlayList(playListName, playListDesc);
        });
    }
}
