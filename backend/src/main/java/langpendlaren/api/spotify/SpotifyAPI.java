package langpendlaren.api.spotify;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpotifyAPI {
    // private static String scope = "user-read-birthdate,user-read-email";
    private final SpotifyApi spotifyApiWrapper;
    private final Authorize authorize;
    private final Object lock = new Object();


    public SpotifyAPI() throws URISyntaxException {
        // Läs in inställningar från fil.
        Properties prop = new Properties();
        URL fileUrl = this.getClass().getResource("/spotify.properties");
        File file = new File(Objects.requireNonNull(fileUrl).toURI());
        try(FileInputStream fis = new FileInputStream(file)) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Hemliga saker för vår app.
        String clientId = prop.getProperty("clientId", "8b83701f45c34a07b396c5199a7c3998");
        String clientSecret = prop.getProperty("clientSecret", "b9e7dfdd05f54d1483cdfecc63e1861c");
        String redirect = prop.getProperty("redirect", "http://localhost/spotify/authenticated");
        // Var Spotify ska skicka tillbaka användaren.
        URI redirectURI = new URI(redirect);
        // Wrapper för Spotifys API
        this.spotifyApiWrapper = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).setRedirectUri(redirectURI).build();
        // Klass för att hantera Spotify inloggning.
        authorize = new Authorize(spotifyApiWrapper);

        // Förnya token varje timme.
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            synchronized(lock) {
                try {
                    authorize.refreshToken();
                } catch(IOException | ParseException | SpotifyWebApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 59, 59, TimeUnit.MINUTES);
    }

    /**
     * Get All play lists
     * @return String
     */
    public String getCurrentPlayList(){
        GetListOfUsersPlaylistsRequest gPlayList;

        synchronized(lock) {
            gPlayList = this.spotifyApiWrapper.getListOfUsersPlaylists("userId").build();
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
    public void createPlayList(String userId, String name, String dec){
        CreatePlaylistRequest createPlayList;
        synchronized(lock) {
            createPlayList = this.spotifyApiWrapper.createPlaylist(userId, name).public_(false).description(dec).build();
        }

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


    /**
     * Get AccessToken each time the request needs
     */

    public URI auth() {
        return authorize.authorize();
    }

    public void getAccessToken(String code) throws IOException, ParseException, SpotifyWebApiException {
        authorize.getAccessToken(code);
    }

    public void me() {
        GetCurrentUsersProfileRequest user = spotifyApiWrapper.getCurrentUsersProfile().build();
        try {
            User me = user.execute();
            System.out.println(me.getId());
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            System.err.println(e);
        }
    }
}

