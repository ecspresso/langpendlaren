package langpendlaren.api.trafikverket;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import langpendlaren.api.JSON;
import langpendlaren.api.http.Http;
import langpendlaren.api.trafikverket.json.TrainAnnouncement;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TrafikverketAPI extends Http {
    public TrafikverketAPI() {
        super(new HttpHost("https://api.trafikinfo.trafikverket.se/v2/data.json"));
    }

    public TrainAnnouncement postJson() {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");

        String xml = """
                <REQUEST>
                <LOGIN authenticationkey="ab3f9656870c4625adfaaca4b76caeae" />
                <QUERY objecttype="TrainAnnouncement" schemaversion="1.3">
                <FILTER>
                <EQ name="AdvertisedTrainIdent" value="535" />
                </FILTER>
                </QUERY>
                </REQUEST>
                """;

        StringEntity entity = null;
        try {
            entity = new StringEntity(xml);
        } catch(UnsupportedEncodingException e) {
            return null;
        }

        httpPost.setEntity(entity);

        try {
            String json = super.post(httpPost);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return objectMapper.readValue(json, TrainAnnouncement.class);
        } catch(IOException e) {
            return null;
        }
    }

    public String postString() {
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

        StringEntity entity = null;
        try {
            entity = new StringEntity(xml);
        } catch(UnsupportedEncodingException e) {
            return null;
        }

        httpPost.setEntity(entity);

        try {
            return super.post(httpPost);
        } catch(IOException e) {
            return null;
        }
    }
}
