package langpendlaren.api.spotify;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class Authorize {
    private final String clientId;
    private final String clientSecret;
    private final String baseUri = "https://accounts.spotify.com";
    private final URI redirectURI;

    public Authorize(String clientId, String clientSecret, URI redirectURI) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectURI = redirectURI;
    }

    public URI authorize() {
        String scope = "playlist-read-private,user-follow-modify,playlist-read-collaborative,user-follow-read,user-read-currently-playing,playlist-modify-private,playlist-modify-public";
        String responseType = "code";
        String authUri = baseUri + "/authorize";
        String formatBase = "%s?client_id=%s&response_type=%s&redirect_uri=%s&scope=%s";
        return URI.create(String.format(formatBase, authUri, clientId, responseType, redirectURI, scope));
    }

    public String getAccessToken(String code) throws IOException {
        List<BasicNameValuePair> body = List.of(
            new BasicNameValuePair("grant_type", "authorization_code"),
            new BasicNameValuePair("code", code),
            new BasicNameValuePair("redirect_uri", redirectURI.toString())
        );

        return post(body);
    }

    public String refreshToken(String refreshToken) throws IOException {
        List<BasicNameValuePair> body = List.of(
            new BasicNameValuePair("grant_type", "refresh_token"),
            new BasicNameValuePair("refresh_token", refreshToken)
        );

        return post(body);
    }

    private String post(List<BasicNameValuePair> body) throws IOException {
        String tokenURL = baseUri + "/api/token";
        String authString = String.format("%s:%s", clientId, clientSecret);
        String base64AuthString = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

        HttpPost post = new HttpPost(tokenURL);
        post.setHeader("Authorization", "Basic " + base64AuthString);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");



        HttpClientBuilder httpBuilder = HttpClientBuilder.create();

        try(CloseableHttpClient client = httpBuilder.build(); HttpEntity entity = new UrlEncodedFormEntity(body)) {
            BasicHttpClientResponseHandler httpHandler = new BasicHttpClientResponseHandler();
            post.setEntity(entity);
            return client.execute(post, httpHandler);
        }
    }
}