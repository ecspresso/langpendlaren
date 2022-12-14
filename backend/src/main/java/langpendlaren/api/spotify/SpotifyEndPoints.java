package langpendlaren.api.spotify;


import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.ErrorHandler;
import langpendlaren.api.spotify.json.loginpage.LoginPage;
import langpendlaren.api.spotify.json.playlist.BodyTrack;
import langpendlaren.api.spotify.json.playlist.AlbumJson;
import langpendlaren.api.spotify.json.playlist.Body;
import langpendlaren.api.spotify.json.playlist.Delete;
import langpendlaren.api.spotify.json.playlist.PlaylistJson;
import langpendlaren.api.spotify.json.playlist.TrackJson;
import langpendlaren.api.spotify.json.seeds.Seeds;
import langpendlaren.api.spotify.json.spotifyUser.SpotifyUserJson;
import langpendlaren.api.spotify.json.tokens.Tokens;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;

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
            try {
                URI uri = spotifyAPI.getLoginPage();
                LoginPage json = new LoginPage();
                json.setUri(uri);
                context.json(json);
            } catch(Exception e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        /*
        * "Skickar" access token och refresh token som kakor till anv??ndaren.
        * Kr??ver kod fr??n Spotify (f??s fr??n /spotify/login).
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

        /**
         * F??rnya access token.
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

        // -- User
        javalin.get("/spotify/user/profile", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            try {
                User user = spotifyAPI.getUserId(accessToken);
                SpotifyUserJson json = new SpotifyUserJson();
                json.setId(user.getId());
                json.setUserUri(user.getUri());
                json.setUserName(user.getDisplayName());
                context.json(json);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        /*
        * H??mta en lista av alla genrer seeds.
         */
        javalin.get("/spotify/genre/seeds", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            try {
                String[] seeds = spotifyAPI.genreSeeds(accessToken);
                Seeds json = new Seeds();
                json.setSeeds(seeds);
                context.json(json);
            } catch(UnauthorizedException e) {
                ErrorHandler.sendErrorMessage(context, e, "You must login first.", HttpStatus.UNAUTHORIZED);
            }

        });



        // -- Playlist
        javalin.get("/spotify/playlist/{pId}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String playlistId = context.pathParam("pId");
            try {
                Playlist playstList = spotifyAPI.getPlaylistByPlaylistId(accessToken, playlistId);
                PlaylistJson playlistJson = new PlaylistJson();
                playlistJson.setName(playstList.getName());
                playlistJson.setId(playstList.getId());
                // playlistJson.setCoverImg(playstList.ge);
                context.json(playlistJson);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        // /spotify/playlist/create ------------------------------------------------------------------------------------
        javalin.post("/spotify/playlist/", context -> {
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
            try {
                Playlist playlist = spotifyAPI.createPlayList(accessToken, name, description);
                PlaylistJson playlistJson = new PlaylistJson();
                playlistJson.setId(playlist.getId());
                playlistJson.setName(playlist.getName());
                context.json(playlistJson);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        // /spotify/playlist/delete/{id} -------------------------------------------------------------------------------
        javalin.delete("/spotify/playlist/{id}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String id = context.pathParam("id");

            try {
                spotifyAPI.deletePlayList(accessToken, id);
                Delete deleted = new Delete();
                deleted.setId(id);
                context.json(deleted);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            };
        });

        // /spotify/playlist/add/{pid}/{tid} ---------------------------------------------------------------------------
        javalin.put("/spotify/playlist/{pid}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String pId = context.pathParam("pid");

            BodyTrack body;

            try {
                body = context.bodyAsClass(BodyTrack.class);
            } catch(Exception e) {
                ErrorHandler.sendErrorMessage(context, e, "Body is missing or not correct", HttpStatus.BAD_REQUEST);
                return;
            }

            try {
                SnapshotResult result = spotifyAPI.addToPlayList(accessToken, pId, body.getTrackId());
                TrackJson trackJson = new TrackJson(result.getSnapshotId(), pId, body.getTrackId());
                context.json(trackJson);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        // /spotify/playlist/deletetracks/{pid}/{tid} ------------------------------------------------------------------
        javalin.delete("/spotify/playlist/track/{pid}/{tid}", context -> {
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String pId = context.pathParam("pid");
            String tId = context.pathParam("tid");

            try {
                SnapshotResult result = spotifyAPI.removeItemFromPlayList(accessToken, pId, tId);
                TrackJson trackJson = new TrackJson(result.getSnapshotId(), pId, tId);
                context.json(trackJson);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        // -- Search playlist
        javalin.get("/spotify/search/track/{type}", context -> {
            // Get access token
            String accessToken;
            if((accessToken = getAccessToken(context)) == null) {
                return;
            }

            String type = context.pathParam("type");
            try {
                Paging<Track> tracks = spotifyAPI.searchTracks(accessToken, type);
                AlbumJson albumJson = new AlbumJson(tracks);
                context.json(albumJson);
            } catch(IOException | ParseException | SpotifyWebApiException e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });
    }

    private String getAccessToken(io.javalin.http.Context context) {
        String accessToken = context.queryParam("access_token");

        if(accessToken == null || accessToken.isBlank()) {
            ErrorHandler.sendErrorMessage(context, new BadRequestException(), "Access token is missing", HttpStatus.BAD_REQUEST);
            return null;
        } else {
            return accessToken;
        }
    }

}
