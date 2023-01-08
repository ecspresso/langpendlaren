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

    public Authorize(SpotifyApi spotifyApiWrapper) {
        this.spotifyApiWrapper = spotifyApiWrapper;
    }

    public URI authorize() {
        // Scope for defining what we need from the API
        String scope = "playlist-read-private,user-follow-modify,playlist-read-collaborative,user-follow-read,user-read-currently-playing,playlist-modify-private, playlist-modify-public";
        AuthorizationCodeUriRequest authorizationCodeUriRequest = this.spotifyApiWrapper.authorizationCodeUri().scope(scope).show_dialog(false).build();
        return authorizationCodeUriRequest.execute();
    }

    public AuthorizationCodeCredentials getAccessToken(String code) throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApiWrapper.authorizationCode(code).build(); // Invalid redirect URI
        return authorizationCodeRequest.execute();

    }

    public AuthorizationCodeCredentials refreshToken(String refreshToken) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApiWrapper.setRefreshToken(refreshToken);
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApiWrapper.authorizationCodeRefresh().build();
        return authorizationCodeRefreshRequest.execute();
    }
}