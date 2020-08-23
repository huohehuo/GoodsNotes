package com.fangzuo.assist.UI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.UI.MqttBox.MqttManager;
import com.fangzuo.assist.UI.MqttBox.OnMqttAndroidConnectListener;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MQTTActivity extends BaseActivity {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_state)
    TextView tvState;

    public static final String SERVICE_CLASSNAME = "com.fangzuo.assist.UI.MQTTService";

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Event_MQTT://
                String str = (String) event.postEvent;
                tvTip.setText(str);
                break;
        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_mqtt);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {




    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                //初始化自定义WiseMqtt模块的配置,并开启长连接
//                MqttManager.getInstance()
//                        .init(getApplication())                  //port
//                        .connect();
//                MqttManager.getInstance().regeisterServerMsg(new OnMqttAndroidConnectListener() {
//                    @Override
//                    public void onDataReceive(String message) {
//                        Lg.e("收到消息了,内容是 ", message);
//                        tvTip.setText(message);
//                    }
//                });
                if (!serviceIsRunning()) {
                    tvState.setText("Start Service");
                    startBlackIceService();
                }else{
                    Toast.showText(mContext,"已启动");
                }
                break;

            case R.id.btn2:
                if (serviceIsRunning()) {
                    tvState.setText("Stop Service");
                    stopBlackIceService();
                }else{
                    Toast.showText(mContext,"未启动");
                }
                break;
            case R.id.btn3:
//                MqttManager.getInstance().sendMsg(App.TOPIC, "你好吗?我发一条消息试试");
                App.getInstance().sendMsg(App.TOPIC, "你好吗?我发一条消息试试");
                break;
        }
    }
    private void startBlackIceService() {
        final Intent intent = new Intent(MQTTActivity.this, MQTTService.class);
        startService(intent);

    }

    private void stopBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        stopService(intent);
    }
    private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }










    public static void start(Context context) {
        Intent intent = new Intent(context, MQTTActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putExtra("buybean", buybean);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
}
