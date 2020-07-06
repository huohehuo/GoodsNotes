package com.fangzuo.assist.UI.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.StripAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.UI.Fragment.AddrBeanFragment;
import com.fangzuo.assist.UI.Fragment.BuyBeanFragment;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseDataActivity extends BaseActivity {

    @BindView(R.id.tabstrip)
    PagerSlidingTabStrip tabstrip;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindColor(R.color.cpb_blue)
    int cpb_blue;
    public int tag;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.BaseData_Tip://
                String res = (String) event.postEvent;
                tvTip.setText(res);
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_basedata);
        ButterKnife.bind(this);
        initBar();
    }

    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        fragments.add(new BuyBeanFragment());
        fragments.add(new AddrBeanFragment());
        titles.add("记录标签");
        titles.add("备注历史");
        StripAdapter stripAdapter = new StripAdapter(getSupportFragmentManager(), fragments, titles);
        Log.e("stripAdapter", stripAdapter + "");
        viewpager.setAdapter(stripAdapter);
        tabstrip.setShouldExpand(true);
        tabstrip.setViewPager(viewpager);
        tabstrip.setDividerColor(cpb_blue);
        tabstrip.setUnderlineHeight(3);
        tabstrip.setIndicatorHeight(6);
        tabstrip.setIndicatorColor(cpb_blue);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }
    public static void start(Context context) {
        Intent intent = new Intent(context, BaseDataActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }

}
