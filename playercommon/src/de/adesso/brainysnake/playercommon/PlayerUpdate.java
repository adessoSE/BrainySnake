package de.adesso.brainysnake.playercommon;

public class PlayerUpdate {

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
