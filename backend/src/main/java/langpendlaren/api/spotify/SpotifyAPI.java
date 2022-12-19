package langpendlaren.api.spotify;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.io.IOException;

public class SpotifyAPI {
    private static final String clientId = "8b83701f45c34a07b396c5199a7c3998";
    private static final String clientSecret = "b9e7dfdd05f54d1483cdfecc63e1861c";
    private static final String redirectURI = "https://localhost:80";
    private static String scope = "user-read-birthdate,user-read-email";
    private final SpotifyApi spotifyApi;
    private final ClientCredentialsRequest ccR;
    private final String userID = "userID";


    public SpotifyAPI() {
        // Not sure if the auth need only one time.
        new Authorize(clientId, clientSecret, redirectURI, scope);

        this.spotifyApi = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).build();
        this.ccR = spotifyApi.clientCredentials().build();
    }




    /**
     * Get All play lists
     * @return String
     */
    public String getCurrentPlayList(){
        getAccessToken(); // Update accessToken
        GetListOfUsersPlaylistsRequest gPlayList = this.spotifyApi.getListOfUsersPlaylists("userId").build();
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
    public void createPlayList(String name, String dec){
        getAccessToken();
        CreatePlaylistRequest createPlayList = this.spotifyApi.createPlaylist(userID, name).public_(false).description(dec).build();
        try {
            Playlist playlist = createPlayList.execute();

            System.out.println("Name: " + playlist.getName());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Get a play list
     */
    public String getPlayList(String pId){
        getAccessToken();
        GetPlaylistRequest getPlayList = this.spotifyApi.getPlaylist(pId).build();
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
     * Get AccessToken each time the request needs
     */
    public void getAccessToken() {
        try {
            ClientCredentials clientCredentials = this.ccR.execute();
            this.spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}

