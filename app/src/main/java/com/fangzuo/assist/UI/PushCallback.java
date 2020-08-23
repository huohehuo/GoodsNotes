package com.fangzuo.assist.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;


public class PushCallback implements MqttCallbackExtended {

    private ContextWrapper context;
    String TAG = "PushCallback";

    public PushCallback(ContextWrapper context) {

        this.context = context;
    }

    @Override
    public void connectionLost(Throwable cause) {
        Lg.e("MQTT连接丢失");
        //We should reconnect here
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Lg.e("收到数据",message.getPayload());
        Lg.e("收到数据",message.getQos());
        Lg.e("收到数据",message.toString());
        Lg.e("收到数据",topic);
        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Event_MQTT, message.toString()));

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Lg.e("MQTT连接成功",token);
    }

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
    }
//
//    @Override
//    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
////        final NotificationManager notificationManager = (NotificationManager)
////                context.getSystemService(Context.NOTIFICATION_SERVICE);
////
////        final Notification notification = new Notification(R.drawable.snow,
////                "Black Ice Warning!", System.currentTimeMillis());
////
////        // Hide the notification after its selected
////        notification.flags |= Notification.FLAG_AUTO_CANCEL;
////
////        final Intent intent = new Intent(context, BlackIceActivity.class);
////        final PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
////        notification.setLatestEventInfo(context, "Black Ice Warning", "Outdoor temperature is " +
////                new String(message.getPayload()) + "°", activity);
////        notification.number += 1;
////        notificationManager.notify(0, notification);
//    }

}
