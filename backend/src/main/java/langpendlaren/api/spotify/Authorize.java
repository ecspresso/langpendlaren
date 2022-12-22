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

public class Authorize {
    private final SpotifyApi spotifyApiWrapper;
    private final AuthorizationCodeUriRequest authorizationCodeUriRequest;
    private AuthorizationCodeCredentials authorizationCodeCredentials;

    public Authorize(SpotifyApi spotifyApiWrapper) {
        this.spotifyApiWrapper = spotifyApiWrapper;
        authorizationCodeUriRequest = this.spotifyApiWrapper.authorizationCodeUri().show_dialog(true).build();
    }

    public URI authorize() {
        return authorizationCodeUriRequest.execute();
    }

    public void getAccessToken(String code) throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApiWrapper.authorizationCode(code).build(); // Invalid redirect URI
        authorizationCodeCredentials = authorizationCodeRequest.execute();
        spotifyApiWrapper.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApiWrapper.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
    }

    public void refreshToken() throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApiWrapper.authorizationCodeRefresh().build();
        authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
        spotifyApiWrapper.setAccessToken(authorizationCodeCredentials.getAccessToken());
    }
}