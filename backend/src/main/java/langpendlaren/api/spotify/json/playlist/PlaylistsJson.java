package langpendlaren.api.spotify.json.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents the unmarshalled reply from Spotify
 * for the endpoint ____?
 */
public class PlaylistsJson {
    @JsonProperty("RESPONSE")
    private Response response;

    static class Response{
        @JsonProperty("items")
        private List<items> userPList;
       // public List<userPList>
    }

    static class items{
        private List<Identifiers> identifiers;


    }

    static class Identifiers {
        @JsonProperty("collaborative")
        private boolean isCollab;

        @JsonProperty("description")
        private String description;

        @JsonProperty("id")
        private String ID;
    }

}
