package com.fangzuo.assist.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.widget.BrowserLayout;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fangzuo.assist.UI.Activity.BackUpActivity.Control_WebView_Time;

public class WebAppActivity extends BaseActivity {


    @BindView(R.id.wb_action)
    BrowserLayout wbAction;

//    String url = "http://192.168.31.55:8085/Assist/AppWebHomeIO?json=";
    String url;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_webapp);
        ButterKnife.bind(this);
        initBar();
        wbAction.setViewGone();
        url = BasicShareUtil.getInstance(mContext).getBaseURL()+"AppWebHomeIO?json="+ Hawk.get(Info.User_Code, "");
        Hawk.put(Control_WebView_Time, Calendar.getInstance().getTimeInMillis());
    }

    @Override
    protected void initData() {
        wbAction.loadUrl(url);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wbAction.canGoBack()) {
            wbAction.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, WebAppActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putExtra("buybean", buybean);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }
    @OnClick({R.id.iv_right, R.id.ll1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                break;
            case R.id.ll1:
                finish();
                break;
        }
    }
}
