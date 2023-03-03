package langpendlaren.api.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import langpendlaren.api.http.Http;
import langpendlaren.api.spotify.json.spotifyUser.SpotifyUserJson;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SpotifyAPI extends Http {
    // Spotify wrapper
    // Authorize class
    private final Authorize authorize;
    // Lock for synchronize work
    private final Object lock = new Object();
    private final Properties prop;
    private Map<String, String> playListToId = new HashMap();

    public SpotifyAPI() throws URISyntaxException, IOException {
        super("https://api.spotify.com/v1/");
        // Läs in inställningar från fil.
        prop = new Properties();
        FileReader fileReader;

        InputStream stream = getClass().getClassLoader().getResourceAsStream("spotify.properties");
        if(stream == null) {
            throw new IOException("Kunde inte hitta filen spotify.properties");
        } else {
            prop.load(stream);
        }

        // Hemliga saker för vår app.
        String clientId = prop.getProperty("clientId");
        String clientSecret = prop.getProperty("clientSecret");
        String redirect = prop.getProperty("redirect");
        // Var Spotify ska skicka tillbaka användaren.
        URI redirectURI = new URI(redirect);
        // Klass för att hantera Spotify inloggning.
        authorize = new Authorize(clientId, clientSecret, redirectURI);
    }

    /**
     * Get request method with headers.
     * @param endpoint Which endpoint to call.
     * @param headers Headers to add to request.
     * @return String with response.
     * @throws IOException If something goes wrong.
     */
    private String get(String endpoint, Header[] headers) throws IOException {
        HttpGet httpGet = new HttpGet(super.host + endpoint);
        httpGet.addHeader("Content-Type", "application/json");
        for(Header header : headers) {
            httpGet.addHeader(header.name(), header.value());
        }
        return super.get(httpGet);
    }

    private String post(String endpoint, String accessToken, String body, Integer contentLength) throws IOException {
        HttpPost httpPost = new HttpPost(super.host + endpoint);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + accessToken);
        // if(contentLength != null)
        //     httpPost.addHeader("Content-Length", contentLength);
        StringEntity entity = new StringEntity(body);
        httpPost.setEntity(entity);
        return super.post(httpPost);
    }

    private String delete(String endpoint, String accessToken) throws IOException {
        HttpDelete httpDelete = new HttpDelete(super.host + endpoint);
        httpDelete.addHeader("Content-Type", "application/json");
        httpDelete.setHeader("Authorization", "Bearer " + accessToken);
        return super.delete(httpDelete);
    }

    /**
     * List of genres.
     * @param accessToken Access token.
     * @return String with genres.
     * @throws IOException If something goes wrong.
     */
    public String genreSeeds(String accessToken) throws IOException {
        Header[] headers = new Header[1];
        headers[0] = new Header("Authorization", "Bearer " + accessToken);
        return get("recommendations/available-genre-seeds", headers);
    }

    /**
     * User can log in to spotify account.
     * @return link to spotify account
     */
    public URI getLoginPage(){
        return authorize.authorize();
    }

    /**
     * Authentication
     *
     * @param code an id from user acceptation
     * @return AuthorizationCodeCredentials som innehåller access token och refresh token.
     */
    public String auth(String code) throws IOException {
        return authorize.getAccessToken(code);
    }

    public String refreshAccessToken(String refreshToken) throws IOException {
        return authorize.refreshToken(refreshToken);
    }

    // User

    /**
     * Users id //FIXME! return profile.
     *
     * @return user id
     */
    public String getUser(String accessToken) throws IOException {
        Header[] headers = new Header[1];
        headers[0] = new Header("Authorization", "Bearer " + accessToken);
        return get("me", headers);
    }

    // Playlist

    /**
     *
     */
    public String createPlaylist(String accessToken, String name, String description) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SpotifyUserJson spotifyUserJson = mapper.readValue(getUser(accessToken), SpotifyUserJson.class);
        String userId = spotifyUserJson.getId();
        String body = String.format("""
            {
                "name": "%s",
                "description": "%s",
                "public": false
            }
            """, name, description);
        return post(String.format("users/%s/playlists", userId), accessToken, body, 92);
    }

    /**
     * Remove item (tracks) from play list
     *
     * @param playlistId  playlist id
     * @param trackId track id
     */
    public String removeItemFromPlayList(String accessToken, String playlistId, String trackId) throws IOException {
        String body = String.format("{\"tracks\": [{\"uri\": \"spotify:track:%s\"}]}", trackId);
        return post(String.format("playlists/%s/tracks", playlistId), accessToken, body, null);
    }

    /**
     * Delete a play list by id
     * @param id playlist id to unfollow
     */
    public void deletePlayList(String accessToken, String id) throws IOException {
        System.out.println("deletePlaylist: " + delete(String.format("playlists/%s/followers", id), accessToken));
    }

    /**
     * Add a track to play list.
     *
     * @param playlistId playlist id
     * @param trackId track id
     * @return confirmation
     */
    public String addToPlayList(String accessToken, String playlistId, String trackId) throws IOException {
        String body = String.format("""
            {
                "uris": [
                    "spotify:track:%s"
                ]
            }
            """, trackId);
        return post(String.format("playlists/%s/tracks", playlistId), accessToken, body, 49);
    }



    // /**
    //  * Get albums by ids
    //  * @param ids album ids
    //  * @return list of albums
    //  */
    // public ArrayList<Map> getAlbums(String accessToken, String ids) {
    //     Header[] headers = new Header[1];
    //     headers[0] = new Header("Authorization", "Bearer " + accessToken);
    //     return get("albums", headers);
    //
    //     spotifyApiWrapper.setAccessToken(accessToken);
    //
    //     final GetSeveralAlbumsRequest getSeveralAlbumsRequest = this.spotifyApiWrapper.getSeveralAlbums(ids)
    //             .build();
    //     try {
    //         final Album[] albums = getSeveralAlbumsRequest.execute();
    //         //FIXME! convert into json before returning all albums.
    //         System.out.println("Length: " + albums.length);
    //         ArrayList<Map> albumList = new ArrayList<>();
    //         Map map = new HashMap<>();
    //         for(Album album : albums){
    //             map.put("id", album.getId());
    //             map.put("Name", album.getName());
    //             map.put("Type", album.getType());
    //         }
    //         albumList.add(map);
    //         return albumList;
    //     } catch (IOException | SpotifyWebApiException | ParseException e) {
    //         System.out.println("Error: " + e.getMessage());
    //         return null;
    //     }
    // }

    // /**
    //  * Get an album by id
    //  * @param id albums id
    //  * @return an album
    //  */
    // public String getAlbumById(String accessToken, String id) {
    //     spotifyApiWrapper.setAccessToken(accessToken);
    //     final GetAlbumRequest getAlbumRequest = this.spotifyApiWrapper.getAlbum(id).build();
    //     try {
    //         final Album album = getAlbumRequest.execute();
    //         System.out.println("Name: " + album.getName());
    //         return album.getName();
    //     } catch (IOException | SpotifyWebApiException | ParseException e) {
    //         System.out.println("Error: " + e.getMessage());
    //         return null;
    //     }
    // }

    /**
     * Search tracks by name
     *
     * @return a list of track
     */
    public String searchTracks(String accessToken, String genre) throws IOException {
        Header[] headers = new Header[1];
        headers[0] = new Header("Authorization", "Bearer " + accessToken);
        return get(String.format("search?q=genre=%s&type=track&limit=10&offset=0&include_external=audio", genre), headers);
    }

    // /**
    //  * Get an artist by id
    //  * @param id artist id
    //  * @return artist
    //  */
    // public String getArtist(String accessToken, String id) {
    //     spotifyApiWrapper.setAccessToken(accessToken);
    //
    //     // Behöver vi verklingen denna metoden?
    //     final GetArtistRequest getArtistRequest = this.spotifyApiWrapper.getArtist(id)
    //             .build();
    //     try {
    //         final Artist artist = getArtistRequest.execute();
    //
    //         System.out.println("Name: " + artist.getName());
    //         return artist.getName();
    //     } catch (IOException | SpotifyWebApiException | ParseException e) {
    //         System.out.println("Error: " + e.getMessage());
    //         return null;
    //     }
    // }

    // public Paging<PlaylistSimplified> searchPlayList(String accessToken, String type) throws IOException, ParseException, SpotifyWebApiException {
    //     spotifyApiWrapper.setAccessToken(accessToken);
    //
    //     SearchPlaylistsRequest searchPlaylistsRequest = this.spotifyApiWrapper.searchPlaylists(type)
    //       .market(CountryCode.SE)
    //       .limit(10)
    //       .offset(0)
    //       .includeExternal("audio")
    //             .build();
    //     return searchPlaylistsRequest.execute();
    // }

    public String getPlaylistByPlaylistId(String accessToken, String playlistId) throws IOException {
        Header[] headers = new Header[1];
        headers[0] = new Header("Authorization", "Bearer " + accessToken);
        return get(String.format("playlists/%s", playlistId), headers);
    }

    private record Header(String name, String value) {}
}

