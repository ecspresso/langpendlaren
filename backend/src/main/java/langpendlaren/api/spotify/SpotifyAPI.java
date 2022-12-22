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
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class SpotifyAPI {
    // Spotify wrapper
    private final SpotifyApi spotifyApiWrapper;
    // Authorize class
    private final Authorize authorize;
    // Lock for synchronize work
    private final Object lock = new Object();

    public SpotifyAPI() throws URISyntaxException {
        // Läs in inställningar från fil.
        Properties prop = new Properties();
        FileReader fileReader;

        try {
            URL fileUrl = this.getClass().getClassLoader().getResource("spotify.properties");
            File file = new File(Objects.requireNonNull(fileUrl).toURI());
            fileReader =  new FileReader(file);
            prop.load(fileReader);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Hemliga saker för vår app.
        String clientId = prop.getProperty("clientId");
        String clientSecret = prop.getProperty("clientSecret");
        String redirect = prop.getProperty("redirect");
        // Var Spotify ska skicka tillbaka användaren.
        URI redirectURI = new URI(redirect);
        // Wrapper för Spotifys API
        this.spotifyApiWrapper = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).setRedirectUri(redirectURI).build();
        // Klass för att hantera Spotify inloggning.
        authorize = new Authorize(spotifyApiWrapper);
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
    public AuthorizationCodeCredentials auth(String code) throws IOException, ParseException, SpotifyWebApiException {
        return authorize.getAccessToken(code);
    }

    // User

    /**
     * Users id //FIXME! return profile.
     * @return user id
     */
    public String getUserId() {
        String userId;

        GetCurrentUsersProfileRequest userProfile = spotifyApiWrapper.getCurrentUsersProfile().build();
        try {
            userId = userProfile.execute().getId();
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            userId = e.getMessage();
        }

        return userId;
    }

    // Playlist

    /**
     */
    public String createPlayList(String accessToken, String name, String dec){
        CreatePlaylistRequest createPlayList;
        String userId = getUserId();
        synchronized(lock) {
            this.spotifyApiWrapper.setAccessToken(accessToken);
            createPlayList = this.spotifyApiWrapper.createPlaylist(userId, name).public_(false).description(dec).build();
        }

        try {
            Playlist playlist = createPlayList.execute();
            System.out.println("Name: " + playlist.getName());
            return playlist.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Get All play lists of the current user
     * @return String
     */
    public String getAllPlayList(String accessToken){
        GetListOfUsersPlaylistsRequest gPlayList;
        String userId = getUserId();

        synchronized(lock) {
            this.spotifyApiWrapper.setAccessToken(accessToken);
            gPlayList = this.spotifyApiWrapper.getListOfUsersPlaylists(userId).build();
        }

        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = gPlayList.execute();
            return "Total: " + playlistSimplifiedPaging.getTotal();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Get a play list by id
     */
    public String getPlayListById(String accessToken, String id){
        GetPlaylistRequest getPlayList;
        synchronized(lock) {
            this.spotifyApiWrapper.setAccessToken(accessToken);
            getPlayList = this.spotifyApiWrapper.getPlaylist(id).build();
        }
        try {
            final Playlist playlist = getPlayList.execute();
            System.out.println("Name: " + playlist.getName());
            return playlist.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Remove item (tracks) from play list
     * @param pId play list id
     * @param tIds truck ids
     */
    public String removeItemFromPlayList(String accessToken, String pId, String tIds){
        final JsonArray trackJson = JsonParser.parseString("[{\"localhost:8080\":\"spotify:track:01iyCAUm8EvOFqVWYJ3dVX\"}]").getAsJsonArray();
        System.out.println("Track JSON: " + trackJson);
        final RemoveItemsFromPlaylistRequest removeItemsFromPlaylistRequest ;

        synchronized(lock) {
            this.spotifyApiWrapper.setAccessToken(accessToken);
            removeItemsFromPlaylistRequest = this.spotifyApiWrapper
                .removeItemsFromPlaylist(pId, trackJson)
                .build();
        }

        try {
            final SnapshotResult snapshotResult = removeItemsFromPlaylistRequest.execute();
            System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
            return snapshotResult.getSnapshotId();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Delete a play list by id
     * @param id playlist id
     */
    public void deletePlayList(String id) {
        // No code provided for deleting a playlist.
    }

    /**
     * Add a track to play list.
     * @param pId playlist id
     * @param tId track id
     * @return confirmation
     */
    public String addToPlayList(String pId, String tId) {
        final String[] uris = new String[]{"spotify:track:01iyCAUm8EvOFqVWYJ3dVX", "spotify:episode:4GI3dxEafwap1sFiTGPKd1"};
        final AddItemsToPlaylistRequest addItemsToPlaylistRequest = this.spotifyApiWrapper
                .addItemsToPlaylist(pId, uris)
                .build();

        try {
            final SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();

            System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
            return snapshotResult.getSnapshotId();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }



    /**
     * Get albums by ids
     * @param ids album ids
     * @return list of albums
     */
    public ArrayList<Map> getAlbums(String ids) {
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
    public String getAlbumById(String id) {
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
     * Search play list by name
     * @param name search key
     * @return a list of play list
     */
    public ArrayList<Map> searchPlaylist(String name) {
        final SearchPlaylistsRequest searchPlaylistsRequest = this.spotifyApiWrapper.searchPlaylists(name)
                .limit(10)
                .offset(0)
                .build();

        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = searchPlaylistsRequest.execute();

            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
            //return new ArrayList<String>(playlistSimplifiedPaging.toString()); //FIXME!
            return new ArrayList<>();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Search tracks by name
     * @param name search key
     * @return a list of tracks
     */
    public ArrayList<Map> searchTracks(String name){
        final SearchTracksRequest searchPlaylistsRequest = this.spotifyApiWrapper.searchTracks(name)
                .build();

        try {
            final Paging<Track> trackPaging = searchPlaylistsRequest.execute();

            System.out.println("Total: " + trackPaging.getTotal());
            return new ArrayList<>(); //FIXME!
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get an artist by id
     * @param id artist id
     * @return artist
     */
    public String getArtist(String id) {
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

    /**
     * Get top tracks by artist id
     * @param id artist id
     * @return list of tracks
     */
    public ArrayList<Map> getTopTruckByArtistId(String id) {
        final GetArtistsTopTracksRequest getArtistsTopTracksRequest = this.spotifyApiWrapper.getArtistsTopTracks(id, CountryCode.SE)
                .build();
        try {
            final Track[] tracks = getArtistsTopTracksRequest.execute();

            System.out.println("Length: " + tracks.length);
            ArrayList trackList = new ArrayList<Map>();
            Map map = new HashMap();
            for(Track track : tracks){
                map.put("name", track.getName());
            }
            return trackList;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null; //FIXME! return valid message
        }
    }

    /**
     * Get all albums by artis id
     * @param id artist id
     * @return list of albums
     */
    public ArrayList<Map> getArtistAlbums(String id) {
        final GetArtistsAlbumsRequest getArtistsAlbumsRequest = this.spotifyApiWrapper.getArtistsAlbums(id)
                .album_type("album").limit(10).offset(0).market(CountryCode.SE)
                .build();
        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = getArtistsAlbumsRequest.execute();

            System.out.println("Total: " + albumSimplifiedPaging.getTotal());
            AlbumSimplified[] albumSim = albumSimplifiedPaging.getItems();
            ArrayList<Map> albumList = new ArrayList<>();
            Map map = new HashMap();
            for(AlbumSimplified albumSimplified : albumSim){
                map.put("Name", albumSimplified.getName());
            }
            albumList.add(map);
            return albumList;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


}

