//package com.fangzuo.assist.Service;
//
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//
//import com.fangzuo.assist.Activity.Crash.App;
//import com.fangzuo.assist.Beans.CommonResponse;
//import com.fangzuo.assist.RxSerivce.MySubscribe;
//import com.fangzuo.assist.UI.PushCallback;
//import com.fangzuo.assist.Utils.Asynchttp;
//import com.fangzuo.assist.Utils.Config;
//import com.fangzuo.assist.Utils.GreenDaoManager;
//import com.fangzuo.assist.Utils.Toast;
//import com.fangzuo.assist.Utils.WebApi;
//import com.fangzuo.greendao.gen.DaoSession;
//import com.loopj.android.http.AsyncHttpClient;
//
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;
//
///**
// * An {@link IntentService} subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// * <p>
// * TODO: Customize class - update intent actions, extra parameters and static
// * helper methods.
// */
//public class MQTTService extends IntentService {
//    // TODO: Rename actions, choose action names that describe tasks that this
//    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
//    private static final String ACTION_FOO = "com.fangzuo.assist.Service.action.FOO";
//    private static final String ACTION_BAZ = "com.fangzuo.assist.Service.action.BAZ";
//
//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.fangzuo.assist.Service.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.fangzuo.assist.Service.extra.PARAM2";
//
//    public static final String BROKER_URL = "tcp://192.168.31.55:8161";
//
//    /* In a real application, you should get an Unique Client ID of the device and use this, see
//    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
//    public static final String clientId = "android-client";
//
//    public static final String TOPIC = "de/eclipsemagazin/blackice/warnings";
//    private MqttClient mqttClient;
//
//    public MQTTService() {
//        super("DataService");
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//    }
//
//    public static void startMQTT(Context context) {
//        Intent intent = new Intent(context, MQTTService.class);
//        intent.setAction(ACTION_FOO);
//        context.startService(intent);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                handleActionBaz();
//            }
//        }
//    }
//
//    private void handleActionBaz() {
//        try {
//            mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
//
//            mqttClient.setCallback(new PushCallback(this));
//            mqttClient.connect();
//
//            //Subscribe to all subtopics of homeautomation
//            mqttClient.subscribe(TOPIC);
//
//
//        } catch (MqttException e) {
//            android.widget.Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
//    }
//
//}
