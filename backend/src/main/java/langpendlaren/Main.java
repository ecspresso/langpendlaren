package langpendlaren;

import langpendlaren.trafikverket.TrafikverketAPI;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        TrafikverketAPI trafikverketAPI = new TrafikverketAPI();
        try {
            trafikverketAPI.post();
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
