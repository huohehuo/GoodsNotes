package com.fangzuo.assist.UI.MqttBox;

import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Utils.Lg;

import java.util.ArrayList;


/**
 * Created by xiaomo
 * Date on  2019/4/14
 *
 * @Desc 所有长连接的base
 */

public abstract class BaseConnect extends Thread {
    public String FORMAT_CLIENT_ID = "MQTT_%s_Android_%s";
    protected String TAG = "BaseConnect";
    private ArrayList<MqttListenerData> mDataLists = new ArrayList<>();   //界面注册的数据和listener

    private boolean isDebug = true;

    protected abstract void startConnect(); //开始连接

    protected abstract boolean isConnected();   //判断是否可连接

    protected abstract void disConnect();   //断开连接

    protected abstract void publish(String topic, String json) throws Exception;  //发送消息

    protected abstract void subscribeTopic();   //订阅主题

    public BaseConnect() {

    }

    @Override
    public void run() {
        super.run();
        startConnect();
    }



    //发送无须在本次回应中回调的消息
    public void sendMsg(String topic, String body) {
        try {
            publish(topic, body);
        } catch (Exception e) {
            Log.e(TAG, "send msg Exception = " + e.toString());
        }
    }


    public void regeisterServerMsg(OnMqttAndroidConnectListener listener) {
        MqttListenerData data = new MqttListenerData();
        data.setListener(listener);
        mDataLists.add(data);
        Lg.e("regeister event " + data.getEvent() + " + listener" + data.getListener());
    }

    public void unRegeisterServerMsg(OnMqttAndroidConnectListener listener) {
        for (int i = 0; i < mDataLists.size(); i++) {
            MqttListenerData data = mDataLists.get(i);
            if (data.getListener() == listener) {
                Lg.e("unregeister event " + data.getEvent() + " + listener" + data.getListener() + " + size = " + mDataLists.size());
                mDataLists.remove(i--);
            }
        }
    }

    /**
     * 对相应注册回调模块进行消息回调
     */
    protected void onDataReceiveCallBack(final String message) {
        for (int i = 0; i < mDataLists.size(); i++) {
            final MqttListenerData data = mDataLists.get(i);

            MqttManager.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    data.getListener().onDataReceive(message);
                }
            });
        }
    }

    /**
     * 断开连接的回调
     */
    protected void disConnectCallBack() {
        ArrayList<OnMqttAndroidConnectListener> totalLists = getAvailListener();
        for (int i = 0; i < totalLists.size(); i++) {
            final OnMqttAndroidConnectListener onMqttAndroidConnectListener = totalLists.get(i);
            MqttManager.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Lg.e("销毁callback");
                    onMqttAndroidConnectListener.disConnect();
                }
            });
        }
    }

    /**
     * 连接错误会对所有listener进行回调
     */
    protected void connectFailCallBack(final String exception) {
        Lg.e("disConnectCallBack = " + exception + " connected state = " + isConnected());
        ArrayList<OnMqttAndroidConnectListener> totalLists = getAvailListener();
        for (int i = 0; i < totalLists.size(); i++) {
            final OnMqttAndroidConnectListener onWebSocketConnectListener = totalLists.get(i);
            MqttManager.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onWebSocketConnectListener.onConnectFail(exception);
                }
            });
        }
    }

    /**
     * 连接成功会对所有listener进行回调
     */
    protected void connectSuccessCallBack(boolean reconnect) {
        if (!reconnect) {
            subscribeTopic();
        }
        ArrayList<OnMqttAndroidConnectListener> totalLists = getAvailListener();
        for (int i = 0; i < totalLists.size(); i++) {
            final OnMqttAndroidConnectListener onWebSocketConnectListener = totalLists.get(i);
            MqttManager.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    App.isOkMqtt = true;
                    onWebSocketConnectListener.connect();
                }
            });
        }
    }

    private ArrayList<OnMqttAndroidConnectListener> getAvailListener() {
        ArrayList<OnMqttAndroidConnectListener> list = new ArrayList<>();
        for (int i = 0; i < mDataLists.size(); i++) {   //去掉重复的回调
            MqttListenerData data = mDataLists.get(i);
            if (data.getListener() == null) {
                Lg.e(" data.getListener() == null");
                continue;
            }
            if (list.contains(data.getListener())) {
                continue;
            }
            list.add(data.getListener());
        }
        return list;
    }


}
