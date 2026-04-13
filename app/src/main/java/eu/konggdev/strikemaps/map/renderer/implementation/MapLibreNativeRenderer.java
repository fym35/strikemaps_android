package eu.konggdev.strikemaps.map.renderer.implementation;

import android.view.View;

import androidx.annotation.NonNull;

import eu.konggdev.strikemaps.helper.FileHelper;
import eu.konggdev.strikemaps.helper.UserPrefsHelper;
import eu.konggdev.strikemaps.map.overlay.MapOverlay;
import eu.konggdev.strikemaps.map.layer.MapLayer;
import eu.konggdev.strikemaps.map.renderer.MapRenderer;
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

    void passLayer(MapLayer layer) {
        map.getStyle().addSource(layer.source);
        map.getStyle().addLayer(layer.layer);
    }

    @Override
    public void reload() {
        map.setStyle(new Style.Builder().fromJson(controller.style), style -> {
            for(MapOverlay overlay : controller.overlays.values()) {
                passLayer(overlay.makeLayer());
            }
        });
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

        controller.style = FileHelper.loadStringFromAssetFile(UserPrefsHelper.startupMapStyle(app.getPrefs()), app);

        //I have my own implementation of attribution that credits MapLibre among others, it's not as bad as it looks :)
        map.getUiSettings().setLogoEnabled(false);
        map.getUiSettings().setAttributionEnabled(false);

        map.addOnMapClickListener(point -> controller.onMapClick(point));
        map.addOnMapLongClickListener(point -> controller.onMapLongClick(point));
        this.reload();
    }
}
