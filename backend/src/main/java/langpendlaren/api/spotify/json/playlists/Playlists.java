package langpendlaren.api.spotify.json.playlists;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents the marshalled reply form the server
 * for the endpoint "/spotify/playlists/___?".
 */
public class Playlists {
    private List<UserPlaylists> playlists;

    public void addPlaylists(PlaylistsJson json){
        //List<PlaylistsJson.>
    }


    /**
     * This class represents the individual playlists.
     */
    class UserPlaylists{
        private String pID;
        private boolean isPublic;
        private String scope; //Behövs?

        public String getpID() {
            return pID;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public void setPublic(boolean aPublic) {
            isPublic = aPublic;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public void setpID(String pID) {
            this.pID = pID;
        }
    }
}
