package eu.konggdev.strikemaps.ui.screen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Map;

import eu.konggdev.strikemaps.R;
import eu.konggdev.strikemaps.app.AppController;
import eu.konggdev.strikemaps.ui.fragment.ContainerFragment;
import eu.konggdev.strikemaps.ui.fragment.layout.content.main.MainContentLayout;
import eu.konggdev.strikemaps.ui.fragment.popup.Popup;
import eu.konggdev.strikemaps.ui.element.UIRegion;

public class Screen {
    @NonNull AppController app;
    public Screen(AppController app, MainContentLayout mainContent, Map<Integer, UIRegion> regions, Integer layout) {
        this.app = app;
        this.layout = layout;
        this.mainContent = mainContent;
        this.uiRegions = regions;
    }

    private final Integer layout;

    private MainContentLayout mainContent;

    Map<Integer, UIRegion> uiRegions;
    public Integer popup;

    public void open(ContainerFragment fragment) {
        if(fragment instanceof Popup && popup != null) return;

        if(fragment instanceof Popup)
            popup = fragment.getRegion();

        setFragment(uiRegions.get(fragment.getRegion()), fragment.toFragment());
    }

    public void closePopup() {
        if(popup != null) {
            UIRegion popupRegion = uiRegions.get(popup);
            popupRegion.back();
            /* If newFragment is still a popup, assign the current popup value to the new fragment
            otherwise, set the current popup value to null */
            if(popupRegion.getFragment() instanceof Popup) {
                popup = popupRegion.layoutId;
            } else  {
                popup = null;
            }
            setFragment(popupRegion, popupRegion.getFragment());
        }
    }

    public void setFragment(UIRegion region, Fragment fragment) {
        if (region == null) return;

        region.setFragment(fragment);
        fragmentTransaction(region.layoutId, fragment);
    }

    public void fragmentTransaction(int layoutId, Fragment fragment) {
        app.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutId, fragment)
                .commit();
    }

    public void attachAll() {
        app.getActivity().setContentView(layout);
        fragmentTransaction(R.id.mainContentView, mainContent.toFragment());
        for (UIRegion region : uiRegions.values()) {
            setFragment(region, region.getFragment());
        }
    }
}