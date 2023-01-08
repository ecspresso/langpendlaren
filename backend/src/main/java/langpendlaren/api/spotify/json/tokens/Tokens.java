package langpendlaren.api.spotify.json.tokens;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

public class Tokens {
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    public Tokens() {}

    public Tokens(AuthorizationCodeCredentials credentials) {
        this.accessToken = new AccessToken(credentials.getAccessToken(), credentials.getExpiresIn());
        this.refreshToken = new RefreshToken(credentials.getRefreshToken());
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
