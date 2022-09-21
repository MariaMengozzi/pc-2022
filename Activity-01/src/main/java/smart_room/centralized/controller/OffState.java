package smart_room.centralized.controller;

import smart_room.Event;
import smart_room.centralized.LightLevelChanged;
import smart_room.centralized.PresenceDetected;
import smart_room.centralized.PresenceNoMoreDetected;
import smart_room.centralized.SinglelBoardSimulator;

public class OffState implements FiniteStateMachineState{
    private final SinglelBoardSimulator board;
    private boolean presence;
    private double luminosity;

    public OffState(final SinglelBoardSimulator board) {
        this.board = board;
        this.presence = this.board.presenceDetected();
        this.luminosity = board.getLuminosity();
        this.board.off();
    }

    @Override
    public FiniteStateMachineState doAction(Event event) {
        if (event instanceof PresenceDetected){
            this.presence = true;
        } else if (event instanceof PresenceNoMoreDetected) {
            this.presence = false;
        } else if (event instanceof LightLevelChanged){
            this.luminosity = ((LightLevelChanged) event).getNewLevel();
        }
        return updateState();
    }

    private FiniteStateMachineState updateState() {
        if (this.presence && this.luminosity < ControllerImpl.LUM_THRESHOLD){
            return new OnState(this.board);
        }
        return this;
    }
}
