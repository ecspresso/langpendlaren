package langpendlaren.api.spotify.json.seeds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Seeds {
    private List<String> seeds;

    public List<String> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }

    public void setSeeds(String[] seeds) {
        this.seeds = new ArrayList<>();
        Collections.addAll(this.seeds, seeds);
    }
}
