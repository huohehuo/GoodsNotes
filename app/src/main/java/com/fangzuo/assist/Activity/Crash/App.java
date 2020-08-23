package com.fangzuo.assist.Activity.Crash;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.RService;
import com.fangzuo.assist.UI.MQTTService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 //  ┏┓　　　┏┓
 //┏┛┻━━━┛┻┓
 //┃　　　　　　　┃
 //┃　　　━　　　┃
 //┃　┳┛　┗┳　┃
 //┃　　　　　　　┃
 //┃　　　┻　　　┃
 //┃　　　　　　　┃
 //┗━┓　　　┏━┛
 //    ┃　　　┃   神兽保佑
 //    ┃　　　┃   代码无BUG！
 //    ┃　　　┗━━━┓
 //    ┃　　　　　　　┣┓
 //    ┃　　　　　　　┏┛
 //    ┗┓┓┏━┳┓┏┛
 //      ┃┫┫　┃┫┫
 //      ┗┻┛　┗┻┛
 */


public class App extends MultiDexApplication {
    public static boolean isDebug=true;
    public static String JsonFile="";
    public static boolean hasChangeView=false;//判断布局是否更新

    private static Context mContext;
//    private String mCurDev = "";
//    private boolean isIsDebug =true;
//    static App instance = null;

    private static OkHttpClient           okHttpClient;
    private static okhttp3.logging.HttpLoggingInterceptor interceptor;
//    private static Gson gson;

    private static Retrofit retrofit;
    public static String NowUrl;
    public static boolean isChangeIp=false;
    private static RService mService;//本地retrofit方法

    public static int PDA_Choose;//{" 1 G02A设备","2 8000设备","3 5000设备"4 M60,"5手机端，6 h100};

    public static String TOPIC = "pdatest";
    public static final String MQTT_Broker_URL = "tcp://129.211.59.124:1883";
//    public static final String MQTT_Broker_URL = "tcp://192.168.31.55:1883";
    public static final String MQTT_ClientId = "xiaomi";
    public static final String MQTT_Service_CLASSNAME = "com.fangzuo.assist.UI.MQTTService";
//    public static final String MQTT_Service_CLASSNAME = "org.eclipse.paho.android.service.MqttService";
    private static MqttAndroidClient mqttClient;
    private static MqttCallbackExtended callbackExtended;
    private static MqttConnectOptions options;
    private Handler handler;
    private Runnable runnable;
    private static App app;

    public static volatile boolean isOkMqtt = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        mContext = this;
        app = this;
        handler = new Handler();

        Hawk.init(mContext).build();
        PDA_Choose=Hawk.get(Config.PDA,1);
        NowUrl = BasicShareUtil.getInstance(mContext).getBaseURL();
        //retrofit的基本初始化相关
//        gson = new Gson();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(5000, TimeUnit.SECONDS);
//        builder.readTimeout(20, TimeUnit.SECONDS);
//        builder.writeTimeout(20, TimeUnit.SECONDS);
//        builder.retryOnConnectionFailure(true);
        interceptor = new okhttp3.logging.HttpLoggingInterceptor();
        interceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();

        //这里的baseurl,注意要有实际格式的链接，不然会崩
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BasicShareUtil.getInstance(mContext).getBaseURL())
                .build();
        mService = new RService();

        //循环执行服务检测
        runnable = new Runnable() {
            @Override
            public void run() {
                dealService();
            }
        };
        handler.postDelayed(runnable,5000);

    }

    //当连接中断时，执行此循环进行重新连接
    public synchronized void startCheckMqtt(){
        Lg.e("五秒后再次检测");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,5000);
    }

    //处理服务状态
    private void dealService(){
        Lg.e("启动。。。。");
        if (isConnectIsNomarl()){//有网络时
            if (serviceIsRunning()){//若服务正在运行
                if (!isOkMqtt){//状态值false时，断开连接
                    Lg.e("服务在运行，但是已经断开连接...终止服务，并重新连接");
                    Intent intent = new Intent(mContext, MQTTService.class);
                    stopService(intent);
                    Intent intentNew = new Intent(mContext, MQTTService.class);
                    startService(intentNew);
//                handler.postDelayed(runnable,5000);
                }else{
                    Lg.e("服务已经连接....");
//                handler.postDelayed(runnable,10000);
                }
            }else{
                Lg.e("新开启服务");
                isOkMqtt = false;
                Intent intent = new Intent(mContext, MQTTService.class);
                startService(intent);
            }
        }else{//没网络，就终止服务，并继续循环
            Intent intent = new Intent(mContext, MQTTService.class);
            stopService(intent);
            startCheckMqtt();
        }


    }


    //用于Service中的MQTT对象获取，以便在App中调用发布信息
    public static MqttAndroidClient getMqttClient(Context context) {
        mqttClient = new MqttAndroidClient(context,
                App.MQTT_Broker_URL, App.MQTT_ClientId);
        return mqttClient;
    }

    private void setRunnable(){
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final long startTime = System.currentTimeMillis();
                    mqttClient = new MqttAndroidClient(mContext,
                            App.MQTT_Broker_URL, App.MQTT_ClientId);
                    callbackExtended = new MqttCallbackExtended() {
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
                            App.isOkMqtt = false;
                            Lg.e("连接丢失connectionLost");
                            disConnect();
//                            Lg.e("连接丢失connectionLost"+cause.getMessage().toString());
//                            disConnectCallBack();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
//                            onDataReceiveCallBack(message.toString());
                            Lg.e("收到数据"+topic,message.toString());
                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Event_MQTT, message.toString()));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
//                    showLog("deliveryComplete");
                        }
                    };
                    options = new MqttConnectOptions();
                    // 清除缓存
                    options.setCleanSession(true);
                    // 设置超时时间，单位：秒
//                        options.setConnectionTimeout(15);
                    // 心跳包发送间隔，单位：秒
                    options.setKeepAliveInterval(15);
                    // 用户名
                    options.setUserName("admin");
                    // 密码
                    options.setPassword("admin".toCharArray());

//                    mqttClient.setCallback(new PushCallback(MQTTService.this));
                    mqttClient.setCallback(callbackExtended);
//                    mqttClient.connect();
                    //进行连接  有多个重载方法  看需求选择
//                    mqttClient.connect(options);
                    mqttClient.connect(options, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Lg.e("连接成功，花费时间: "+(System.currentTimeMillis() - startTime));
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
                            Lg.e("connect failure",exception.getMessage().toString());
                            App.isOkMqtt = false;
//                            connectFailCallBack(exception.getMessage().toString());
                        }
                    });

                } catch (MqttException e) {
                    Lg.e("错误",e.getMessage());
//                    Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
    }

    private void startSubscribe(){
        try {
            mqttClient.subscribe(App.TOPIC, 1);
        } catch (MqttException e) {
            e.printStackTrace();
            Lg.e( "订阅失败","subscribe topic exception = " + e.toString());
        }
    }
    //发布消息到所有订阅
    public void sendMsg(String topic, String message) {
        if (!isConnected()) {
            Toast.makeText(mContext, "还未建立连接", Toast.LENGTH_SHORT).show();
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
    //检测MQTTService是否在运行
    public boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (App.MQTT_Service_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    //检测Mqtt是否已经连接
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
    //断开Mqtt连接
    public void disConnect(){
        if (mqttClient != null && mqttClient.isConnected()) {
            App.isOkMqtt = false;
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
                Lg.e("断开失败");
            }
        }else{
            mqttClient.setCallback(null);
            Lg.e("断开状态",mqttClient == null);
            mqttClient = null;
            callbackExtended = null;
            options = null;
            handler.removeCallbacks(runnable);
        }
    }

    /** 判断网络是否连接 */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Lg.e("MQTT当前网络名称：" + name);
            return true;
        } else {
            Lg.e("MQTT 没有可用网络" );
            return false;
        }
    }


    public static Context getContext(){
        return mContext;
    }


    //获取Service对象，当ip发生变化时，更换Serivce对象
    public static RService getRService() {
        if (TextUtils.equals(BasicShareUtil.getInstance(App.getContext()).getBaseURL(),App.NowUrl)){
            isChangeIp=false;
            return mService;
        }else{
            isChangeIp=true;
            RService mSerivce = new RService();
            setRService(mSerivce);
            String changeUrl=BasicShareUtil.getInstance(App.getContext()).getBaseURL();
            App.NowUrl=changeUrl;
            return mSerivce;
        }
    }

    public static App getInstance() {
        return app;
    }
    public static void setRService(RService mService) {
        App.mService = mService;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        App.retrofit = retrofit;
    }
    public static OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}
