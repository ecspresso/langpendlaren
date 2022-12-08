package langpendlaren.trafikverket;

import langpendlaren.http.Http;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TrafikverketAPI extends Http {
    public TrafikverketAPI() {
        super(new HttpHost("https://api.trafikinfo.trafikverket.se/v2/data.json"));
    }

    public void post() throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");

        String xml = "<REQUEST>" +
                "<LOGIN authenticationkey=\"ab3f9656870c4625adfaaca4b76caeae\" />" +
                "<QUERY objecttype=\"TrainAnnouncement\" schemaversion=\"1.3\">" +
                "<FILTER>" +
                "<EQ name=\"AdvertisedTrainIdent\" value=\"535\" />" +
                "</FILTER>" +
                "</QUERY>" +
                "</REQUEST>";

        StringEntity entity = new StringEntity(xml);

        httpPost.setEntity(entity);

        try {
            super.post(httpPost);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
