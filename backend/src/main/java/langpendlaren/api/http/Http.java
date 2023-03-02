package langpendlaren.api.http;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;

public abstract class Http {
    protected final String host;
    private final HttpClientBuilder httpBuilder = HttpClientBuilder.create();

    protected Http(String host) {
        this.host = host;
    }

    protected String post(HttpPost httpPost) throws IOException {
        try(CloseableHttpClient client = httpBuilder.build()) {
            BasicHttpClientResponseHandler httpHandler = new BasicHttpClientResponseHandler();
            return client.execute(httpPost, httpHandler);
        }
    }

    protected String get(HttpGet httpGet) throws IOException {
        try(CloseableHttpClient client = httpBuilder.build()) {
            BasicHttpClientResponseHandler httpHandler = new BasicHttpClientResponseHandler();
            return client.execute(httpGet, httpHandler);
        }
    }

    protected String delete(HttpDelete httpDelete) throws IOException {
        try(CloseableHttpClient client = httpBuilder.build()) {
            BasicHttpClientResponseHandler httpHandler = new BasicHttpClientResponseHandler();
            return client.execute(httpDelete, httpHandler);
        }
    }
}
