package langpendlaren.api.spotify;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Authorize {
    private final AuthorizationCodeUriRequest authorizationCodeUriRequest;
    private final SpotifyApi spotifyApiWrapper;
    private final Object lock = new Object();


    public Authorize(SpotifyApi spotifyApiWrapper) {
        this.spotifyApiWrapper = spotifyApiWrapper;
        authorizationCodeUriRequest = this.spotifyApiWrapper.authorizationCodeUri().build();
    }

    /**
     * To authenticate
     * @param code an id from login page come back
     * @return
     */
    public String auth(String code){
        System.out.println("auth");
        final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApiWrapper.authorizationCode(code)
                .build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            this.spotifyApiWrapper.setAccessToken(authorizationCodeCredentials.getAccessToken());
            this.spotifyApiWrapper.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            updateToken();
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn() + " token: " + authorizationCodeCredentials.getAccessToken());
            return authorizationCodeCredentials.getAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return e.getMessage();
        }
    }

    public void refreshToken() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            synchronized(lock) {
                updateToken();
            }
        }, 59, 59, TimeUnit.MINUTES);
    }
    public void updateToken(){
        final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = this.spotifyApiWrapper.authorizationCodeRefresh()
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            this.spotifyApiWrapper.setAccessToken(authorizationCodeCredentials.getAccessToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
