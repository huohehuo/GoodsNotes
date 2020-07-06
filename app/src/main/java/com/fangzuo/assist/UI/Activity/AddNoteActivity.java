package com.fangzuo.assist.UI.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.BuyAtRyAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.AddrBean;
import com.fangzuo.assist.Dao.BuyAtBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.LocDataUtil;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.VibratorUtil;
import com.fangzuo.assist.widget.ViewNumber;
import com.fangzuo.greendao.gen.BuyAtBeanDao;
import com.fangzuo.greendao.gen.BuyBeanDao;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class AddNoteActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.tv_num)
    ViewNumber tvNum;
    @BindView(R.id.ry_list)
    EasyRecyclerView ryList;

    BuyAtRyAdapter adapter;
    private NoteBeanDao noteBeanDao;
    private NoteBean noteBean;
    private BuyBeanDao buyBeanDao;
    private AddrBean addrBean;
    private BuyBean buyBean;
    private BuyAtBeanDao buyAtBeanDao;
    private String buyBeanName;
    public static String baseLoc = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.View_Upload_Addr://
                addrBean = (AddrBean) event.postEvent;
                if (!"".equals(addrBean.FName)){
                    tvNum.setNote(addrBean.FName);
                }
                break;
            case EventBusInfoCode.View_Save://
                String num = (String) event.postEvent;
                saveNote(MathUtil.toDBigString(num));

                break;

        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initBar();
//        getPermisssion();
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
        initList();
    }
    private void initList(){
        adapter.clear();
        ryList.setRefreshing(true);
        adapter.addAll(buyAtBeanDao.queryBuilder().where(BuyAtBeanDao.Properties.FBuyName.eq(buyBeanName)).orderDesc(BuyAtBeanDao.Properties.Id).build().list());
        adapter.notifyDataSetChanged();
        ryList.setRefreshing(false);
        double res=0;
        for (int i = 0; i < adapter.getAllData().size(); i++) {
            res = MathUtil.sum(res+"",adapter.getAllData().get(i).FNum);
        }
        Lg.e("得到数量",res);
        tvResult.setText("汇总:"+MathUtil.toDBigString(res+""));
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

    }


//
//    @OnClick({R.id.iv_add})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_add:
//                saveNote();
//                break;
//        }
//    }

    private void saveNote(String num){
        if (MathUtil.toD(num)<=0){
            Toast.showText(mContext,"请输入数量");
            return;
        }
        //当本地不存在时
        List<NoteBean> list = noteBeanDao.queryBuilder().where(NoteBeanDao.Properties.NBuyName.eq(buyBeanName)).build().list();
        if (list.size()<=0){
            noteBean = new NoteBean();
            noteBean.FID = CommonUtil.getTimeLong(false);
            noteBean.NBuyName= buyBeanName;
            noteBean.Ntime = CommonUtil.getTimeLong(true);
            noteBean.NCreateTime = CommonUtil.getTimeLong(true);
            noteBeanDao.insert(noteBean);
            Toast.showText(mContext, "添加成功");
        }else{
            NoteBean bean = list.get(0);
            bean.Ntime = CommonUtil.getTimeLong(true);//更新最新时间
            noteBeanDao.update(bean);
        }
        BuyAtBean buyAtBean = new BuyAtBean();
        buyAtBean.FID = CommonUtil.getTimeLong(false);
        buyAtBean.FNum = num;
        buyAtBean.setBuyBean(buyBean);
//        buyAtBean.setAddrBean(spAddrUIDlg.getData());
        buyAtBean.FAddrName = tvNum.getNote();//历史只和文本有联系，
        buyAtBean.FCreateData = CommonUtil.getTimeLong(true);
        buyAtBeanDao.insert(buyAtBean);
        //添加历史
        LocDataUtil.addAddrBean(tvNum.getNote());
        VibratorUtil.Vibrate(mContext, 200);
        tvNum.clearNum();
        initList();
    }
    @Override
    protected void OnReceive(String code) {

    }

    public static void start(Context context,String buybean) {
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

    //权限获取-------------------------------------------------------------
    private void getPermisssion() {
        String[] perm = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(mContext, perm)) {
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perm);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("permisssion", "获取成功的权限" + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i("permisssion", "获取失败的权限" + perms);
    }

}
