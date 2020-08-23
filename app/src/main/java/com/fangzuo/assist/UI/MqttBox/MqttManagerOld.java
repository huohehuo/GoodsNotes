//package com.fangzuo.assist.UI.MqttBox;
//
//import android.app.Application;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.fangzuo.assist.Activity.Crash.App;
//import com.fangzuo.assist.Utils.Lg;
//
//
///**
// * Created by xiaomo
// * Date on  2019/4/14 16:25.
// *
// * @Desc mqttdemo 长连接接收推送 模块 的启动,功能操作类
// */
//
//public class MqttManagerOld implements Imanager {
//    private static final String TAG = "MqttManager";
//    public static String APP_NAME = "pdatest";
//    public static String ip;
//    public static int port;
//    public static Handler mHandler;
//    public static Application mApp; //当前应用的Application
//
//    private MqttAndroidConnect mMqttAndroidConnect;
//    private static MqttManagerOld mInstance;
//
//    public static MqttManagerOld getInstance() {
//        synchronized (MqttManagerOld.class) {
//            if (mInstance == null) {
//                mInstance = new MqttManagerOld();
//            }
//        }
//        return mInstance;
//    }
//
//    private MqttManagerOld() {
//        mMqttAndroidConnect = new MqttAndroidConnect();
//    }
//
//    /**
//     * @param application 当前Application
//     */
//    public MqttManagerOld init(Application application) {
//        mApp = application;
//        mHandler = new Handler(application.getMainLooper());
//        return mInstance;
//    }
//
//    /**
//     * @param serverIp 服务端的ip
//     * @return
//     */
//    public MqttManagerOld setServerIp(String serverIp) {
//        ip = serverIp;
//        return mInstance;
//    }
//
//    /**
//     * @param serverPort 服务端的port
//     * @return
//     */
//    public MqttManagerOld setServerPort(int serverPort) {
//        port = serverPort;
//        return mInstance;
//    }
//
//    @Override
//    public void connect() {
//        if (mMqttAndroidConnect.isAlive()) {
//            App.isOkMqtt = true;
//            Log.e(TAG, "MqttManager connect thread has alive");
//            return;
//        }
//        if (mMqttAndroidConnect.isConnected()) {
//            App.isOkMqtt = true;
//            Log.e(TAG, "MqttManager has connected");
//            return;
//        }
//        try {
//            mMqttAndroidConnect.start();
//        }catch (Exception e){
//            Lg.e("启动错误");//关闭所有
//            mMqttAndroidConnect.disConnect();
//        }
//    }
//
//    @Override
//    public void disConnect() {
//        if (mMqttAndroidConnect == null) {
//            Log.e(TAG, "Wisepush should connect first");
//            return;
//        }
//        mMqttAndroidConnect.disConnect();
//    }
//
//    /**
//     * 需要订阅的模块的String
//     */
//    @Override
//    public void regeisterServerMsg(OnMqttAndroidConnectListener listener) {
//        mMqttAndroidConnect.regeisterServerMsg(listener);
//    }
//
//    @Override
//    public void unRegeisterServerMsg(OnMqttAndroidConnectListener listener) {
//        mMqttAndroidConnect.unRegeisterServerMsg(listener);
//    }
//
//    /**
//     * @param topic 需要发送的模块的toppic
//     */
//    @Override
//    public void sendMsg(String topic, String message) {
//        if (!isConnected()) {
//            Toast.makeText(MqttManagerOld.mApp.getApplicationContext(), "还未建立连接", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mMqttAndroidConnect.sendMsg(topic, message);
//    }
//
//    /**
//     * 判断是否正在连接
//     *
//     * @return
//     */
//    @Override
//    public boolean isConnected() {
//        if (mMqttAndroidConnect == null) {
//            App.isOkMqtt = false;
//            return false;
//        }
//        if (mMqttAndroidConnect.isConnected()) {
//            App.isOkMqtt = true;
//            return true;
//        }
//        return false;
//    }
//
//}
