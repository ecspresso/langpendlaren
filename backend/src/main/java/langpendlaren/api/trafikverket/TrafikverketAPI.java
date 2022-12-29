package langpendlaren.api.trafikverket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import langpendlaren.api.http.Http;
import langpendlaren.api.trafikverket.json.StationShortNames;
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

        return post(xml);
    }

    //Anger avgångar från en viss station genom station signature (exempelvis: Cst)
    public String getDepartures(String stationSignature) {
        String xml = String.format("""
                <REQUEST>
                    <LOGIN authenticationkey='%s' />
                        <QUERY objecttype='TrainAnnouncement' orderby='AdvertisedTimeAtLocation' schemaversion='1'>
                            <FILTER>
                                <AND>
                                    <OR>
                                        <AND>
                                            <GT name='AdvertisedTimeAtLocation' value='$dateadd(-00:15:00)' />
                                            <LT name='AdvertisedTimeAtLocation' value='$dateadd(14:00:00)' />
                                        </AND>
                                        <GT name='EstimatedTimeAtLocation' value='$now' />
                                    </OR>
                                    <EQ name='LocationSignature' value='%s' />
                                    <EQ name='ActivityType' value='Avgang' />
                                </AND>
                            </FILTER>
                        <INCLUDE>InformationOwner</INCLUDE>
                        <INCLUDE>AdvertisedTimeAtLocation</INCLUDE>
                        <INCLUDE>TrackAtLocation</INCLUDE>
                        <INCLUDE>FromLocation</INCLUDE>
                        <INCLUDE>ToLocation</INCLUDE>
                        <INCLUDE>AdvertisedTrainIdent</INCLUDE>
                    </QUERY>
                </REQUEST>
                """, apikey, stationSignature);
        return post(xml);
    }

    //Hämtar alla destinationer för ett tåg
    public String getTrainStopStation(String trainId) {

        String xml = String.format("""
                <REQUEST>
                    <LOGIN authenticationkey="%s" />
                    <QUERY objecttype="TrainAnnouncement" orderby="AdvertisedTimeAtLocation" schemaversion="1.3">
                        <FILTER>
                            <AND>
                                <EQ name="AdvertisedTrainIdent" value="%s"/> <!-- value = tågID -->
                                <EQ name="Advertised" value="true" />
                                <EQ name="ActivityType" value="Avgang"/> <!-- Hämta endast avgångar -->
                                <!-- value = måste vara dagens datum annars hämtas inget -->
                                <EQ name="ScheduledDepartureDateTime" value="$now" />
                            </AND>
                        </FILTER>
                        <INCLUDE>LocationSignature</INCLUDE> <!-- stationID -->
                        <INCLUDE>AdvertisedTimeAtLocation</INCLUDE>
                    </QUERY>
                </REQUEST>
                """, apikey, trainId);

        return post(xml);
    }

    public StationShortNames getStationShortNames() {
        String xml = String.format("""     
                <REQUEST>
                      <LOGIN authenticationkey="%s" />
                      <QUERY objecttype="TrainStation" schemaversion="1">
                            <FILTER>
                                  <EQ name="Advertised" value="true" />
                            </FILTER>
                            <INCLUDE>AdvertisedLocationName</INCLUDE>
                            <INCLUDE>LocationSignature</INCLUDE>
                      </QUERY>
                </REQUEST>
                """, apikey);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(post(xml), StationShortNames.class);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStationNames() {
        String xml = String.format("""     
                <REQUEST>
                    <LOGIN authenticationkey='%s'/>
                    <QUERY objecttype='TrainStation' schemaversion='1'>
                        <FILTER/>
                        <INCLUDE>Prognosticated</INCLUDE>
                        <INCLUDE>AdvertisedLocationName</INCLUDE>
                        <INCLUDE>LocationSignature</INCLUDE>
                    </QUERY>
                </REQUEST>
                """, apikey);

        return post(xml);
    }

    private String post(String xml) {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");

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





