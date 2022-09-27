package smart_room.distributed;

import io.vertx.core.Vertx;
import mqtt_tests.MQTTAgent;
import smart_room.distributed.controller.ControllerImpl;
import smart_room.distributed.controller.DetectSensorAgent;
import smart_room.distributed.controller.LightAgent;
import smart_room.distributed.controller.LuminosityAgent;

public class TestDistributed {
    public static void main(String[] args){
        final LuminositySensorSimulator ls = new LuminositySensorSimulator("LuminositySensor");
        final PresDetectSensorSimulator pd = new PresDetectSensorSimulator("PresenceSensor");
        final LightDeviceSimulator ld = new LightDeviceSimulator("LightDevice");

        ls.init();
        pd.init();
        ld.init();

        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new LuminosityAgent());
        vertx.deployVerticle(new LightAgent(ld));
        vertx.deployVerticle( new DetectSensorAgent());

        final ControllerImpl controller = new ControllerImpl(vertx);
        ls.register(controller);
        pd.register(controller);

    }
}
