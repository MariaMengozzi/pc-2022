package smart_room.distributed.controller;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import smart_room.Controller;
import smart_room.Event;
import smart_room.distributed.LightLevelChanged;
import smart_room.distributed.PresenceDetected;
import smart_room.distributed.PresenceNoMoreDetected;

public class ControllerImpl implements Controller {
    private static final String PRESENCE_ADDRESS = "presence";
    private static final String LUMINOSITY_ADDRESS = "light";
    private final EventBus eventBus;

    public ControllerImpl(final Vertx vertx) {
        this.eventBus = vertx.eventBus();
    }

    @Override
    public void notifyEvent(Event ev) {
        if (ev instanceof PresenceDetected) {
            this.eventBus.publish(PRESENCE_ADDRESS, String.valueOf(true));
        } else if (ev instanceof PresenceNoMoreDetected) {
            this.eventBus.publish(PRESENCE_ADDRESS, String.valueOf(false));
        } else if (ev instanceof LightLevelChanged) {
            this.eventBus.publish(LUMINOSITY_ADDRESS, String.valueOf(((LightLevelChanged) ev).getNewLevel()));
        }
    }
}
