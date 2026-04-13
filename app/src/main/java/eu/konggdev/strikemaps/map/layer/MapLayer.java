package eu.konggdev.strikemaps.map.layer;

import org.maplibre.android.style.layers.Layer;
import org.maplibre.android.style.sources.GeoJsonSource;

import java.util.List;

//TOOD: Make not strictly MapLibre reliant
public class MapLayer {
    public GeoJsonSource source;
    public Layer layer;
    public MapLayer(GeoJsonSource source, Layer layer) {
        this.source = source;
        this.layer = layer;
    }
}
