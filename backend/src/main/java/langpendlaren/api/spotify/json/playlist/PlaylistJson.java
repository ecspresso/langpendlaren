package langpendlaren.api.spotify.json.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaylistJson {
    private String id;
    private String name;
    @JsonProperty("images")
    private CoverImage[] coverImages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoverImage[] getCoverImages() {
        return coverImages;
    }

    public void setCoverImages(CoverImage[] coverImages) {
        this.coverImages = coverImages;
    }

    public static class CoverImage {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}