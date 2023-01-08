package langpendlaren.api.spotify.json.playlist;

import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.ArrayList;
import java.util.List;

public class AlbumJson {
    private List<Album> album;

    public AlbumJson(Paging<se.michaelthelin.spotify.model_objects.specification.Track> tracks) {
        album = new ArrayList<>();
        // List<Track> trackList = new ArrayList<>();
        for(se.michaelthelin.spotify.model_objects.specification.Track t : tracks.getItems()) {
            Album album = new Album(t.getAlbum().getId(), t.getAlbum().getName());
            this.album.add(album);

        }
    }

    public List<Album> getAlbum() {
        return album;
    }

    public void setAlbum(List<Album> album) {
        this.album = album;
    }


    static class Album {

        private String id;
        private String name;

        public Album(String id, String name) {
            this.id = id;
            this.name = name;
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
    }

    // static class Track {
    //     private String id;
    //     private String name;
    //
    //     public Track(String id, String name) {
    //         this.id = id;
    //         this.name = name;
    //     }
    //
    //     public String getId() {
    //         return id;
    //     }
    //
    //     public void setId(String id) {
    //         this.id = id;
    //     }
    //
    //     public String getName() {
    //         return name;
    //     }
    //
    //     public void setName(String name) {
    //         this.name = name;
    //     }
    // }
}
