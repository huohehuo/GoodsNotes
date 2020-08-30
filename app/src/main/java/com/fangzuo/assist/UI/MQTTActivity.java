package com.fangzuo.assist.UI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
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
import com.fangzuo.assist.Utils.NfcUtils;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MQTTActivity extends BaseActivity {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_state)
    TextView tvState;
    private boolean hasNFC;
    private Intent intentNfc;
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
    NfcUtils nfcUtils;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_mqtt);
        ButterKnife.bind(this);
        nfcUtils = new NfcUtils(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启前台调度系统
        PackageManager packageManager = this.getPackageManager();
        hasNFC = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
        Toast.showText(mContext,"设备是否支持NFC:"+hasNFC);
        if (hasNFC)NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasNFC)NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 当前app正在前端界面运行，这个时候有intent发送过来，那么系统就会调用onNewIntent回调方法，将intent传送过来
        // 我们只需要在这里检验这个intent是否是NFC相关的intent，如果是，就调用处理方法
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
//            processIntent(intent);
//        }

        //当该Activity接收到NFC标签时，运行该方法
        //调用工具方法，读取NFC数据
        intentNfc = intent;
//        try {
            read(intent);
//            String str = NfcUtils.readNFCFromTag(intentNfc);
//            LoadingUtil.showAlter(mContext,"读取NFC",str);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }
    protected short getSectorAddress(short blockAddress) {
        return (short) (blockAddress / BlockCountPerSector);
    }
    protected void CombineByteArray(byte[] data1, byte[] data2, int startIndex) {
        for (int i = 0; i < data2.length; i++) {
            data1[startIndex + i] = data2[i];
        }
    }
    private final short BlockCountPerSector = 4;
    private final short ByteCountPerCluster = 16
            * BlockCountPerSector;
    private final short ByteCountPerBlock = 16;
    private void read2(Intent intent){
        //tag 就是在上一篇中onNewIntent中获取的tag
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mc = MifareClassic.get(tag);
        short startAddress = 0;
        short endAddress = 5;

        byte[] data = new byte[(endAddress - startAddress + 1 ) * ByteCountPerBlock];

        try {
            int time = 0;
            mc.connect();for (short i = startAddress; i <= endAddress; i++ ,time++) {
                boolean auth = false;
                short sectorAddress = getSectorAddress(i);
                auth = mc.authenticateSectorWithKeyA(sectorAddress, MifareClassic.KEY_DEFAULT);
                if (auth){

                    //the last block of the sector is used for KeyA and KeyB cannot be overwritted
                    short readAddress = (short)(sectorAddress == 0 ? i : i + sectorAddress);

                    byte[] response = mc.readBlock(readAddress);
                    CombineByteArray(data, response, time * ByteCountPerBlock);
                }
                else{
//                    throw new NfcException(NfcErrorCode.TemporaryError,
//                            "Authorization Error.");
                }
            }

            mc.close();

        }
        catch (Exception ne) {
            Lg.e("报错",ne.getMessage());
        } finally
        {
            try {
                mc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void read (Intent intent){
            //取出封装在intent中的TAG
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String CardId =ByteArrayToHexString(tagFromIntent.getId());
            String metaInfo = "";
            metaInfo+="卡片ID:"+CardId;
            for (String tech : tagFromIntent.getTechList()) {
                System.out.println(tech);
            }
            boolean auth = false;
            //读取TAG
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                //Enable I/O operations to the tag from this TagTechnology object.
                mfc.connect();
                int type = mfc.getType();//获取TAG的类型
                int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
                String typeS = "";
                switch (type) {
                    case MifareClassic.TYPE_CLASSIC:
                        typeS = "TYPE_CLASSIC";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        typeS = "TYPE_PLUS";
                        break;
                    case MifareClassic.TYPE_PRO:
                        typeS = "TYPE_PRO";
                        break;
                    case MifareClassic.TYPE_UNKNOWN:
                        typeS = "TYPE_UNKNOWN";
                        break;
                }
                metaInfo += "\n卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                        + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
                for (int j = 0; j < sectorCount; j++) {
                    //Authenticate a sector with key A.
                    auth = mfc.authenticateSectorWithKeyA(j,
                            MifareClassic.KEY_DEFAULT);
                    int bCount;
                    int bIndex;
                    if (auth) {
                        metaInfo += "Sector " + j + ":验证成功\n";
                        // 读取扇区中的块
                        bCount = mfc.getBlockCountInSector(j);
                        bIndex = mfc.sectorToBlock(j);
                        for (int i = 0; i < bCount; i++) {
                            byte[] data = mfc.readBlock(bIndex);
                            metaInfo += "Block " + bIndex + " : "
                                    + bytesToHexString(data) + "\n";
                            bIndex++;
                        }
                    } else {
                        metaInfo += "Sector " + j + ":验证失败\n";
                    }
                }
                Lg.e("结果",metaInfo);
                LoadingUtil.showAlter(mContext,"读取NFC",metaInfo);
//                promt.setText(metaInfo);
                //Toast.makeText(this, metaInfo, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Lg.e("读取失败",e.getMessage());
                e.printStackTrace();
            }


    }
    //向标签指定block写入数据
    private void writeData(final MifareClassic mfc, final int blockIndex, String msg) {
        if (mfc != null) {
            try {
                Lg.e(TAG, "writeData: isConnected:" + mfc.isConnected());
                if (mfc.isConnected()) {
                    byte[] temp = msg.getBytes(Charset.forName("UTF-8"));
                    final byte[] write = new byte[MifareClassic.BLOCK_SIZE];//每一块最大存储字节数
                    for (int i = 0; i < MifareClassic.BLOCK_SIZE; i++) {
                        if (i < temp.length)
                            write[i] = temp[i];
                        else
                            write[i] = 0;
                    }
//                    tvStatus.setText("正在写入");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Lg.e(TAG, "run: 写入内容：" + new String(write));
                                mfc.writeBlock(blockIndex, write);//写入方法是一个阻塞函数，不能在UI线程调用
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        tvStatus.setText("写入完成");
                                    }
                                });
                            } catch (final Exception ex) {
                                Lg.e(TAG, "writeData_Ex: " + ex.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        tvStatus.setText("写入失败：" + ex.getMessage());
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
//                    Toast.makeText(this, "NFC连接断开", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Lg.e(TAG, "writeData: " + ex.getMessage());
            }
        } else {
//            Toast.makeText(this, "未扫描到NFC卡片", Toast.LENGTH_SHORT).show();
        }
    }

    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        String readResult=new String(stringBuilder.toString().getBytes(), Charset.forName("UTF-8"));
        try {
////            readResult = new String(stringBuilder.toString().getBytes(), "UTF-8");
            readResult = new String(src, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return readResult;
    }
    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
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
                try {
                    NfcUtils.writeNFCToTag("数据写入:方左科技",intentNfc);
                } catch (IOException e) {
                    Lg.e("写入错误",e.getMessage());
                    e.printStackTrace();
                } catch (FormatException e) {
                    Lg.e("写入错误2",e.getMessage());
                    e.printStackTrace();
                }
//                MqttManager.getInstance().sendMsg(App.TOPIC, "你好吗?我发一条消息试试");
//                App.getInstance().sendMsg(App.TOPIC, "你好吗?我发一条消息试试");
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
