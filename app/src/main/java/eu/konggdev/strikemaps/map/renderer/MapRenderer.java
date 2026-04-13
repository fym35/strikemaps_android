package eu.konggdev.strikemaps.map.renderer;

import android.view.View;
import android.view.ViewGroup;

import eu.konggdev.strikemaps.map.layer.MapLayer;
import org.maplibre.android.style.layers.Layer;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.geojson.Feature;

import java.util.List;

public interface MapRenderer {
    void reload();

    View getView();

    List<Feature> featuresAtPoint(LatLng point);
}
