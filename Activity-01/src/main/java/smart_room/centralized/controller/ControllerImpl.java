package smart_room.centralized.controller;

import smart_room.Controller;
import smart_room.Event;
import smart_room.centralized.SinglelBoardSimulator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerImpl extends Thread implements Controller {
    public static final Double LUM_THRESHOLD = 0.5;
    private final BlockingQueue<Event> eventQueue;
    private FiniteStateMachineState currentState;

    public ControllerImpl(final SinglelBoardSimulator board) {
        this.eventQueue = new LinkedBlockingQueue<>();
        this.currentState = new OffState(board);
    }

    //EVENT LOOP
    @Override
    public void run() {
        while (true) {
            try {
                //wait for a new event
                final Event newEvent = this.eventQueue.take();
                // perform the handler for the event that cause an update of the current state.
                this.currentState = this.currentState.doAction(newEvent);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void notifyEvent(Event ev) {
        // a new event is occurred, so I add it to the event queue
        this.eventQueue.add(ev);
    }
}
