package langpendlaren.api.spotify.json.spotifyUser;

import com.fasterxml.jackson.annotation.JsonProperty;
public class SpotifyUserJson {
    @JsonProperty("display_name")
    private String userName;

    @JsonProperty("uri")
    private String userUri;

    private String id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
