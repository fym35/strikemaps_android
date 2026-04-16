package eu.konggdev.strikemaps.map.overlay;

import eu.konggdev.strikemaps.map.layer.SourcedMapLayer;

/* More or less a data-driven layer factory */
public interface MapOverlay {
    public SourcedMapLayer makeLayer();
}
