package eu.konggdev.strikemaps.map.renderer.implementation;

import android.view.View;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import eu.konggdev.strikemaps.data.helper.UserPrefsHelper;
import eu.konggdev.strikemaps.map.overlay.MapOverlay;
import eu.konggdev.strikemaps.map.layer.SourcedMapLayer;
import eu.konggdev.strikemaps.map.renderer.MapRenderer;
import eu.konggdev.strikemaps.map.style.MapStyle;
import org.maplibre.android.MapLibre;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;
import org.maplibre.geojson.Feature;

import java.util.List;

import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.map.MapComponent;

public class MapLibreNativeRenderer implements MapRenderer, OnMapReadyCallback {
    AppController app;
    MapComponent controller;
    MapLibreMap map;
    final MapView mapView;

    public MapLibreNativeRenderer(AppController app, MapComponent controller) {
        this.app = app;
        this.controller = controller;
        MapLibre.getInstance(app.getActivity());
        this.mapView = new MapView(app.getActivity());
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    public void reload() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        MapStyle style = controller.style;
        try {
	        /* Take metadata from MapStyle
	        everything outside sources, layers */
            ObjectNode root = style.metadata.deepCopy();
            
    	    //Sources
            ObjectNode sources = mapper.createObjectNode();
            style.sources.forEach((k, v) -> sources.set(k, mapper.valueToTree(v)));

	        //Layers
	        ArrayNode layers = mapper.createArrayNode();
    	    layers.addAll((ArrayNode) style.layerDefinitions);

	        //Overlays
	        for (MapOverlay overlay : controller.overlays.values()) {
                SourcedMapLayer overlayLayer = overlay.makeLayer();
		        sources.set(overlayLayer.key, mapper.valueToTree(overlayLayer.source));
		        layers.addAll((ArrayNode) overlayLayer.layer);
	        }

            //Set all to root
	        root.set("sources", sources);
  	        root.set("layers", layers);

            map.setStyle(new Style.Builder().fromJson(mapper.writeValueAsString(root)));
        } catch (Exception e) {
	    app.logcat("Failed to reload Map");
            e.printStackTrace();
        }
    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public List<Feature> featuresAtPoint(LatLng point) {
        return map.queryRenderedFeatures(map.getProjection().toScreenLocation(point));
    }

    @Override
    public void onMapReady(@NonNull MapLibreMap maplibreMap) {
        this.map = maplibreMap;

        controller.setStyle(MapStyle.fromMapLibreJsonFile(UserPrefsHelper.startupMapStyle(app.getPrefs()), app));

        //I have my own implementation of attribution that credits MapLibre among others, it's not as bad as it looks :)
        map.getUiSettings().setLogoEnabled(false);
        map.getUiSettings().setAttributionEnabled(false);

        map.addOnMapClickListener(point -> controller.onMapClick(point));
        map.addOnMapLongClickListener(point -> controller.onMapLongClick(point));
    }
}
