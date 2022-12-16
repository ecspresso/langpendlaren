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
                System.out.println(status);

                BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                EntityUtils.consume(entity);
            }
        }

        return result.toString();
    }
}
