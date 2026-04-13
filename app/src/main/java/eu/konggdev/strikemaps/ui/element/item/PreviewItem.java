package eu.konggdev.strikemaps.ui.element.item;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import eu.konggdev.strikemaps.ui.UIComponent;
import org.maplibre.geojson.Feature;

import eu.konggdev.strikemaps.R;
public class PreviewItem implements UIItem {
    public String name;
    public String type;
    public Bitmap image;
    boolean hasImage;
    public PreviewItem(String refName, String refType) {
        this.name = refName;
        this.type = refType;
        hasImage = false;
    }
    public PreviewItem(String refName, String refType, Bitmap refImage) {
        this.name = refName;
        this.type = refType;
        this.image = refImage;
        hasImage = true;
    }
    public static PreviewItem fromFeature(Feature feature) {
        return new PreviewItem(feature.getStringProperty("name"), feature.getStringProperty("class"));
    }
    public View makeView(UIComponent spawner) {
        View view = spawner.inflateUi(R.layout.item_preview);
        ((TextView) view.findViewById(R.id.choiceName)).setText(name);
        ((TextView) view.findViewById(R.id.type)).setText(type);
        return view;
    }
    public View makeView(UIComponent spawner, View.OnClickListener onClick) {
        View view = makeView(spawner);
        view.setOnClickListener(onClick);
        return view;
    }
}
