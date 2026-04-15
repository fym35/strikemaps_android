package eu.konggdev.strikemaps.map.layer;

import org.maplibre.android.style.layers.Layer;
import org.maplibre.android.style.sources.GeoJsonSource;


//FIXME: Get rid of reliance on MapLibre!
//Most likely implement an "AdditionalMapLayer" or something of that sorts (?)
public class MapLayer {
    public GeoJsonSource source;
    public Layer layer;
    public MapLayer(GeoJsonSource source, Layer layer) {
        this.source = source;
        this.layer = layer;
    }
}