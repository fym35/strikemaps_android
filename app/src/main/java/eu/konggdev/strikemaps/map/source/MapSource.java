package eu.konggdev.strikemaps.map.source;

public class MapSource {
    public final String url;
    public final String type;
    public final String schema;
    public MapSource(String url, String type, String schema) {
        this.url = url;
        this.type = type;
        this.schema = schema;
    }
}
