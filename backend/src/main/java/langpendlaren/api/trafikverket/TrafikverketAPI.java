package langpendlaren.api.trafikverket;

import langpendlaren.api.http.Http;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;


import java.io.IOException;
import java.time.LocalDate;

public class TrafikverketAPI extends Http {
    public TrafikverketAPI() {
        super("https://api.trafikinfo.trafikverket.se/v2/data.json");
        apikey = "ab3f9656870c4625adfaaca4b76caeae";
    }
    private final String apikey;

    public String getTrainStops(int trainId) throws IOException {
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
    public String getDepartures(String stationSignature) throws IOException {
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
    public String getTrainStopStation(String trainId) throws IOException {

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
                                <EQ name="ScheduledDepartureDateTime" value="%s" />
                            </AND>
                        </FILTER>
                        <INCLUDE>LocationSignature</INCLUDE> <!-- stationID -->
                        <INCLUDE>AdvertisedTimeAtLocation</INCLUDE>
                    </QUERY>
                </REQUEST>
                """, apikey, trainId, LocalDate.now());

        return post(xml);
    }

    public String getStationNames() throws IOException {
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
        StringEntity entity = new StringEntity(xml);
        httpPost.setEntity(entity);
        return super.post(httpPost);
    }
}
