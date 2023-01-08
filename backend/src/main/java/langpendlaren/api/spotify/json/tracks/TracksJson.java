package langpendlaren.api.spotify.json.tracks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class TracksJson {
    /**
     * This class represents the unmarshalled reply form the server
     * for the endpoint "/spotify/_______".
     */

    @JsonProperty("RESPONSE")
    private Response response;

    public void setResponse(Response response){this.response = response;}
    public Response getResponse(){
        return response;
    }
    static class Response{
        /**
         * The property holds the array RESULT with the corresponding track objects.
         */
        @JsonProperty
        private List<RecommendedTracks> tracksList;

        public List<RecommendedTracks> getTracksList(){
            return tracksList;
        }
        public void setTracksList(List<RecommendedTracks> tracksList){
            this.tracksList = tracksList;
        }
    }

    /**
     * This class represents the song track object.
     */
    static class RecommendedTracks{
        @JsonProperty("tracks")
        private List<TrackID> trackIDS; //this cant be right

        @JsonProperty("duration_ms")
        private int duration;

        public void setDuration(int duration){
            this.duration = duration;
        }
        public int getDuration(){
            return duration;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class TrackID{
        @JsonProperty("id")
        private String id;      //The unique song ID (ex."spotify:track:4iV5W9uYEdYUVa79Axb7Rh")

        public void setID(String id){
            this.id = id;
        }
        public String getID(){
            return id;
        }
    }

}
