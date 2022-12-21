package langpendlaren.webserver;

import io.javalin.Javalin;
import langpendlaren.api.spotify.SpotifyAPI;
import langpendlaren.api.spotify.SpotifyEndPoints;
import langpendlaren.api.trafikverket.TrafikverketAPI;
import langpendlaren.api.trafikverket.TrafikverketEndPoints;

import java.net.URISyntaxException;

public class WebServer {
    private final Javalin javalin;
    private final Integer port = 8080;
    public WebServer() {
        javalin = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        });

        // Run APIs
        new TrafikverketEndPoints(javalin);
        new SpotifyEndPoints(javalin);
    }

    /**
     * Start the server
     */
    public void run() {
        javalin.start(port);
    }

}
