package eu.konggdev.strikemaps.map.style;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.helper.FileHelper;
import eu.konggdev.strikemaps.map.layer.MapLayer;
import eu.konggdev.strikemaps.map.source.MapSource;
import java.util.*;


public class MapStyle {
    public int version;
    public String name;
    public String icon;
    public String description;

    public Map<String, MapSource> sources;
    public List<MapLayer> layers;

    public JsonNode raw;

    //FIXME
    public static MapStyle fromMapLibreJsonFile(String filename, AppController app) {
        String styleContents;
        if (filename.startsWith("/storage")) styleContents = FileHelper.loadStringFromUserFile(filename);
        else styleContents = FileHelper.loadStringFromAssetFile(filename, app);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(styleContents);
            MapStyle style = new MapStyle();

            style.version = root.path("version").asInt();
            style.name = root.path("name").asText();
            style.icon = root.path("icon").asText();
            style.description = root.path("description").asText();

            style.sources = mapper.convertValue(
                    root.path("sources"),
                    new TypeReference<Map<String, MapSource>>() {}
            );

            style.layers = new ArrayList<>();
            for (JsonNode layerNode : root.path("layers")) {

                MapLayer layer = mapper.treeToValue(layerNode, MapLayer.class);

                layer.raw = layerNode; // IMPORTANT

                style.layers.add(layer);
            }

            style.raw = root; // full backup

            return style;
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }
}
