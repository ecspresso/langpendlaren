package langpendlaren.spotify;

import langpendlaren.http.Http;
import org.apache.http.HttpHost;

public class SpotifyAPI extends Http {
    public SpotifyAPI() {
        super(new HttpHost("https://api.trafikinfo.trafikverket.se/v2/data.json"));
    }
}
