package langpendlaren.api.trafikverket;

import langpendlaren.api.http.Http;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TrafikverketAPI extends Http {
    public TrafikverketAPI() {
        super(new HttpHost("https://api.trafikinfo.trafikverket.se/v2/data.json"));
        apikey = "ab3f9656870c4625adfaaca4b76caeae";
    }
    private final String apikey;

    public String getTrainStops(int trainId) {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");

        String xml = String.format("""
                <REQUEST>
                    <LOGIN authenticationkey="%s" />
                    <QUERY objecttype="TrainAnnouncement" schemaversion="1.3">
                        <FILTER>
                            <EQ name="AdvertisedTrainIdent" value="%s" />
                        </FILTER>
                    </QUERY>
                </REQUEST>
                """, apikey, trainId);


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
