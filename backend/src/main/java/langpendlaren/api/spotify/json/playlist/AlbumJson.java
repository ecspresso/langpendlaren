// package langpendlaren.api.spotify.json.playlist;
//
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.util.ArrayList;
// import java.util.List;
//
// public class AlbumJson {
//     private List<Album> albums;
//     private int totalDurationMs;
//     private String offset;
//
//     public AlbumJson(Paging<se.michaelthelin.spotify.model_objects.specification.Track> tracks) throws URISyntaxException {
//         albums = new ArrayList<>();
//         for(se.michaelthelin.spotify.model_objects.specification.Track t : tracks.getItems()) {
//             String albumId = t.getAlbum().getId();
//             String albumName = t.getAlbum().getName();
//             Album album = new Album(albumId, albumName);
//
//             Track track = new Track(t);
//
//             if(albums.contains(album)) {
//                 int index = albums.indexOf(album);;
//                 albums.get(index).addTrack(track);
//             } else {
//                 album.addTrack(track);
//                 albums.add(album);
//             }
//
//             totalDurationMs += t.getDurationMs();
//         }
//
//         URI uri = new URI(tracks.getNext());
//         for(String query : uri.getQuery().split("&")) {
//             if(query.contains("offset=")) {
//                 offset = query.split("=")[1];
//                 break;
//             }
//         }
//     }
//
//     public List<Album> getAlbums() {
//         return albums;
//     }
//
//     public void setAlbums(List<Album> albums) {
//         this.albums = albums;
//     }
//
//     public int getTotalDurationMs() {
//         return totalDurationMs;
//     }
//
//     public void setTotalDurationMs(int totalDurationMs) {
//         this.totalDurationMs = totalDurationMs;
//     }
//
//     public String getOffset() {
//         return offset;
//     }
//
//     public void setOffset(String offset) {
//         this.offset = offset;
//     }
//
//     static class Album {
//
//         private String id;
//         private String name;
//
//         private List<Track> tracks;
//
//         public Album(String id, String name) {
//             this.id = id;
//             this.name = name;
//         }
//
//         public String getId() {
//             return id;
//         }
//
//         public void setId(String id) {
//             this.id = id;
//         }
//
//         public String getName() {
//             return name;
//         }
//
//         public void setName(String name) {
//             this.name = name;
//         }
//
//         public List<Track> getTracks() {
//             return tracks;
//         }
//
//         public void setTracks(List<Track> tracks) {
//             this.tracks = tracks;
//         }
//
//         public void addTrack(Track track) {
//             if(tracks == null) tracks = new ArrayList<>();
//             tracks.add(track);
//         }
//     }
//
//     static class Track {
//         private String id;
//         private String name;
//         private int durationMs;
//         private String imageUri;
//
//         public Track(se.michaelthelin.spotify.model_objects.specification.Track track) {
//             this.id = track.getId();
//             this.name = track.getName();
//             this.durationMs = track.getDurationMs();
//             this.imageUri = track.getAlbum().getImages()[0].getUrl();
//         }
//
//         public String getId() {
//             return id;
//         }
//
//         public void setId(String id) {
//             this.id = id;
//         }
//
//         public String getName() {
//             return name;
//         }
//
//         public void setName(String name) {
//             this.name = name;
//         }
//
//         public int getDurationMs() {
//             return durationMs;
//         }
//
//         public void setDurationMs(int durationMs) {
//             this.durationMs = durationMs;
//         }
//
//         public String getImageUri() {
//             return imageUri;
//         }
//
//         public void setImageUri(String imageUri) {
//             this.imageUri = imageUri;
//         }
//     }
// }
