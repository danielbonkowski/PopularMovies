package android.example.com.popularmovies.data;

public class Trailer {

    private final String id;
    private final String key;
    private final String name;
    private final String site;
    private final int maxVideoQuality;
    private final String type;


    public Trailer(String id, String key, String name,
                   String site, int maxVideoQuality, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
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

    public String getSite() {
        return site;
    }

    public int getMaxVideoQuality() {
        return maxVideoQuality;
    }

    public String getType() {
        return type;
    }

    public String toString(){
        return String.format("Id: %s key: %s name: %s \n" +
                "site: %s quality: %d type: %s", id, key, name, site, maxVideoQuality, type);
    }
}
