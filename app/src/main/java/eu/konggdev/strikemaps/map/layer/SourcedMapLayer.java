package eu.konggdev.strikemaps.map.layer;

import com.fasterxml.jackson.databind.JsonNode;
import eu.konggdev.strikemaps.map.source.MapSource;

public class SourcedMapLayer {
    public String key;
    public MapSource source;
    public JsonNode layer;

    public SourcedMapLayer(String key, MapSource source, JsonNode layer) {
	this.key = key;
	this.source = source;
	this.layer = layer;
    }
}
