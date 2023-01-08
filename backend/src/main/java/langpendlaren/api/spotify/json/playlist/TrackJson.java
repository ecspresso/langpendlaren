package langpendlaren.api.spotify.json.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrackJson {
    private Playlist playlist;
    @JsonProperty("snapshot_id")
    private String snapshotId;

    public TrackJson() {};

    public TrackJson(String snapshotId, String playlistId, String trackId) {
        this.snapshotId = snapshotId;

        Track track = new Track();
        track.setId(trackId);

        playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setTrack(track);
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    static class Playlist {
        private String id;
        private Track track;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Track getTrack() {
            return track;
        }

        public void setTrack(Track track) {
            this.track = track;
        }
    }

    static class Track {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
