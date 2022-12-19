package langpendlaren;

import langpendlaren.webserver.WebServer;

import java.io.IOException;
import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws IOException {
        WebServer webServer = new WebServer();
        webServer.run();
    }
}
