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

    //Anger avgångar från en viss station genom station signature (exempelvis: Cst)
    public String getDepartures(String stationSignature) {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");

        String xml = String.format("""
                <REQUEST> 
                    <LOGIN authenticationkey="%s" />
                    <QUERY objecttype="TrainAnnouncement"  
                        orderby="AdvertisedTimeAtLocation" schemaversion="1"> 
                        <FILTER> 
                        <AND> 
                            <OR> 
                                <AND> 
                                    <GT name="AdvertisedTimeAtLocation"  
                                                value="$dateadd(-00:15:00)" /> 
                                    <LT name="AdvertisedTimeAtLocation"  
                                                value="$dateadd(14:00:00)" /> 
                                </AND> 
                                <GT name="EstimatedTimeAtLocation" value="$now" /> 
                            </OR> 
                            <EQ name="LocationSignature" value="%s" /> 
                            <EQ name="ActivityType" value="Avgang" /> 
                        </AND> 
                        </FILTER>
                        
                        <INCLUDE>AdvertisedTrainIdent</INCLUDE>
                        <INCLUDE>InformationOwner</INCLUDE> 
                        <INCLUDE>AdvertisedTimeAtLocation</INCLUDE> 
                        <INCLUDE>TrackAtLocation</INCLUDE> 
                        <INCLUDE>FromLocation</INCLUDE> 
                        <INCLUDE>ToLocation</INCLUDE> 
                    </QUERY> 
                </REQUEST>
                
                """, apikey, stationSignature);


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

    //Förslag på en annan fråga till trafikverket
    public String getTrainStopStation(int trainId) {
        HttpPost httpPost = new HttpPost("https://api.trafikinfo.trafikverket.se/v2/data.json");
        httpPost.addHeader("contentType", "text/xml");
        httpPost.addHeader("dataType", "json");

        String xml = String.format("""
                               
                <REQUEST>
                      <LOGIN authenticationkey="%s" />
                      <QUERY objecttype="TrainAnnouncement" schemaversion="1.3">
                            <FILTER>
                                  <EQ name="AdvertisedTrainIdent" value="%s" />
                     
                                <OR>
                                      <AND>
                                            <GT name="AdvertisedTimeAtLocation" value="$dateadd(-00:15:00)" />
                                            <LT name="AdvertisedTimeAtLocation" value="$dateadd(14:00:00)" />
                                      </AND>
                                      <AND>
                                            <LT name="AdvertisedTimeAtLocation" value="$dateadd(00:30:00)" />
                                            <GT name="EstimatedTimeAtLocation" value="$dateadd(00:00:00)" />
                                      </AND>
                                </OR>
                                       
                            </FILTER>
                            <INCLUDE>LocationSignature</INCLUDE>
                            <INCLUDE>AdvertisedTimeAtLocation</INCLUDE>
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





