package com.fangzuo.assist.UI;

import com.fangzuo.assist.Activity.Crash.App;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class Thermometer {


    private MqttClient client;


    public Thermometer() {
        try {
            client = new MqttClient(App.MQTT_Broker_URL, MqttClient.generateClientId(), new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() {

        try {
            client.connect();

            publishTemperature();

            client.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void publishTemperature() throws MqttException {
        final MqttTopic temperatureTopic = client.getTopic(App.TOPIC);

        final int temperature = createRandomNumberBetween(-20, 4);

        final MqttMessage message = new MqttMessage(String.valueOf(temperature).getBytes());
        temperatureTopic.publish(message);

        System.out.println("Published data. Topic: " + temperatureTopic.getName() + "  Message: " + temperature);
    }

    public static int createRandomNumberBetween(int min, int max) {

        return new Random().nextInt(max - min + 1) + min;
    }

    public static void main(String... args) {
        final Thermometer thermometer = new Thermometer();
        thermometer.start();
    }
}
