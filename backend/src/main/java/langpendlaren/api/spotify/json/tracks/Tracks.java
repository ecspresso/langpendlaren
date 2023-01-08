package langpendlaren.api.spotify.json.tracks;

import java.util.LinkedList;
import java.util.List;

public class Tracks {
    /**
     * This class represents the marshalled reply from the server
     * for the endpoint "/spotify/_______".
     */
    private List<Tracks> tracks = new LinkedList<>();

    //Add GetSet later


    public void addTracks(TracksJson json){

        //Its so late i cant see straight so ill leave this mess to be cleaned up tomorrow
        //List<TracksJson.TrackID> trackIDList = json.getResponse().getTracksList().get(0).

        /*for(TracksJson.RecommendedTracks tracks : tracks){
            this.tracks.add(new Tracks(
                    tracks
            ))*/
        }
        class Track{
            private String songID;
            private String artist;
            private String title;
            private int durationMS;
        }
}


