package langpendlaren.trafikverket;

import langpendlaren.http.Http;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;

public class API extends Http {
    public void post() {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");
        httpPost.setEntity();

        try {
            super.post(httpPost);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
