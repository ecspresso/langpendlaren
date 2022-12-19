package langpendlaren.api.spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Authorize {
    private final SpotifyApi spotifyApi;
    private final AuthorizationCodeUriRequest auR;
    public Authorize(String clientId, String clientSecret, String redirectURI, String scope){
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectURI))
                .build();
        this.auR = spotifyApi.authorizationCodeUri()
                .scope(scope)
                .show_dialog(false)
                .build();

        // Call authorize
        authorizationUri_Async();
    }

    public void authorizationUri_Async() {
        try {
            CompletableFuture<URI> uriFuture = this.auR.executeAsync();
            URI uri = uriFuture.join();
            System.out.println("Authority: " + uri.getAuthority() + " URI: " + uri);
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}
