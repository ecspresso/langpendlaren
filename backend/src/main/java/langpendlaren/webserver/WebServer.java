package langpendlaren.webserver;

import io.javalin.Javalin;
import langpendlaren.api.spotify.SpotifyAPI;
import langpendlaren.api.trafikverket.TrafikverketAPI;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;

public class WebServer {
    private final Javalin javalin;
    private final TrafikverketAPI trafikverketAPI;
    private final SpotifyAPI spotifyAPI;

    public WebServer() {
        javalin = Javalin.create();
        trafikverketAPI = new TrafikverketAPI();
        try {
            spotifyAPI = new SpotifyAPI();
        } catch(URISyntaxException e) {
            throw new RuntimeException(e);
        }


        // TRAFIKVERKET //
        // ?
        javalin.get("/trafikverket/trains", context -> context.json(trafikverketAPI.getTrainStops(1)));

        //Avgångar från en viss station, anges genom station signature
        javalin.get("/trafikverket/departures", context -> context.json(trafikverketAPI.getDepartures("Cst")));

        //Förslag på en annan fråga för att hämta ut vilka stationer som ett tåg stannar på
        javalin.get("/trafikverket/trains/stops", context -> context.json(trafikverketAPI.getTrainStopStation(261)));

        // ?
        javalin.get("/trafikverket/stations", context -> context.json(trafikverketAPI.getTrainStops(1)));


        // SPOTIFY //
        // Get play lists
        javalin.get("/spotify/playlists", context -> context.json(spotifyAPI.getCurrentPlayList()));

        // Get play list
        javalin.get("/spotify/playlist/{id}", context -> {
            String playListId = context.pathParam("id");
            this.spotifyAPI.getPlayList(playListId);
        });

        // Create play list
        javalin.put("/spotify/createplaylist/{userId}/{name}", context -> {
            String playListName = context.pathParam("name");
            String playListDesc = context.pathParam("desc");
            String userId = context.pathParam("userId");
            spotifyAPI.createPlayList(userId, playListName, playListDesc);
        });

        javalin.get("/spotify/auth", context -> {
            String code = context.queryParam("code");
            if(code == null) { // Skapa URI för att logga in.
                context.json(spotifyAPI.auth());
            } else { // Returnera koden från Spotify
                try {
                    this.spotifyAPI.getAccessToken(code);
                } catch (IOException | ParseException | SpotifyWebApiException e) {
                    e.printStackTrace();
                }
            }
        });

        // Hämta Spotify user Id
        javalin.get("/spotify/me", context -> {
            spotifyAPI.me();
        });
    }

    public void run() {
        javalin.start(80);
    }
}
