package eu.konggdev.strikemaps.map.overlay.implementation;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import androidx.annotation.NonNull;
import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.map.MapComponent;
import eu.konggdev.strikemaps.map.layer.SourcedMapLayer;
import eu.konggdev.strikemaps.map.overlay.MapOverlay;
import eu.konggdev.strikemaps.map.source.MapSource;

import eu.konggdev.strikemaps.data.provider.LocationDataProvider;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.FeatureCollection;
import org.maplibre.geojson.Point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
    public SourcedMapLayer makeLayer() {
	MapSource source = new MapSource();

	source.type = "geojson";

	ObjectMapper mapper = new ObjectMapper();
	try {
	    ObjectNode data = mapper.createObjectNode();
	    data.put("type", "Feature");

	    if(currentLocation != null) {
	       ObjectNode geometry = mapper.createObjectNode();
               geometry.put("type", "Point");

	       ArrayNode coordinates = mapper.createArrayNode();
	       coordinates.add(currentLocation.getLongitude());
	       coordinates.add(currentLocation.getLatitude());

	       geometry.set("coordinates", coordinates);
	       data.set("geometry", geometry);
	       data.set("properties", mapper.createObjectNode());
	    }
	    source.data = data;
	} catch (Exception e) {
	    e.printStackTrace();
	}

	ObjectNode layer = mapper.createObjectNode();
	layer.put("id", "location");
	layer.put("type", "circle");
	layer.put("source", "location");

	ObjectNode paint = mapper.createObjectNode();
	paint.put("circle-radius", 5);
	paint.put("circle-color", "#1E88E5");
	paint.put("circle-stroke-color", "#FFFFFF");
	paint.put("circle-stroke-width", 1.5);

	layer.set("paint", paint);

	ObjectNode layout = mapper.createObjectNode();
	layout.put("circle-pitch-alignment", "map");

	layer.set("layout", layout);
	
	ArrayNode layers = mapper.createArrayNode();
	layers.add(layer);

        return new SourcedMapLayer("location", source, layers);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.currentLocation = location;
        map.onOverlayUpdate();
    }
}
