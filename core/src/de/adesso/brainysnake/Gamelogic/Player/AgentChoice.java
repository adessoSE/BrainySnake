package de.adesso.brainysnake.Gamelogic.Player;

public class AgentChoice {

    private boolean hasChosen;
    private AgentMovement agentMovement;

    public AgentChoice(AgentMovement agentMovement) {
        this.agentMovement = agentMovement;
        this.hasChosen = true;
    }

    public AgentChoice(boolean hasChosen, AgentMovement agentMovement) {
        this.hasChosen = hasChosen;
        this.agentMovement = agentMovement;
    }

    public static AgentChoice createNoChoice() {
        return new AgentChoice(false, AgentMovement.NO_MOVE);
    }

    public boolean isHasChosen() {
        return hasChosen;
    }

    public AgentMovement getAgentMovement() {
        return agentMovement;
    }
}
