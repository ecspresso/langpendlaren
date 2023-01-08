package langpendlaren.api.spotify.json.tokens;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tokens {
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    public Tokens() {}

    public Tokens(String accessToken, int expiresIn, String refreshToken) {
        this.accessToken = new AccessToken(accessToken, expiresIn);
        this.refreshToken = new RefreshToken(refreshToken);
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
