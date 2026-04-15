package eu.konggdev.strikemaps.map.overlay.implementation;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import androidx.annotation.NonNull;
import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.map.MapComponent;
import eu.konggdev.strikemaps.map.layer.MapLayer;

import eu.konggdev.strikemaps.map.overlay.MapOverlay;
import eu.konggdev.strikemaps.data.provider.LocationDataProvider;
import org.maplibre.android.style.layers.CircleLayer;
import org.maplibre.android.style.layers.Property;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.FeatureCollection;
import org.maplibre.geojson.Point;

import static org.maplibre.android.style.layers.PropertyFactory.*;

public class LocationOverlay implements MapOverlay, LocationListener {
    LocationDataProvider locationDataProvider;
    AppController app;
    MapComponent map;

    public Location currentLocation = null;

    public LocationOverlay(AppController app) {
        this.app = app;
        this.map = app.getMap();
        this.locationDataProvider = new LocationDataProvider(app.getActivity(), this);
    }

    @Override
    public MapLayer makeLayer() {
        GeoJsonSource source = new GeoJsonSource(
                "location",
                FeatureCollection.fromFeatures(new Feature[]{}) // empty
        );

        if (currentLocation != null)
            source.setGeoJson(Feature.fromGeometry(Point.fromLngLat(currentLocation.getLongitude(), currentLocation.getLatitude())));

        CircleLayer layer = new CircleLayer("location", "location");
        layer.setProperties(
                circleRadius(5f),
                circleColor(Color.parseColor("#1E88E5")),
                circleStrokeColor(Color.WHITE),
                circleStrokeWidth(1.5f),
                circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP)
        );

        return new MapLayer(source, layer);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.currentLocation = location;
        map.onOverlayUpdate();
    }
}