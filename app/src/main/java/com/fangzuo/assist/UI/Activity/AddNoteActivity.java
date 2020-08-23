package com.fangzuo.assist.UI.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.BuyAtRyAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.AddrBean;
import com.fangzuo.assist.Dao.BuyAtBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.VibratorUtil;
import com.fangzuo.greendao.gen.BuyAtBeanDao;
import com.fangzuo.greendao.gen.BuyBeanDao;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class AddNoteActivity extends BaseActivity{
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.ry_list)
    EasyRecyclerView ryList;

    BuyAtRyAdapter adapter;
    @BindView(R.id.tv_model)
    AppCompatTextView tvModel;
    @BindView(R.id.tv_stuff)
    AppCompatTextView tvStuff;
    @BindView(R.id.tv_color)
    AppCompatTextView tvColor;
    @BindView(R.id.tv_unit)
    AppCompatTextView tvUnit;
    @BindView(R.id.ed_price)
    EditText edPrice;
    private NoteBeanDao noteBeanDao;
    private NoteBean noteBean;
    private BuyBeanDao buyBeanDao;
    private AddrBean addrBean;
    private BuyBean buyBean;
    private BuyAtBeanDao buyAtBeanDao;
    private String buyBeanName;

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Event_Model://
                tvModel.setText((String) event.postEvent);
                Hawk.put(event.Msg,(String) event.postEvent);
                break;
            case EventBusInfoCode.Event_Stuff://
                tvStuff.setText((String) event.postEvent);
                Hawk.put(event.Msg,(String) event.postEvent);
                break;
            case EventBusInfoCode.Event_Color://
                tvColor.setText((String) event.postEvent);
                Hawk.put(event.Msg,(String) event.postEvent);
                break;
            case EventBusInfoCode.Event_Unit://
                tvUnit.setText((String) event.postEvent);
                Hawk.put(event.Msg,(String) event.postEvent);
                break;


        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initBar();
        noteBean = new NoteBean();
        noteBeanDao = daoSession.getNoteBeanDao();
        buyBeanDao = daoSession.getBuyBeanDao();
        buyAtBeanDao = daoSession.getBuyAtBeanDao();
        Intent in = getIntent();
        buyBeanName = in.getStringExtra("buybean");
        tvTitle.setText(buyBeanName);
    }

    @Override
    protected void initData() {
        buyBean = buyBeanDao.queryBuilder().where(BuyBeanDao.Properties.FName.eq(buyBeanName)).build().list().get(0);
        adapter = new BuyAtRyAdapter(mContext);
        ryList.setAdapter(adapter);
        ryList.setLayoutManager(new LinearLayoutManager(mContext));
        //设置上次的值
        tvModel.setText(Hawk.get(EventBusInfoCode.Event_Model,""));
        tvUnit.setText(Hawk.get(EventBusInfoCode.Event_Unit,""));
        tvColor.setText(Hawk.get(EventBusInfoCode.Event_Color,""));
        tvStuff.setText(Hawk.get(EventBusInfoCode.Event_Stuff,""));
        initList();
    }

    private void initList() {
        adapter.clear();
        ryList.setRefreshing(true);
        adapter.addAll(buyAtBeanDao.queryBuilder().where(BuyAtBeanDao.Properties.FBuyName.eq(buyBeanName)).orderDesc(BuyAtBeanDao.Properties.Id).build().list());
        adapter.notifyDataSetChanged();
        ryList.setRefreshing(false);
        tvNum.setText(adapter.getCount()+"");
//        double res = 0;
//        for (int i = 0; i < adapter.getAllData().size(); i++) {
//            res = MathUtil.sum(res + "", adapter.getAllData().get(i).FNum);
//        }
//        Lg.e("得到数量", res);
//        tvResult.setText("汇总:" + MathUtil.toDBigString(res + ""));

    }

    @Override
    protected void initListener() {
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                new AlertDialog.Builder(mContext)
                        .setTitle("是否删除该条数据")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buyAtBeanDao.deleteInTx(adapter.getAllData().get(position));
                                initList();
                            }
                        })
                        .create().show();
                return true;
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Lg.e("点击",adapter.getAllData().get(position));
            }
        });


        edNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    saveNote();
                }
                return true;
            }
        });

    }

    private void saveNote() {
        if (MathUtil.toD(edNum.getText().toString()) <= 0) {
            Toast.showText(mContext, "请输入数量");
            MediaPlayer.getInstance(mContext).error();
            return;
        }
        if (MathUtil.toD(edPrice.getText().toString()) <= 0) {
            Toast.showText(mContext, "请输入请输入单价");
            MediaPlayer.getInstance(mContext).error();
            return;
        }
        //同种规格不能出现重复
        if (buyAtBeanDao.queryBuilder().where(
                BuyAtBeanDao.Properties.FBuyName.eq(buyBeanName),
                BuyAtBeanDao.Properties.FModelName.eq(tvModel.getText().toString())
        ).count()>0) {
            Toast.showText(mContext, "同一个客户下，不能添加重复的规格，请重新检查");
            MediaPlayer.getInstance(mContext).error();
            return;
        }

        //当本地不存在时
        List<NoteBean> list = noteBeanDao.queryBuilder().where(NoteBeanDao.Properties.NBuyName.eq(buyBeanName)).build().list();
        if (list.size() <= 0) {
            noteBean = new NoteBean();
            noteBean.FID = CommonUtil.getTimeLong(false);
            noteBean.NBuyName = buyBeanName;
            noteBean.Ntime = CommonUtil.getTimeLong(true);
            noteBean.NCreateTime = CommonUtil.getTimeLong(true);
            noteBeanDao.insert(noteBean);
            Toast.showText(mContext, "添加成功");
        } else {
            NoteBean bean = list.get(0);
            bean.Ntime = CommonUtil.getTimeLong(true);//更新最新时间
            noteBeanDao.update(bean);
        }


        BuyAtBean buyAtBean = new BuyAtBean();
        buyAtBean.FID = CommonUtil.getTimeLong(false);
        buyAtBean.FNum = edNum.getText().toString();
        buyAtBean.FPrice = edPrice.getText().toString();
        buyAtBean.FSum = MathUtil.mul(buyAtBean.FNum,buyAtBean.FPrice)+"";
        buyAtBean.setBuyBean(buyBean);
        buyAtBean.FColorName = tvColor.getText().toString();
        buyAtBean.FModelName = tvModel.getText().toString();
        buyAtBean.FUnitName = tvUnit.getText().toString();
        buyAtBean.FStuffName = tvStuff.getText().toString();

//        buyAtBean.setAddrBean(spAddrUIDlg.getData());
//        buyAtBean.FAddrName = edNum.getNote();//历史只和文本有联系，
        buyAtBean.FCreateData = CommonUtil.getTimeLong(true);
        buyAtBeanDao.insert(buyAtBean);

        VibratorUtil.Vibrate(mContext, 200);
        MediaPlayer.getInstance(mContext).ok();
        edNum.setText("");
        initList();
    }

    @Override
    protected void OnReceive(String code) {

    }

    public static void start(Context context, String buybean) {
        Intent intent = new Intent(context, AddNoteActivity.class);
//        intent.putExtra("activity", activity);
        intent.putExtra("buybean", buybean);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @OnClick({R.id.iv_save, R.id.tv_model, R.id.tv_stuff, R.id.tv_color, R.id.tv_unit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_save:
                saveNote();
                break;
            case R.id.tv_model:
                SearchDataActivity.start(mContext,buyBeanName,EventBusInfoCode.Event_Model);
                break;
            case R.id.tv_stuff:
                SearchDataActivity.start(mContext,buyBeanName,EventBusInfoCode.Event_Stuff);
                break;
            case R.id.tv_color:
                SearchDataActivity.start(mContext,buyBeanName,EventBusInfoCode.Event_Color);
                break;
            case R.id.tv_unit:
                SearchDataActivity.start(mContext,buyBeanName,EventBusInfoCode.Event_Unit);
                break;
        }
    }
}
