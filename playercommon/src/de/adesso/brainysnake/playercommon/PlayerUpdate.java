package de.adesso.brainysnake.playercommon;

/**
 * PlayerUpdate wird genutzt um dem Hauptspiel mitzuteilen, wie die Schlange sich im level bewegen will/soll.
 * Hier kann eine Aktion einer Schlange hinterlegt werden. Die Aktion wird vom Hauptspiel validiert und wenn möglich ausgeführt.
 */
public class PlayerUpdate {

    /*
        Über die Orientierung wird festgelegt, in welche Richtung die Schlange sich im nächsten Schritt bewegen soll.
        Die Orientierung ist nicht aus Sicht der Schlange, sondern absolut gesehen.
        Eine Orientierung nach links bspw. beduetet immer nach links, unabhäging von der aktuellen Orientierung der Schlange.
     */
    private Orientation nextStep;

    public PlayerUpdate(Orientation nextStep) {
        this.nextStep = nextStep;
    }

    public Orientation getNextStep() {
        return nextStep;
    }

    public void setNextStep(Orientation nextStep) {
        this.nextStep = nextStep;
    }
}
