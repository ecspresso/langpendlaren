package langpendlaren.api.spotify.json.spotifyUser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class spotifyUser {

    @JsonProperty("id")
    private String userID;

    @JsonProperty("uri")
    private String uri;
}
