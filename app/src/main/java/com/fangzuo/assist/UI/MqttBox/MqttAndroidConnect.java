package com.fangzuo.assist.UI.MqttBox;

import android.util.Log;
import android.widget.Toast;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Utils.Lg;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * Created by xiaomo
 * Date on  2019/4/14
 *
 * @Desc 使用AndroidClient的Mqtt模块, 用以替代使用MqttClient的模块
 */

public class MqttAndroidConnect extends BaseConnect {
    private MqttAndroidClient mqttAndroidClient;

    public MqttAndroidConnect() {
        TAG = "MqttAndroidConnect";
    }

    @Override
    protected void startConnect() {
        try {
            final long startTime = System.currentTimeMillis();
//            String randomId = BuildRandomNumber.createGUID();
//            String clientId = String.format(FORMAT_CLIENT_ID, MqttManager.APP_NAME, randomId);
//            showLog("clientId = " + clientId);
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
//            showLog(String.format(URL_FORMAT, MqttManager.ip, MqttManager.port));
//            String URL_FORMAT = "tcp://%s:%d";
            mqttAndroidClient = new MqttAndroidClient(MqttManager.mApp.getApplicationContext(),
                    App.MQTT_Broker_URL, App.MQTT_ClientId);
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Lg.e(TAG, "connectComplete = " + reconnect+serverURI);
                    if (reconnect) {
                        // Because Clean Session is true, we need to re-subscribe
                        Lg.e(TAG+"It is reconnect = ",reconnect);
                    } else {
                        Lg.e(TAG+"It is first connect...");
                    }
                    Lg.e("启动成功");
                    connectSuccessCallBack(reconnect);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Lg.e(TAG,"连接丢失connectionLost"+cause.getMessage().toString());
                    App.isOkMqtt = false;
                    disConnectCallBack();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    onDataReceiveCallBack(message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
//                    showLog("deliveryComplete");
                }
            });

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(false);
//            mqttConnectOptions.setUserName("admin");
//            mqttConnectOptions.setPassword("admin".toCharArray());
            //设置true,不然重复发送
            mqttConnectOptions.setCleanSession(true);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Lg.e("连接成功，花费时间: "+(System.currentTimeMillis() - startTime));
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    App.isOkMqtt = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Lg.e("connect failure",exception.getMessage().toString());
                    App.isOkMqtt = false;
                    connectFailCallBack(exception.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Lg.e(TAG, "连接失败Exception = " + e.toString());
//            Toast.makeText(MqttManager.mApp.getApplicationContext(), "错误了", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void subscribeTopic() {
        try {
            mqttAndroidClient.subscribe(App.TOPIC, 1);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "subscribe topic exception = " + e.toString());
        }
    }

    @Override
    public boolean isConnected() {
        if (mqttAndroidClient == null) {
            return false;
        }
        return mqttAndroidClient.isConnected();
    }

    @Override
    public void disConnect() {
        if (null != mqttAndroidClient) {
            try {
                mqttAndroidClient.disconnect();
                mqttAndroidClient.close();
                disConnectCallBack();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void publish(String topic, String msg) throws Exception {
        MqttMessage mqttMsg = new MqttMessage();
        mqttMsg.setPayload(msg.getBytes());
        mqttAndroidClient.publish(topic, mqttMsg);
    }

}
