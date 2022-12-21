package langpendlaren.webserver;

import io.javalin.Javalin;
import langpendlaren.api.spotify.SpotifyAPI;
import langpendlaren.api.trafikverket.TrafikverketAPI;

import java.net.URISyntaxException;

public class WebServer {
    private final Javalin javalin;
    private final TrafikverketAPI trafikverketAPI;
    private final SpotifyAPI spotifyAPI;

    public WebServer() {
        javalin = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        });
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




        // Authorize
        javalin.get("/spotify/login", context -> context.json(spotifyAPI.getLoginAddress()));
        javalin.post("/spotify/auth/{code}", context -> {
            String code = context.pathParam("code");
            spotifyAPI.auth(code);
        });

        // -- User
        javalin.get("/spotify/user/profile", context -> context.json(spotifyAPI.getUserProfile()));
        // -- Albums
        javalin.get("/spotify/album/albums", context -> context.json(spotifyAPI.getAlbums()));
        javalin.get("/spotify/album/{id}", context -> context.json(spotifyAPI.getAlbumById()));
        // -- Playlist
        javalin.post("/spotify/playlist/create/{name}/{des}", context -> spotifyAPI.createPlayList(context.pathParam("name"), context.pathParam("des")));
        javalin.post("/spotify/playlist/delete/{id}", context -> spotifyAPI.deletePlayList(context.pathParam("id")));
        javalin.put("/spotify/playlist/add/{pid}/{tid}", context -> spotifyAPI.addToPlayList(context.pathParam("pid"), context.pathParam("tid")));
        javalin.get("/spotify/playlist/current", context -> context.json(spotifyAPI.getCurrentPlayList()));
        // -- Search
        javalin.get("/spotify/search/{name}", context -> context.json(spotifyAPI.searchItem(context.pathParam("name"))));
        // -- Artists
        javalin.get("/spotify/artist/profile/{id}", context -> context.json(spotifyAPI.getArtist(context.pathParam("id"))));
        javalin.get("/spotify/artist/toptrack", context -> context.json(spotifyAPI.getArtistTopTruck()));
        javalin.get("/spotify/artist/albums/{id}", context -> context.json(spotifyAPI.getArtistAlbums(context.pathParam("id"))));
    }




    public void run() {
        javalin.start(8080);
    }
}
