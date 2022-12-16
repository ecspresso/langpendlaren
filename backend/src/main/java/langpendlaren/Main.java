package langpendlaren;

import langpendlaren.webserver.WebServer;

public class Main {
    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.run();
    }
}
