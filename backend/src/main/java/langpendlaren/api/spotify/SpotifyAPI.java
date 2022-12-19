package langpendlaren.api.spotify;

import langpendlaren.api.http.Http;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SpotifyAPI extends Http{
    public SpotifyAPI() {
        super(new HttpHost("https://accounts.spotify.com/authorize"));

        id = "8b83701f45c34a07b396c5199a7c3998";
        secret = "b9e7dfdd05f54d1483cdfecc63e1861c";
    }
    private final String id;
    private final String secret;

    public String authorize() {
        HttpPost http = new HttpPost("https://accounts.spotify.com/authorize");
        http.addHeader("contentType", "text/xml");
        http.addHeader("datatype", "json");

        StringBuilder url = new StringBuilder();
        String authorizeUrl = "https://accounts.spotify.com/authorize";
        url.append(authorizeUrl).append("?client_id=").append(id).append("&response_type=code").append("%redirect_uri=").append("https://localhost:80").append("&show_dialog=true").append("&scope=user-read-private user-read-email user-modify-playback-state user-read-playback-position");


        StringEntity entity = null;
        try {
            entity = new StringEntity(url.toString());
        } catch(UnsupportedEncodingException e) {
            return null;
        }
        http.setEntity(entity);
        try {
            return super.post(http);
        } catch(IOException e) {
            return null;
        }
    }

}

