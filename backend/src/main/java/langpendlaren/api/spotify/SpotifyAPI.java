package langpendlaren.api.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.follow.UnfollowPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class SpotifyAPI {
    // Spotify wrapper
    // Authorize class
    private final Authorize authorize;
    // Lock for synchronize work
    private final Object lock = new Object();
    private Properties prop;
    private Map<String, String> playListToId = new HashMap();

    public static void main(String[] args) throws URISyntaxException, IOException {
        SpotifyAPI spotifyAPI = new SpotifyAPI();
        String s = spotifyAPI.getLoginPage().toString();
        System.out.println(s);
    }

    public SpotifyAPI() throws URISyntaxException, IOException {
        // Läs in inställningar från fil.
        prop = new Properties();
        FileReader fileReader;

        InputStream stream = getClass().getClassLoader().getResourceAsStream("spotify.properties");
        if(stream == null) {
            throw new IOException("Kunde inte hitta filen spotify.properties");
        } else {
            prop.load(stream);
        }

        // Hemliga saker för vår app.
        String clientId = prop.getProperty("clientId");
        String clientSecret = prop.getProperty("clientSecret");
        String redirect = prop.getProperty("redirect");
        // Var Spotify ska skicka tillbaka användaren.
        URI redirectURI = new URI(redirect);
        // Klass för att hantera Spotify inloggning.
        authorize = new Authorize(clientId, clientSecret, redirectURI);
    }

    public String[] genreSeeds(String accessToken) throws IOException, ParseException, SpotifyWebApiException, URISyntaxException {
        spotifyApiWrapper = new SpotifyApi.Builder().setAccessToken(accessToken).build();
        return spotifyApiWrapper.getAvailableGenreSeeds().build().execute();
    }

    /**
     * User can log in to spotify account.
     * @return link to spotify account
     */
    public URI getLoginPage(){
        return authorize.authorize();
    }

    /**
     * Authentication
     *
     * @param code an id from user acceptation
     * @return AuthorizationCodeCredentials som innehåller access token och refresh token.
     */
    public String auth(String code) throws IOException, ParseException, SpotifyWebApiException {
        return authorize.getAccessToken(code);
    }

    public String refreshAccessToken(String refreshToken) throws IOException, ParseException, SpotifyWebApiException {
        return authorize.refreshToken(refreshToken);
    }

    // User

    /**
     * Users id //FIXME! return profile.
     *
     * @return user id
     */
    public User getUserId(String accessToken) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setAccessToken(accessToken);
        GetCurrentUsersProfileRequest userProfile = spotifyApiWrapper.getCurrentUsersProfile().build();
        return userProfile.execute();
    }

    // Playlist

    /**
     *
     */
    public Playlist createPlayList(String accessToken, String name, String description) throws IOException, ParseException, SpotifyWebApiException {
        CreatePlaylistRequest createPlayList;
        String userId = getUserId(accessToken).getId();
        synchronized(lock) {
            this.spotifyApiWrapper.setAccessToken(accessToken);
            createPlayList = this.spotifyApiWrapper.createPlaylist(userId, name).public_(false).description(description).build();
        }

        return createPlayList.execute();
    }

    /**
     * Remove item (tracks) from play list
     *
     * @param pId  play list id
     * @param tIds truck ids
     */
    public SnapshotResult removeItemFromPlayList(String accessToken, String pId, String tIds) throws IOException, ParseException, SpotifyWebApiException {
        final JsonArray trackJson = JsonParser.parseString(String.format("[{\"uri\":\"spotify:track:%s\"}]", tIds)).getAsJsonArray();
        // System.out.println("Track JSON: " + trackJson);
        final RemoveItemsFromPlaylistRequest removeItemsFromPlaylistRequest ;

        synchronized(lock) {
            this.spotifyApiWrapper.setAccessToken(accessToken);
            removeItemsFromPlaylistRequest = this.spotifyApiWrapper
                .removeItemsFromPlaylist(pId, trackJson)
                .build();
        }

        return removeItemsFromPlaylistRequest.execute();
    }

    /**
     * Delete a play list by id
     * @param id playlist id
     */
    public void deletePlayList(String accessToken, String id) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setAccessToken(accessToken);
        UnfollowPlaylistRequest request = spotifyApiWrapper.unfollowPlaylist(id).build();
        request.execute();
    }

    /**
     * Add a track to play list.
     *
     * @param pId playlist id
     * @param tId track id
     * @return confirmation
     */
    public SnapshotResult addToPlayList(String accessToken, String pId, String tId) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setAccessToken(accessToken);

        final String[] uris = new String[]{String.format("spotify:track:%s", tId)};
        final AddItemsToPlaylistRequest addItemsToPlaylistRequest = this.spotifyApiWrapper
                .addItemsToPlaylist(pId, uris)
                .build();

        return addItemsToPlaylistRequest.execute();
    }



    /**
     * Get albums by ids
     * @param ids album ids
     * @return list of albums
     */
    public ArrayList<Map> getAlbums(String accessToken, String ids) {
        spotifyApiWrapper.setAccessToken(accessToken);

        final GetSeveralAlbumsRequest getSeveralAlbumsRequest = this.spotifyApiWrapper.getSeveralAlbums(ids)
                .build();
        try {
            final Album[] albums = getSeveralAlbumsRequest.execute();
            //FIXME! convert into json before returning all albums.
            System.out.println("Length: " + albums.length);
            ArrayList<Map> albumList = new ArrayList<>();
            Map map = new HashMap<>();
            for(Album album : albums){
                map.put("id", album.getId());
                map.put("Name", album.getName());
                map.put("Type", album.getType());
            }
            albumList.add(map);
            return albumList;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get an album by id
     * @param id albums id
     * @return an album
     */
    public String getAlbumById(String accessToken, String id) {
        spotifyApiWrapper.setAccessToken(accessToken);
        final GetAlbumRequest getAlbumRequest = this.spotifyApiWrapper.getAlbum(id).build();
        try {
            final Album album = getAlbumRequest.execute();
            System.out.println("Name: " + album.getName());
            return album.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Search tracks by name
     *
     * @return a list of track
     */
    public Paging<Track> searchTracks(String accessToken, String genre) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setAccessToken(accessToken);
        // Behöver vi verklingen denna metoden?
        final SearchTracksRequest searchTracksRequest = this.spotifyApiWrapper.searchTracks(genre)
                .limit(10)
                  .offset(0)
                  .includeExternal("audio")
                .build();

        return searchTracksRequest.execute();
    }

    /**
     * Get an artist by id
     * @param id artist id
     * @return artist
     */
    public String getArtist(String accessToken, String id) {
        spotifyApiWrapper.setAccessToken(accessToken);

        // Behöver vi verklingen denna metoden?
        final GetArtistRequest getArtistRequest = this.spotifyApiWrapper.getArtist(id)
                .build();
        try {
            final Artist artist = getArtistRequest.execute();

            System.out.println("Name: " + artist.getName());
            return artist.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public Paging<PlaylistSimplified> searchPlayList(String accessToken, String type) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setAccessToken(accessToken);

        SearchPlaylistsRequest searchPlaylistsRequest = this.spotifyApiWrapper.searchPlaylists(type)
          .market(CountryCode.SE)
          .limit(10)
          .offset(0)
          .includeExternal("audio")
                .build();
        return searchPlaylistsRequest.execute();
    }

    public Playlist getPlaylistByPlaylistId(String accessToken, String playlistId) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setAccessToken(accessToken);
        return spotifyApiWrapper.getPlaylist(playlistId).build().execute();
    }
}

