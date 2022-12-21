package langpendlaren.api.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.playlists.*;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpotifyAPI {
    // Spotify wrapper
    private final SpotifyApi spotifyApiWrapper;
    // Authorize class
    private final Authorize authorize;
    // Lock for synchronize work
    private final Object lock = new Object();
    // Scope for defining what we need from the API
    private final String scope = "playlist-read-private,user-follow-modify,playlist-read-collaborative,user-follow-read,user-read-currently-playing,playlist-modify-private, playlist-modify-public";

    public SpotifyAPI() throws URISyntaxException {
        String clientId = "8b83701f45c34a07b396c5199a7c3998";
        String clientSecret = "b9e7dfdd05f54d1483cdfecc63e1861c";
        URI redirectURI = SpotifyHttpManager.makeUri("http://localhost:8080");
        this.spotifyApiWrapper = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURI)
                .build();

        authorize = new Authorize(spotifyApiWrapper);
        authorize.refreshToken();
    }

    /**
     * User can log in to spotify account.
     * @return link to spotify account
     */
    public URI getLoginPage(){
        final AuthorizationCodeUriRequest authorizationCodeUriRequest = this.spotifyApiWrapper.authorizationCodeUri()
                .scope(scope)
                .build();
        URI uri = authorizationCodeUriRequest.execute();
        return uri;
    }

    /**
     * Authentication
     * @param code an id from user acceptation
     */
    public void auth(String code) {
        authorize.auth(code);
    }


    // User

    /**
     * Users id //FIXME! return profile.
     * @return user id
     */
    public String getUserId() {
        GetCurrentUsersProfileRequest userProfile = spotifyApiWrapper.getCurrentUsersProfile().build();
        try {
            User user = userProfile.execute();
            return user.getId();
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            System.err.println(e);
            return null;
        }
    }

    // Playlist

    /**
     * Create a play list by Name and a description
     */
    public String createPlayList(String name, String dec){
        CreatePlaylistRequest createPlayList;
        String userId = getUserId();
        synchronized(lock) {
            createPlayList = this.spotifyApiWrapper.createPlaylist(userId, name).public_(false).description(dec).build();
        }

        try {
            Playlist playlist = createPlayList.execute();
            System.out.println("Name: " + playlist.getName());
            return playlist.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    /**
     * Get All play lists of the current user
     * @return String
     */
    public String getCurrentPlayLists(){
        GetListOfUsersPlaylistsRequest gPlayList;
        String userId = getUserId();

        synchronized(lock) {
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
    public String getPlayListById(String id){
        GetPlaylistRequest getPlayList = this.spotifyApiWrapper.getPlaylist(id).build();
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
     * @param pid play list id
     * @param tracks tracks
     */
    public String removeItemFromPlayList(String pid, String tracks){
        final JsonArray trackJson = JsonParser.parseString("[{\"localhost:8080\":\"spotify:track:01iyCAUm8EvOFqVWYJ3dVX\"}]").getAsJsonArray();
        System.out.println("Track JSON: " + trackJson);
        final RemoveItemsFromPlaylistRequest removeItemsFromPlaylistRequest = this.spotifyApiWrapper
                .removeItemsFromPlaylist(pid, trackJson)
                .build();

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
     * @param pid playlist id
     * @param tid track id
     * @return confirmation
     */
    public String addToPlayList(String pid, String tid) {
        final String[] uris = new String[]{"spotify:track:01iyCAUm8EvOFqVWYJ3dVX", "spotify:episode:4GI3dxEafwap1sFiTGPKd1"};
        final AddItemsToPlaylistRequest addItemsToPlaylistRequest = this.spotifyApiWrapper
                .addItemsToPlaylist(pid, uris)
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
    public ArrayList<Map> getAlbums(String[] ids) {
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

