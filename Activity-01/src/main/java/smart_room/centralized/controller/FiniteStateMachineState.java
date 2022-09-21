package smart_room.centralized.controller;

import smart_room.Event;

public interface FiniteStateMachineState {
    FiniteStateMachineState doAction(Event event);
}
