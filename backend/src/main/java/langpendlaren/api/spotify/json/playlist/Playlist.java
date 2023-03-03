package langpendlaren.api.spotify.json.playlist;

public class Playlist {
    private String id;
    private String name;
    private String coverImg;

    public Playlist() {}
    public Playlist(PlaylistJson playlistJson) {
        id = playlistJson.getId();
        name = playlistJson.getName();
        if(playlistJson.getCoverImages().length > 0)
            coverImg = playlistJson.getCoverImages()[0].getUrl();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
