package com.fangzuo.assist.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.widget.BrowserLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {


    @BindView(R.id.wb_action)
    BrowserLayout wbAction;

    String url="http://192.168.31.55:8085/Assist/App/AppAbout.jsp";
    @Override
    protected void initView() {
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
//        url=BasicShareUtil.getInstance(mContext).getBaseURL()+"App/AppAbout.jsp";
        url=BasicShareUtil.getInstance(mContext).getBaseURL()+"App4GoodsNotes/App4GoodsNotesFile.jsp";
        initBar();
        wbAction.setViewGone();

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
        Intent intent = new Intent(context, AboutActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putExtra("buybean", buybean);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }
}
