package de.adesso.brainysnake.playercommon;

import java.util.Collections;
import java.util.List;

/**
 * Everything the player sees in his environment
 */
public final class PlayerView {

   private final List<Field> visibleFields;

    public PlayerView(List<Field> visibleFields) {
        this.visibleFields = visibleFields;
    }

    public List<Field> getVisibleFields() {
        return Collections.unmodifiableList(visibleFields);
    }
}
