package eu.konggdev.strikemaps.map.source;

import com.fasterxml.jackson.databind.JsonNode;

public class MapSource {
    public String url;
    public JsonNode data;
    public String type;
    public String schema;

    public JsonNode tiles;
    public int minzoom;
    public int maxzoom;
    public String scheme;
    public int tileSize;

    public String attribution;

    public String encoding;
    public MapSource() { }
}
