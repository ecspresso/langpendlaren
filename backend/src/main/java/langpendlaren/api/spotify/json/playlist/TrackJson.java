package langpendlaren.api.spotify.json.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TrackJson(Playlist playlist, @JsonProperty("snapshot_id") String snapshotId) {
    public TrackJson(String snapshotId, String playlistId, String trackId) {
        this(createPlaylist(playlistId, trackId), snapshotId);
    }

    private static Playlist createPlaylist(String playlistId, String trackId) {
        Track track = new Track(trackId);
        return new Playlist(playlistId, track);
    }

    record Playlist(String id, Track track) {}

    record Track(String id) {}
}
