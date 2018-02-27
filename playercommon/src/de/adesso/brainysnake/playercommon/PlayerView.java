package de.adesso.brainysnake.playercommon;

import java.util.List;

/**
 * Die PlayerView bildet das Sichtfeld einer Schlange. Es ist quadratisch angeordnet und befindet sich genau auf Köpfhohe der Schlange. Die einzelnen Felder
 * sind als Liste im PlayerView gespeichert. Das erste Feld in der Liste befindet sich immer hinten links aus Sicht der Schlange (Sichtweite + linker Rand der
 * Feldbreite). Das letzte Element der List ist der Punkt vorne Rechts.
 */

// .....___5____
// ....|4......24|
// ....|3.8....23|
// ..5.|2.7..<|22|SchlangenKörper
// ....|1.6......|
// ....|0_5______|
public final class PlayerView {

    /*
     * Feldbreite in Feldern aus Sicht der Schlange. Bsp.: Eine Feldbreite von 5 bedeutet, dass die Schlange 2 Felder nach links und 2 Felder nach rechts inkl.
     * dem Feld vor dem eigenen Kopf sieht.
     */
    private final int viewWidth;

    // Anzahl der Felder, die die Schlange nach vorne gucken kann
    private final int visibilityRange;

    // Aktuelle Orientierung der Schlange nach oben, unten, links oder rechts
    private final Orientation currentOrientation;

    /*
     * Enthält alle Felder, die die Schlange von seiner aktuellen Position aus sehen kann. Aus Sicht der Schlange ist das erste Feld in der Liste das Feld
     * hinten links (Sichtweite).
     */
    private final List<Field> visibleFields;

    public PlayerView(List<Field> visibleFields, Orientation currentOrientation, int viewWidth, int visibilityRange) {
        this.visibleFields = visibleFields;
        this.currentOrientation = currentOrientation;
        this.viewWidth = viewWidth;
        this.visibilityRange = visibilityRange;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getVisibilityRange() {
        return visibilityRange;
    }

    public Orientation getCurrentOrientation() {
        return currentOrientation;
    }

    public List<Field> getVisibleFields() {
        return visibleFields;
    }
}
