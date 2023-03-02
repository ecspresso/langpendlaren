package langpendlaren.api.spotify.json.tokens;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Tokens {
    @JsonProperty("access_token")
    private AccessToken accessToken;
    @JsonProperty("refresh_token")
    private RefreshToken refreshToken;


    public Tokens(String credentials) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TokensJson tokensJson = mapper.readValue(credentials, TokensJson.class);

        accessToken = new AccessToken(tokensJson.getAccessToken(), tokensJson.getExpiresIn());
        refreshToken = new RefreshToken(tokensJson.getRefreshToken());
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    protected static class AccessToken {
        private String value;
        @JsonProperty("expires_in")
        private int expiresIn;

        public AccessToken() {}

        public AccessToken(String value, int expiresIn) {
            this.value = value;
            this.expiresIn = expiresIn;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    protected static class RefreshToken {
        private String value;

        public RefreshToken() {}

        public RefreshToken(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
