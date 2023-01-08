package langpendlaren.api.spotify.json.spotifyUser;

import com.fasterxml.jackson.annotation.JsonProperty;
public class spotifyUserJson {
    @JsonProperty("RESPONSE")
    private Response response;


    static class Response{
        //Not sure what @JsonProperty to use for the array
        @JsonProperty("display_name")
        private String userName;

        @JsonProperty("uri")
        private String userUri;

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
    }
}
