package eu.konggdev.strikemaps.ui;

import android.app.AlertDialog;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.common.collect.BiMap;
import eu.konggdev.strikemaps.Component;
import eu.konggdev.strikemaps.R;
import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.map.MapComponent;
import eu.konggdev.strikemaps.ui.element.UIRegion;
import eu.konggdev.strikemaps.ui.fragment.layout.FragmentLayoutControls;
import eu.konggdev.strikemaps.ui.fragment.layout.content.main.FragmentLayoutContentSettings;
import eu.konggdev.strikemaps.ui.screen.Screen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UIComponent implements Component {
    @NonNull AppController app;
    private Map<Integer, Screen> screens;
    private Integer currentScreen;

    public UIComponent(AppController app, MapComponent map) {
        this.app = app;
        this.screens = Map.of(
                //Main screen
                R.layout.screen_main, new Screen(
                        //App reference
                        app,
                        //Map view
                        map.toFragment(), //FragmentLayoutContentMap
                        //Main screen init regions definition
                        Map.of(R.id.bottomUi, new UIRegion(new FragmentLayoutControls(app, R.id.bottomUi), R.id.bottomUi)), //TODO: Probably stop referencing layout 3(!) times everytime
                        //Layout
                        R.layout.screen_main //TODO: Define this for the Screen without duplicating the reference
                ),
                //Settings screen
                R.layout.screen_settings, new Screen(
                        app,
                        //Settings
                        new FragmentLayoutContentSettings(),
                        /* No regions defined in settings
                           Entire screen is just the main view */
                        new HashMap<>(),
                        //Layout
                        R.layout.screen_settings
                )
        );
    }

    public void swapScreen(Integer screen) {
        currentScreen = screen;
        getCurrentScreen().attachAll();
    }

    public Screen getCurrentScreen() {
        return getScreen(currentScreen);
    }

    public Screen getScreen(Integer screen) {
        return screens.get(screen);
    }

    public void alert(AlertDialog dialog) {
        dialog.show();
    }

    public <T> void alert(AlertDialog dialog, Consumer<T> callback) {
        dialog.show();
    }

    public View inflateUi(int layout) {
        return app.getActivity().getLayoutInflater().inflate(layout, null);
    }
}
