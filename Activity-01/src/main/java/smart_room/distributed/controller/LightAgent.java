package smart_room.distributed.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.mqtt.MqttClient;
import smart_room.distributed.LightDeviceSimulator;

public class LightAgent extends AbstractVerticle {
    private static final String LUM_TOPIC = "smartRoom/luminosity";
    private static final String PRES_TOPIC = "smartRoom/presenceDetection";
    private static final String SMART_ROOM_TOPICS = "smartRoom/+";

    public static final Double LUM_THRESHOLD = 0.5;
    private final LightDeviceSimulator light;
    private double currentLevel;
    private boolean presence;

    public LightAgent(final LightDeviceSimulator light) {
        this.light = light;
    }

    @Override
    public void start() {
        MqttClient client = MqttClient.create(this.getVertx());

        client.connect(1883, "broker.mqtt-dashboard.com", c -> {
            log("connected Light Agent");
            //è necessario avere 1 sola subscribe!
            client.publishHandler(s -> {
                switch (s.topicName()){
                    case LUM_TOPIC -> this.currentLevel = Double.parseDouble(s.payload().toString());
                    case PRES_TOPIC -> this.presence = Boolean.parseBoolean(s.payload().toString());
                }
                this.updateState();
            }).subscribe(SMART_ROOM_TOPICS, 2);
        });
    }

    private void updateState() {
        if (this.presence && this.currentLevel < LUM_THRESHOLD) {
            this.light.on();
        } else {
            this.light.off();
        }
    }

    private void log(String msg) {
        System.out.println("LightSensorAgent " + msg);
    }
}
