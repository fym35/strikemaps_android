package eu.konggdev.strikemaps.ui.element.item;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import eu.konggdev.strikemaps.R;
import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.helper.FileHelper;
import eu.konggdev.strikemaps.map.MapComponent;
import eu.konggdev.strikemaps.ui.UIComponent;
import org.json.JSONObject;

import java.io.InputStream;

public class GenericItem implements UIItem {
    @NonNull public String name;
    public Bitmap image;
    public Runnable onClick;
    boolean hasImage;
    public GenericItem(String refName) {
        this.name = refName;
        hasImage = false;
    }
    public GenericItem(String refName, Runnable onClick) {
        this.name = refName;
        this.onClick = onClick;
        hasImage = false;
    }
    public GenericItem(String refName, Bitmap refImage) {
        this.name = refName;
        this.image = refImage;
        hasImage = true;
    }
    public GenericItem(String refName, Bitmap refImage, Runnable onClick) {
        this.name = refName;
        this.image = refImage;
        this.onClick = onClick;
        hasImage = true;
    }

    //FIXME: Ugly glue static constructor
    public final static GenericItem fromStyle(String style, AppController app, MapComponent map) {
        try {
            JSONObject styleJson = new JSONObject(style);
            String name = "Unknown"; //Fallback name
            if (styleJson.has("name")) name = styleJson.getString("name");
            if (styleJson.has("icon")) {
                switch(styleJson.getString("icon").split("//")[0]) {
                    //TODO: https
                    case "assets:":
                        Bitmap icon = BitmapFactory.decodeStream(FileHelper.openAssetStream("bundled/icon/" + styleJson.getString("icon").split("//")[1], app));
                        return new GenericItem(name, icon, () -> map.setStyle(style));
                    default:
                        app.logcat("Unimplemented icon source requested in style: " + name);
                        return new GenericItem(name, () -> map.setStyle(style));
                }
            }
            return new GenericItem(name, () -> map.setStyle(style));
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericItem("Exception!", () -> map.setStyle(style));
        }
    }
    public View makeView(UIComponent spawner) {
        View v = spawner.inflateUi(R.layout.item_generic);
        //FIXME: These shouldn't be casted like that!
        ((TextView) v.findViewById(R.id.name)).setText(name);
        if(image != null) ((ImageButton) v.findViewById(R.id.image)).setImageBitmap(image);
        if(onClick != null) v.findViewById(R.id.image).setOnClickListener(click(onClick));

        return v;
    }
}
