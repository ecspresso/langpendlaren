package langpendlaren.api.spotify;


import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.net.URISyntaxException;

public class SpotifyEndPoints {
    private final Javalin javalin;
    private final SpotifyAPI spotifyAPI;

    public SpotifyEndPoints(Javalin javalin){
        this.javalin = javalin;
        try {
            spotifyAPI = new SpotifyAPI();
        } catch(URISyntaxException e) {
            throw new RuntimeException(e);
        }

        endPoints();
    }

    /**
     * All end points will be handled in this function
     */
    public void endPoints(){
        /*
         * Skickar Spotifys auth url.
         */
        javalin.get("/spotify/login", context -> context.json(spotifyAPI.getLoginPage()));

        /*
        * "Skickar" access token och refresh token som kakor till användaren.
        * Kräver kod från Spotify (fås från /spotify/login).
         */
        javalin.get("/spotify/authenticated", context -> {
            String code = context.queryParam("code");

            try {
                AuthorizationCodeCredentials tokens = spotifyAPI.auth(code);
                context.cookie("accessToken", tokens.getAccessToken(), tokens.getExpiresIn());
                context.cookie("refreshToken", tokens.getRefreshToken());
            } catch (IOException e) {
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json("{'error': 'IOException'}");
            } catch (ParseException e) {
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json("{'error': 'ParseException'}");
            } catch (SpotifyWebApiException e) {
                context.status(HttpStatus.INTERNAL_SERVER_ERROR);
                context.json("{'error': 'SpotifyWebApiException'}");
            }
        });

        /*
        * Hämta en lista av alla genrer seeds.
         */
        javalin.get("/spotify/genre/seeds", context -> {
            String[] seeds = spotifyAPI.genreSeeds();
            context.json(seeds);
        });

        // -- User
        javalin.get("/spotify/user/profile", context -> context.json(spotifyAPI.getUserId())); //FIXME! a possible way can be to return all profile

        // -- Playlist
        javalin.post("/spotify/playlist/create/{name}/{des}", context -> {
            String name = context.pathParam("name");
            String des = context.pathParam("des");

            context.result(spotifyAPI.createPlayList(getAccessToken(context), name, des));
        });
        javalin.post("/spotify/playlist/delete/{id}", context -> {
            String id = context.pathParam("id");
            spotifyAPI.deletePlayList(id);
        });
        javalin.put("/spotify/playlist/add/{pid}/{tid}", context -> {
            String pId = context.pathParam("pid");
            String tId = context.pathParam("tid");
            context.result(spotifyAPI.addToPlayList(pId, tId));
        });
        javalin.put("/spotify/playlist/deletetracks/{pid}/{tids}", context -> {
            String pId = context.pathParam("pid");
            String tIds = context.pathParam("tids");
            context.result(spotifyAPI.removeItemFromPlayList(getAccessToken(context), pId, tIds));
        });

        // -- Albums
        javalin.get("/spotify/album/{id}", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getAlbumById(id));
        });
        javalin.get("/spotify/album/{ids}", context ->{
            String ids = context.pathParam("ids");
            context.json(spotifyAPI.getAlbums(ids));
        });

        // -- Search
        javalin.get("/spotify/search/track/{name}", context -> {
            String name = context.pathParam("name");
            context.json(spotifyAPI.searchTracks(name));
        });

        // -- Artists
        javalin.get("/spotify/artist/profile/{id}", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getArtist(id));
        });
    }

    private String getAccessToken(Context context) {
        // TODO Kontrollera kaka och förnya vid behov.
        return context.cookie("accessToken");
    }
}
