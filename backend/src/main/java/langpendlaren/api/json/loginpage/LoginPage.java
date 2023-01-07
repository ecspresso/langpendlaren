package langpendlaren.api.json.loginpage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class LoginPage {
    @JsonProperty("spotify_auth_uri")
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
