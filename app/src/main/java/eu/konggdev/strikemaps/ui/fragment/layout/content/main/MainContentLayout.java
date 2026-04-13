package eu.konggdev.strikemaps.ui.fragment.layout.content.main;

import eu.konggdev.strikemaps.R;
import eu.konggdev.strikemaps.ui.fragment.layout.content.ContentLayout;

public interface MainContentLayout extends ContentLayout {
    default Integer getRegion() {
        return R.id.mainContentView;
    }
}
