package langpendlaren.api.spotify;


import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.ErrorHandler;
import langpendlaren.api.spotify.json.loginpage.LoginPage;
import langpendlaren.api.spotify.json.playlist.Body;
import langpendlaren.api.spotify.json.playlist.PlaylistList;
import langpendlaren.api.spotify.json.seeds.Seeds;
import langpendlaren.api.spotify.json.tokens.Tokens;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

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
        javalin.get("/spotify/genre/seeds", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            try {
                String[] seeds = spotifyAPI.genreSeeds(accessToken);
                System.out.println(seeds);
                Seeds json = new Seeds();
                json.setSeeds(seeds);
                context.json(json);
            } catch(UnauthorizedException e) {
                ErrorHandler.sendErrorMessage(context, e, "You must login first.", HttpStatus.UNAUTHORIZED);
            }

        });

        // -- User
        javalin.get("/spotify/user/profile", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            context.json(spotifyAPI.getUserId(accessToken));
        });

        // -- Playlist
        javalin.post("/spotify/playlist/create", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            Body body;

            try {
                body = context.bodyAsClass(Body.class);
            } catch(Exception e) {
                ErrorHandler.sendErrorMessage(context, e, "Body is missing or not correct", HttpStatus.BAD_REQUEST);
                return;
            }

            String name = body.getName();
            String description = body.getDescription();
            context.result(spotifyAPI.createPlayList(accessToken, name, description)); //TODO returnera id?
        });

        javalin.delete("/spotify/playlist/delete/{id}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String id = context.pathParam("id");
            try {
                String deleted = spotifyAPI.deletePlayList(accessToken, id);
                System.out.println(deleted);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            };
        });

        javalin.put("/spotify/playlist/add/{pid}/{tid}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String pId = context.pathParam("pid");
            String tId = context.pathParam("tid");
            context.result(spotifyAPI.addToPlayList(accessToken, pId, tId));
        });

        javalin.delete("/spotify/playlist/deletetracks/{pid}/{tids}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String pId = context.pathParam("pid");
            String tIds = context.pathParam("tids");


            //FIXME broken
            context.result(spotifyAPI.removeItemFromPlayList(accessToken, pId, tIds));
        });

        // -- Albums
        // javalin.get("/spotify/album/{id}", context -> {
        //     String accessToken;
        //     if((accessToken = getAccessToken(context)) == null) {
        //         return;
        //     }
        //
        //     String id = context.pathParam("id");
        //     context.json(spotifyAPI.getAlbumById(accessToken, id));
        // });

        // javalin.get("/spotify/album/{ids}", context ->{
        //     String accessToken;
        //     if((accessToken = getAccessToken(context)) == null) {
        //         return;
        //     }
        //
        //     String ids = context.pathParam("ids");
        //     context.json(spotifyAPI.getAlbums(accessToken, ids));
        // });

        // -- Search
        // javalin.get("/spotify/search/track/{name}", context -> {
        //     String accessToken;
        //     if((accessToken = getAccessToken(context)) == null) {
        //         return;
        //     }
        //
        //     String name = context.pathParam("name");
        //     context.json(spotifyAPI.searchTracks(accessToken, name));
        // });

        // -- Search playlist
        javalin.get("/spotify/search/playlist/{type}", context -> {
            // Get access token
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String type = context.pathParam("type");
            Paging<PlaylistSimplified> paging;

            try {
                int offset = Integer.parseInt(context.queryParam("offset"));
                paging = spotifyAPI.searchPlayList(accessToken, type, offset);
            } catch(NumberFormatException | NullPointerException e) {
                paging = spotifyAPI.searchPlayList(accessToken, type);
            }

            PlaylistList list = new PlaylistList();
            list.addPlaylists(type, paging);
            context.json(list);
        });



        // -- Artists
        // javalin.get("/spotify/artist/profile/{id}", context -> {
        //     String accessToken;
        //     if((accessToken = getAccessToken(context)) == null) {
        //         return;
        //     }
        //
        //     String id = context.pathParam("id");
        //     context.json(spotifyAPI.getArtist(accessToken, id));
        // });
    }

    private String getAccessToken(Context context) {
        String accessToken = context.queryParam("access_token");

        if(accessToken == null || accessToken.isBlank()) {
            ErrorHandler.sendErrorMessage(context, new BadRequestException(), "Access token is missing", HttpStatus.BAD_REQUEST);
            return null;
        } else {
            return accessToken;
        }
    }
}
