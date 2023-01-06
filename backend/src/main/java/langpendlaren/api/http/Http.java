package langpendlaren.api.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Http {
    protected final HttpHost host;

    protected Http(HttpHost host) {
        this.host = host;
    }

    protected String post(HttpPost httpPost) throws IOException {
        StringBuilder result = new StringBuilder();

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();

                int status = response.getStatusLine().getStatusCode();
                switch (status){
                    case 200: {
                        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
                        String line;
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                        }
                        EntityUtils.consume(entity);
                        break;
                    }
                    case 201:{ result.append("Status: " + status + ", Resursen blev skapad."); break; }
                    case 400:{ result.append("Status: " + status + ", Bad request - Förfrågan kunde nte skikcas eller så saknades nödvändiga förfrågan."); break; }
                    case 401:{ result.append("Status: " + status + ", Unotharized - Ingen tillåtelse att göra förfrågan."); break; }
                    case 404:{ result.append("Status: " + status + ", Not found - resursen finns inte."); break; }
                    case 500:{ result.append("Status: " + status + ", Internt serverfel."); break; }
                }

                System.out.println(httpPost.getEntity().toString() + status);

            }
        }
        return result.toString();
    }
}
