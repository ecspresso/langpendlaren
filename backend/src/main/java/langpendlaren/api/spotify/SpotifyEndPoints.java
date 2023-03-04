package langpendlaren.api.spotify;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import langpendlaren.api.http.ErrorHandler;
import langpendlaren.api.spotify.json.loginpage.LoginPage;
import langpendlaren.api.spotify.json.playlist.Body;
import langpendlaren.api.spotify.json.playlist.BodyTrack;
import langpendlaren.api.spotify.json.playlist.Delete;
import langpendlaren.api.spotify.json.playlist.Playlist;
import langpendlaren.api.spotify.json.playlist.PlaylistJson;
import langpendlaren.api.spotify.json.playlist.SnapshotId;
import langpendlaren.api.spotify.json.playlist.TrackJson;
import langpendlaren.api.spotify.json.seeds.Seeds;
import langpendlaren.api.spotify.json.seeds.SeedsJson;
import langpendlaren.api.spotify.json.spotifyUser.SpotifyUser;
import langpendlaren.api.spotify.json.spotifyUser.SpotifyUserJson;
import langpendlaren.api.spotify.json.tokens.Tokens;
import langpendlaren.api.spotify.json.tracks.TracksJson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyEndPoints {
    private final Javalin javalin;
    private final SpotifyAPI spotifyAPI;
    private final ObjectMapper mapper = new ObjectMapper();

    public SpotifyEndPoints(Javalin javalin){
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.javalin = javalin;
        try {
            spotifyAPI = new SpotifyAPI();
        } catch(URISyntaxException | IOException e) {
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
        * "Skickar" access token och refresh token som kakor till användaren.
        * Kräver kod från Spotify (fås från /spotify/login).
         */
        javalin.get("/spotify/authenticated", context -> {
            String code = context.queryParam("code");

            if(code == null || code.isBlank()) {
                ErrorHandler.sendErrorMessage(context, new Exception(), "Code is missing", HttpStatus.BAD_REQUEST);
            }

            try {
                String tokenCredentials = spotifyAPI.auth(code);
                Tokens tokensJson = new Tokens(tokenCredentials);
                context.json(tokensJson);
            } catch (Exception e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });

        /*
         * Förnya access token.
         */
        javalin.get("/spotify/login/refresh", context -> {
            String refreshToken = context.queryParam("refresh_token");
            if(refreshToken == null || refreshToken.isBlank()) {
                ErrorHandler.sendErrorMessage(context, new Exception(), "Refresh token is missing.", HttpStatus.BAD_REQUEST);
            }

            try {
                String tokenCredentials = spotifyAPI.refreshAccessToken(refreshToken);
                Tokens tokens = new Tokens(tokenCredentials);
                context.json(tokens);
            } catch(Exception e) {
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
                SpotifyUserJson spotifyUserJson = mapper.readValue(spotifyAPI.getUser(accessToken), SpotifyUserJson.class);
                SpotifyUser spotifyUser = new SpotifyUser(spotifyUserJson);
                context.json(spotifyUser);
            } catch(Exception e) {
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
                SeedsJson seedsJson = mapper.readValue(spotifyAPI.genreSeeds(accessToken), SeedsJson.class);
                Seeds json = new Seeds();
                json.setSeeds(seedsJson.getGenres());
                context.json(json);
            } catch(Exception e) {
                ErrorHandler.sendErrorMessage(context, e);
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
                PlaylistJson playlistJson = mapper.readValue(spotifyAPI.getPlaylistByPlaylistId(accessToken, playlistId), PlaylistJson.class);
                Playlist playlist = new Playlist(playlistJson);
                context.json(playlist);
            } catch(Exception e) {
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
                PlaylistJson playlistJson = mapper.readValue(spotifyAPI.createPlaylist(accessToken, name, description), PlaylistJson.class);
                Playlist playlist = new Playlist(playlistJson);
                context.json(playlist);
            } catch(Exception e) {
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
            } catch(Exception e) {
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
                SnapshotId result = mapper.readValue(spotifyAPI.addToPlayList(accessToken, pId, body.getTrackId()), SnapshotId.class);
                TrackJson trackJson = new TrackJson(result.getSnapshotId(), pId, body.getTrackId());
                context.json(trackJson);
            } catch(Exception e) {
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
                String s = spotifyAPI.removeItemFromPlayList(accessToken, pId, tId);
                SnapshotId result = mapper.readValue(s, SnapshotId.class);
                TrackJson trackJson = new TrackJson(result.getSnapshotId(), pId, tId);
                context.json(trackJson);
            } catch(Exception e) {
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

            try {
                String type = context.pathParam("type");
                TracksJson tracksJson = mapper.readValue(spotifyAPI.searchTracks(accessToken, type), TracksJson.class);
                context.json(tracksJson);
            } catch(Exception e) {
                ErrorHandler.sendErrorMessage(context, e);
            }
        });
    }

    private String getAccessToken(io.javalin.http.Context context) {
        String accessToken = context.queryParam("access_token");

        if(accessToken == null || accessToken.isBlank()) {
            ErrorHandler.sendErrorMessage(context, new Exception(), "Access token is missing", HttpStatus.BAD_REQUEST);
            return null;
        } else {
            return accessToken;
        }
    }

}
