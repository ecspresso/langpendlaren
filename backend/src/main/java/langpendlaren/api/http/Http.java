package langpendlaren.api.http;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;

public abstract class Http {
    protected final String host;

    protected Http(String host) {
        this.host = host;
    }

    protected String post(HttpPost httpPost) {
        HttpClientBuilder httpBuilder = HttpClientBuilder.create();

        try(CloseableHttpClient client = httpBuilder.build()) {
            BasicHttpClientResponseHandler httpHandler = new BasicHttpClientResponseHandler();
            return client.execute(httpPost, httpHandler);
        } catch(IOException e) {
            return e.getMessage();
        }
    }
}
