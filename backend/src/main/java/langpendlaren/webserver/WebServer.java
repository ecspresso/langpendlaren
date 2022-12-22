package langpendlaren.webserver;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import langpendlaren.api.spotify.SpotifyEndPoints;
import langpendlaren.api.trafikverket.TrafikverketEndPoints;

public class WebServer {
    private final Javalin javalin;
    private final Integer port = 80;
    public WebServer() {
        javalin = Javalin.create(config -> config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost)));

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
