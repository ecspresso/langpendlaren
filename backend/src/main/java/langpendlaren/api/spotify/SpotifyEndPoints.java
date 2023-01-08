package langpendlaren.api.spotify;


import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.ErrorHandler;
import langpendlaren.api.spotify.json.loginpage.LoginPage;
import langpendlaren.api.spotify.json.seeds.Seeds;
import langpendlaren.api.spotify.json.tokens.Tokens;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
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

        /**
         * Förnya access token.
         */
        javalin.get("/spotify/login/refresh", context -> {
            String refreshToken = context.queryParam("refresh_token");
            if(refreshToken == null || refreshToken.isBlank()) {
                ErrorHandler.sendErrorMessage(context, new BadRequestException(), "Refresh token is missing.", HttpStatus.BAD_REQUEST);
            }

            try {
                AuthorizationCodeCredentials tokenCredentials = spotifyAPI.refreshAccessToken(refreshToken);
                Tokens tokens = new Tokens(tokenCredentials);
                context.json(tokens);
            } catch(IOException | SpotifyWebApiException | ParseException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        /*
        * "Skickar" access token och refresh token som kakor till användaren.
        * Kräver kod från Spotify (fås från /spotify/login).
         */
        javalin.get("/spotify/authenticated", context -> {
            String code = context.queryParam("code");

            if(code == null || code.isBlank()) {
                ErrorHandler.sendErrorMessage(context, new BadRequestException(), "Code is missing", HttpStatus.BAD_REQUEST);
            }

            try {
                AuthorizationCodeCredentials tokenCredentials = spotifyAPI.auth(code);
                Tokens tokensJson = new Tokens(tokenCredentials);
                context.json(tokensJson);
            } catch (IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        /*
        * Hämta en lista av alla genrer seeds.
         */
        javalin.get("/spotify/genre/seeds/{accessToken}", context -> {
            String accessToken = context.pathParam("accessToken");
            try {
                String[] seeds = spotifyAPI.genreSeeds(accessToken);
                Seeds json = new Seeds();
                json.setSeeds(seeds);
                context.json(json);
            } catch(UnauthorizedException e) {
                ErrorHandler.sendErrorMessage(context, e, "You must login first.", HttpStatus.UNAUTHORIZED);
            }
        });

        // -- User
        //TODO Ändra access token till query param
        javalin.get("/spotify/user/profile/{accessToken}", context -> {
            String accessToken = context.pathParam("accessToken");
            context.json(spotifyAPI.getUserId(accessToken));
        });

        // -- Playlist
        javalin.post("/spotify/playlist/create", context -> {
            langpendlaren.api.spotify.json.playlist.Body body;

            try {
                body = context.bodyAsClass(langpendlaren.api.spotify.json.playlist.Body.class);
            } catch(Exception e) {
                ErrorHandler.sendErrorMessage(context, e, "Body is missing or not correct", HttpStatus.BAD_REQUEST);
                return;
            }

            String name = body.getName();
            String description = body.getDescription();
            String accessToken = context.queryParam("access_token");

            if(accessToken == null || accessToken.isBlank()) {
                ErrorHandler.sendErrorMessage(context, new BadRequestException(), "Access token is missing", HttpStatus.BAD_REQUEST);
            } else {
                context.result(spotifyAPI.createPlayList(accessToken, name, description));
            }
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


            //FIXME broken
            context.result(spotifyAPI.removeItemFromPlayList("", pId, tIds));
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
}
