package langpendlaren.api.spotify.json.tracks;

import com.fasterxml.jackson.annotation.JsonProperty;



public record TracksJson (Tracks tracks) {
    /**
     * This class represents the unmarshalled reply form the server
     * for the endpoint "/spotify/_______".
     */

    public record Tracks(String href,
                         Items[] items,
                         int limit,
                         String next,
                         int offset,
                         String previous,
                         int total) {}

    public record Items (Album album,
                         Artist[] artists,
                         @JsonProperty("available_markets")
                         String[] availableMarkets,
                         @JsonProperty("disc_number")
                         int discNumber,
                         @JsonProperty("duration_ms")
                         int durationMs,
                         boolean explicit,
                         @JsonProperty("external_ids")
                         ExternalIds externalIds,
                         @JsonProperty("external_urls")
                         ExternalUrls externalUrls,
                         String href,
                         String id,
                         @JsonProperty("is_local")
                         boolean isLocal,
                         String name,
                         int popularity,
                         @JsonProperty("preview_url")
                         String previewUrl,
                         @JsonProperty("track_number")
                         int trackNumber,
                         String type,
                         String uri) {}

    public record Album (String albumType,
                         Artist[] artists,
                         @JsonProperty("available_markets")
                         String[] availableMarkets,
                         @JsonProperty("external_urls")
                         ExternalUrls externalUrls,
                         String href,
                         String id,
                         Images[] images,
                         String name,
                         @JsonProperty("release_date")
                         String releaseDate,
                         @JsonProperty("release_date_precision")
                         String releaseDatePrecision,
                         @JsonProperty("total_tracks")
                         int totalTracks,
                         String type,
                         String uri) {}

    public record Artist (@JsonProperty("external_urls") ExternalUrls externalUrls,
                          String href,
                          String id,
                          String name,
                          String type,
                          String uri) {}

    public record ExternalIds (String spotify) {}

    public record ExternalUrls (String spotify) {}

    public record Images (int height,
                          String url,
                          int width) {}
}
