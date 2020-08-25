package com.fangzuo.assist.UI;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
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
 * @author Dominik Obermaier
 */
public class MQTTServiceS extends Service {

//    public static final String BROKER_URL = "tcp://192.168.31.55:8161";

    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */

//    public static final String TOPIC = "pdatest";
    private MqttAndroidClient mqttClient;
    private Thread thread;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Lg.e("MQTTService服务开启");
    }

    @Override
    public void onStart(Intent intent, int startId) {
         thread =new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    final long startTime = System.currentTimeMillis();
                    mqttClient = App.getMqttClientS(MQTTServiceS.this);

//                    mqttClient = new MqttClient(App.MQTT_Broker_URL, App.MQTT_ClientId, new MemoryPersistence());
//                    mqttClient = new MqttAndroidClient(MQTTService.this,
//                            App.MQTT_Broker_URL, App.MQTT_ClientId);

                    MqttConnectOptions options = new MqttConnectOptions();
                    // 清除缓存
                    options.setCleanSession(true);
                    // 设置超时时间，单位：秒
                    options.setConnectionTimeout(15);
                    // 心跳包发送间隔，单位：秒
                    options.setKeepAliveInterval(15);
                    // 用户名
                    options.setUserName("admin");
                    // 密码
                    options.setPassword("admin".toCharArray());
// last will message
                    boolean doConnect = true;
                    String message = "{\"terminal_uid\":\"" + App.MQTT_ClientIdS + "\"} 当前id非正常断开连接前的最后一条信息";
                    Log.e(getClass().getName(), "message是:" + message);
                    Boolean retained = false;
                    if ((!message.equals(""))) {
                        // 最后的遗嘱
                        // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
                        //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
                        //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
                        try {
                            //设置后，当该id非正常退出时，其他订阅了该tip的客户端也会受到这条数据
                            options.setWill(App.TOPIC2, message.getBytes(), 2, retained.booleanValue());
                        } catch (Exception e) {
                            Lg.e( "Exception Occured", e.getMessage());
                            doConnect = false;
//                            iMqttActionListener.onFailure(null, e);
                        }
                    }

//                    mqttClient.setCallback(new PushCallback(MQTTService.this));
                    mqttClient.setCallback(new MqttCallbackExtended() {
                        @Override
                        public void connectComplete(boolean reconnect, String serverURI) {
                            Lg.e( "connectComplete = " + reconnect+serverURI);
                            if (reconnect) {
                                // Because Clean Session is true, we need to re-subscribe
                                Lg.e("It is reconnect = ",reconnect);
                            } else {
                                Lg.e("It is first connect...");
                            }
                            Lg.e("启动成功");
//                            connectSuccessCallBack(reconnect);
                        }

                        @Override
                        public void connectionLost(Throwable cause) {
                            Lg.e("S连接丢失connectionLost");
//                            Lg.e("连接丢失connectionLost"+cause.getMessage().toString());
                            App.isOkMqtt = false;
                            App.getInstance().startCheckMqtt();
//                            disConnectCallBack();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
//                            onDataReceiveCallBack(message.toString());
                            Lg.e("收到数据S"+topic+message.toString().length(),message.toString());
                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Event_MQTT, message.toString()));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
//                    showLog("deliveryComplete");
                        }
                    });
//                    mqttClient.connect();
                    //进行连接  有多个重载方法  看需求选择
//                    mqttClient.connect(options);
                    mqttClient.connect(options, MQTTServiceS.this, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Lg.e("S连接成功，花费时间: "+(System.currentTimeMillis() - startTime));
                            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                            disconnectedBufferOptions.setBufferEnabled(true);
                            disconnectedBufferOptions.setBufferSize(100);
                            disconnectedBufferOptions.setPersistBuffer(false);
                            disconnectedBufferOptions.setDeleteOldestMessages(false);
                            mqttClient.setBufferOpts(disconnectedBufferOptions);
                            App.isOkMqtt = true;
                            startSubscribe();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Lg.e("Sconnect failure",exception.getMessage().toString());
                            App.isOkMqtt = false;
//                            App.getInstance().startCheckMqtt();
//                            connectFailCallBack(exception.getMessage().toString());
                        }
                    });
//连接成功后订阅主题

                    //Subscribe to all subtopics of homeautomation
//                    mqttClient.subscribe(App.TOPIC);


                } catch (MqttException e) {
                    Lg.e("错误",e.getMessage());
                    App.getInstance().startCheckMqtt();
//                    Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        super.onStart(intent, startId);
    }

    private void startSubscribe(){
        try {
            mqttClient.subscribe(App.TOPIC2, 2);
        } catch (MqttException e) {
            e.printStackTrace();
            Lg.e( "订阅失败","subscribe topic exception = " + e.toString());
        }
    }

    public void sendMsg(String topic, String message) {
        if (!isConnected()) {
            Lg.e("还未建立连接");
//            Toast.makeText( "还未建立连接");
            return;
        }
        MqttMessage mqttMsg = new MqttMessage();
        mqttMsg.setPayload(message.getBytes());
        try {
            mqttClient.publish(topic, mqttMsg);
        } catch (MqttException e) {
            Lg.e("发送失败");
            e.printStackTrace();
        }
    }
    public boolean isConnected() {
        if (mqttClient == null) {
            App.isOkMqtt = false;
            return false;
        }
        if (mqttClient.isConnected()) {
            App.isOkMqtt = true;
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        Lg.e("MQTTService服务结束");
//        try {
//            mqttClient.disconnect(0);
//        } catch (MqttException e) {
//            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
//        thread.stop();
        if (null != mqttClient) {
            try {
                mqttClient.disconnect();
//                mqttClient.disconnectForcibly();
                mqttClient = null;
//                if (null!=mqttClient)mqttClient.close();
            } catch (MqttException e) {
                Lg.e("断开错误"+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
