package langpendlaren.api.spotify.json.tracks;

public record Tracks(Items[] items, int offset, int total) {
    public Tracks(TracksJson tracksJson) {
        this(Tracks.setItems(tracksJson.tracks().items()), tracksJson.tracks().offset(), tracksJson.tracks().total());
    }

    private static Items[] setItems(TracksJson.Items[] trackItems) {
        Items[] items = new Items[trackItems.length];
        for(int i = 0; i < trackItems.length; i++) {
            items[i] = new Items(trackItems[i].id(), trackItems[i].name(), trackItems[i].album().images()[0].url(), trackItems[i].durationMs());
        }

        return items;
    }


    public record Items(String id, String name, String imgSrc, int durationMs) {}
}


