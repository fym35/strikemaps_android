package eu.konggdev.strikemaps.map.source;

import com.fasterxml.jackson.databind.JsonNode;

public class MapSource {
    public String url;
    public String type;
    public String schema;
    
    public String attribution;

    /* For raster sources */
    public JsonNode tiles;
    public int minzoom;
    public int maxzoom;

    public MapSource() { }
}
