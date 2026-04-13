package eu.konggdev.strikemaps.map.overlay;

import eu.konggdev.strikemaps.map.layer.MapLayer;

/* More or less a data-driven layer factory */
public interface MapOverlay {
    public MapLayer makeLayer();
}
