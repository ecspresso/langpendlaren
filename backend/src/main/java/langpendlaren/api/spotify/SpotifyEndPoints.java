package langpendlaren.api.spotify;

import io.javalin.Javalin;
import java.net.URISyntaxException;

public class SpotifyEndPoints {
    private Javalin javalin;
    private final SpotifyAPI spotifyAPI;

    public SpotifyEndPoints(Javalin javalin){
        this.javalin = javalin;
        try {
            spotifyAPI = new SpotifyAPI();
        } catch(URISyntaxException e) {
            throw new RuntimeException(e);
        }

        endPoints();
    }

    /**
     * All end points will be handled in this function
     */
    public void endPoints(){

        // Authorization
        javalin.get("/spotify/login", context -> context.json(spotifyAPI.getLoginPage()));
        javalin.post("/spotify/auth/{code}", context -> {
            String code = context.pathParam("code");
            spotifyAPI.auth(code);
        });


        // -- User
        javalin.get("/spotify/user/profile", context -> context.json(spotifyAPI.getUserId())); //FIXME! a possible way can be to return all profile

        // -- Playlist
        javalin.post("/spotify/playlist/create/{name}/{des}", context -> {
            String name = context.pathParam("name");
            String des = context.pathParam("des");
            spotifyAPI.createPlayList(name, des);
        });
        javalin.post("/spotify/playlist/delete/{id}", context -> {
            String id = context.pathParam("id");
            spotifyAPI.deletePlayList(id);
        });
        javalin.put("/spotify/playlist/add/{pid}/{tid}", context -> {
            String pid = context.pathParam("pid");
            String tid = context.pathParam("tid");
            spotifyAPI.addToPlayList(pid, tid);
        });
        javalin.get("/spotify/playlist/{id}", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getPlayListById(id));
        });
        javalin.get("/spotify/playlist/all", context -> context.json(spotifyAPI.getCurrentPlayLists()));

        // -- Albums
        javalin.get("/spotify/album/{id}", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getAlbumById(id));
        });
        javalin.get("/spotify/album/{ids}", context ->{
            String[] ids = context.pathParam("ids");
            context.json(spotifyAPI.getAlbums(ids));
        });

        // -- Search
        javalin.get("/spotify/search/playlist/{name}", context -> {
            String name = context.pathParam("name");
            context.json(spotifyAPI.searchPlaylist(name));
        });
        javalin.get("/spotify/search/track/{name}", context -> {
            String name = context.pathParam("name");
            context.json(spotifyAPI.searchTracks(name));
        });

        // -- Artists
        javalin.get("/spotify/artist/profile/{id}", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getArtist(id));
        });
        javalin.get("/spotify/artist/{id}/toptrack", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getTopTruckByArtistId(id));
        });
        javalin.get("/spotify/artist/albums/{id}", context -> {
            String id = context.pathParam("id");
            context.json(spotifyAPI.getArtistAlbums(id));
        });
    }
}
