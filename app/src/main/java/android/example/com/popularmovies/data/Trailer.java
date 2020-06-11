package android.example.com.popularmovies.data;

public class Trailer {

    private final String id;
    private final String key;
    private final String name;
    private final String playbackSite;
    private final String maxVideoQuality;
    private final String type;


    public Trailer(String id, String key, String name,
                   String playbackSite, String maxVideoQuality, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.playbackSite = playbackSite;
        this.maxVideoQuality = maxVideoQuality;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getPlaybackSite() {
        return playbackSite;
    }

    public String getMaxVideoQuality() {
        return maxVideoQuality;
    }

    public String getType() {
        return type;
    }
}
