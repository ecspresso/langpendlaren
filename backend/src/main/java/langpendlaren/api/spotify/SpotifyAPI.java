package langpendlaren.api.spotify;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyAPI {
    private final SpotifyApi spotifyApiWrapper;
    private final Authorize authorize;
    private final Object lock = new Object();

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

    public URI getLoginAddress(){
        final AuthorizationCodeUriRequest authorizationCodeUriRequest = this.spotifyApiWrapper.authorizationCodeUri()
                .scope("playlist-read-private,user-follow-modify,playlist-read-collaborative,user-follow-read, user-read-currently-playing,playlist-modify-private, playlist-modify-public")
                .build();
        URI uri = authorizationCodeUriRequest.execute();
        return uri;
    }

    /**
     * Get All play lists
     * @return String
     */
    public String getCurrentPlayList(){
        GetListOfUsersPlaylistsRequest gPlayList;

        String userId = getUserProfile();
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
     * Create a play list
     */
    public String createPlayList(String name, String dec){
        CreatePlaylistRequest createPlayList;
        String userId = getUserProfile();
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
     * Authentication
     * @param code id from user premonition
     */
    public void auth(String code) {
        authorize.auth(code);
    }





    // End Points functions

    /**
     * Get a play list
     */
    public String getPlayList(String pId){
        GetPlaylistRequest getPlayList = this.spotifyApiWrapper.getPlaylist(pId).build();
        try {
            final Playlist playlist = getPlayList.execute();
            System.out.println("Name: " + playlist.getName());
            return playlist.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return e.getMessage();
        }
    }

    public String getUserProfile() {
        GetCurrentUsersProfileRequest userProfile = spotifyApiWrapper.getCurrentUsersProfile().build();
        try {
            User user = userProfile.execute();
            return user.getId();
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            System.err.println(e);
            return null;
        }
    }

    //FIXME! implement

    public String getAlbums() {
        return null;
    }

    public String getAlbumById(String id) {
        return null;
    }

    public void deletePlayList(String id) {
    }

    public void addToPlayList(String pid, String tid) {

    }

    public String searchItem(String name) {
        return null;
    }

    public String getArtist(String id) {
        return null;
    }

    public String getTopTruckByArtistId(String id) {
        return null;
    }

    public String getArtistAlbums(String id) {
        return null;
    }


}

