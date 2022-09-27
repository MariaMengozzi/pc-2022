package smart_room.distributed.controller;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.mqtt.MqttClient;

public class LuminosityAgent extends AbstractVerticle {
    private static final String TOPIC = "smartRoom/luminosity";
    private static final String ADDRESS = "light";
    private EventBus eventBus;

    @Override
    public void start() {
        this.eventBus = this.getVertx().eventBus();
        MqttClient client = MqttClient.create(this.getVertx());

        client.connect(1883, "broker.mqtt-dashboard.com", c -> {
            log("connected Luminosity Agent");
            this.eventBus.consumer(ADDRESS, msg -> {
                client.publish(TOPIC,
                        Buffer.buffer(msg.body().toString()),
                        MqttQoS.EXACTLY_ONCE,
                        false,
                        false);
            });
        });
    }

    private void log(String msg) {
        System.out.println("LuminositySensorAgent " + msg);
    }
}
