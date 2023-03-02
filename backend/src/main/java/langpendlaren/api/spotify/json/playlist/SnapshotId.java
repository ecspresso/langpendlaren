package langpendlaren.api.spotify.json.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnapshotId {
    @JsonProperty("snapshot_id")
    private String snapshotId;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
}
