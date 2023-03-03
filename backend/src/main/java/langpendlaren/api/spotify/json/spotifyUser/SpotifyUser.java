package langpendlaren.api.spotify.json.spotifyUser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyUser {

    @JsonProperty("id")
    private String userID;

    @JsonProperty("display_name")
    private String userName;

    @JsonProperty("uri")
    private String uri;

    public SpotifyUser() {}

    public SpotifyUser(SpotifyUserJson spotifyUserJson) {
        userID = spotifyUserJson.getId();
        userName = spotifyUserJson.getUserName();
        uri = spotifyUserJson.getUserUri();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
