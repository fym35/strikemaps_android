package eu.konggdev.strikemaps.map.layer;

import com.fasterxml.jackson.databind.JsonNode;

public class MapLayer {
    public JsonNode layer;

    public MapLayer(JsonNode layer) {
	this.layer = layer;
    }
}
