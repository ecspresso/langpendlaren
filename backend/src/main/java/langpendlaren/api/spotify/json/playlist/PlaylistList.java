package langpendlaren.api.spotify.json.playlist;

import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistList {
    private List<PlaylistSimplified> list;
    private String next;
    private String previous;

    public void addPlaylists(String type, Paging<PlaylistSimplified> list) throws MalformedURLException {
        this.list = new ArrayList<>();
        Collections.addAll(this.list, list.getItems());
        String[] fields = new URL(list.getNext()).getQuery().split("&");
        next = null;
        int offset = 0;
        for(int i = 0; i < fields.length && next == null; i++) {
            if(fields[i].contains("offset=")) {
                next = String.format("http://localhost/spotify/search/playlist/%s?%s", type, fields[i]);
                offset = Integer.parseInt(fields[i].split("=")[1]);
            }
        }

        if(offset >= 10) {
            previous = String.format("http://localhost/spotify/search/playlist/%s?offset=%s", type, offset-10);
        }
    }

    public List<PlaylistSimplified> getList() {
        return list;
    }

    public void setList(List<PlaylistSimplified> list) {
        this.list = list;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
