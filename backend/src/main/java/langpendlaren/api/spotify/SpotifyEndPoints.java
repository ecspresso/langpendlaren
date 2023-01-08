package langpendlaren.api.spotify;


import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.ErrorHandler;
import langpendlaren.api.http.json.error.Error;
import langpendlaren.api.spotify.json.loginpage.LoginPage;
import langpendlaren.api.spotify.json.seeds.Seeds;
import langpendlaren.api.spotify.json.tokens.Tokens;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.net.URI;
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
        javalin.get("/spotify/login", context -> {
            URI uri = spotifyAPI.getLoginPage();
            LoginPage json = new LoginPage();
            json.setUri(uri);
            context.json(json);
        });

        /*
        * "Skickar" access token och refresh token som kakor till användaren.
        * Kräver kod från Spotify (fås från /spotify/login).
         */
        javalin.get("/spotify/authenticated", context -> {
            String code = context.queryParam("code");

            try {
                AuthorizationCodeCredentials tokens = spotifyAPI.auth(code);
                Tokens tokensJson = new Tokens(tokens.getAccessToken(), tokens.getExpiresIn(), tokens.getRefreshToken());
                context.json(tokensJson);
            } catch (IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        /*
        * Hämta en lista av alla genrer seeds.
         */
        javalin.get("/spotify/genre/seeds", context -> {
            try {
                String[] seeds = spotifyAPI.genreSeeds();
                Seeds json = new Seeds();
                json.setSeeds(seeds);
                context.json(json);
            } catch(UnauthorizedException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        // -- User
        javalin.get("/spotify/user/profile/{accessToken}", context -> {
            String accessToken = context.pathParam("accessToken");
            context.json(spotifyAPI.getUserId(accessToken));
        }); //FIXME! a possible way can be to return all profile

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
