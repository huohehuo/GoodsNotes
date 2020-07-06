package com.fangzuo.assist.UI.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.AppLinkBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.WebResponse;
import com.fangzuo.assist.Dao.AddrBean;
import com.fangzuo.assist.Dao.BuyAtBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.LinSubscribe;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.AddrBeanDao;
import com.fangzuo.greendao.gen.BuyAtBeanDao;
import com.fangzuo.greendao.gen.BuyBeanDao;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackUpActivity extends BaseActivity {

    @BindView(R.id.tv_pg_num)
    TextView tvPgNum;
    //    @BindView(R.id.tv_sign)
//    TextView tvSign;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_backup)
    ImageView ivBackup;
    @BindView(R.id.iv_down)
    ImageView ivDown;
    @BindView(R.id.iv_own)
    ImageView ivOwn;
    @BindView(R.id.iv_loging)
    ImageView ivLoging;
    @BindView(R.id.pg_time)
    ProgressBar pgTime;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_pwd)
    EditText edPwd;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.tv_user)
    AppCompatTextView tvUser;
    @BindView(R.id.ll_logined)
    LinearLayout llLogined;
    @BindView(R.id.tv_loc_num)
    TextView tvLocNum;
    @BindView(R.id.tv_web_num)
    TextView tvWebNum;

    private long allNum = 0;
    private NoteBeanDao noteBeanDao;
    private BuyBeanDao buyBeanDao;
    private BuyAtBeanDao buyAtBeanDao;
    private AddrBeanDao addrBeanDao;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_back_up);
        ButterKnife.bind(this);
        initBar();
        noteBeanDao = daoSession.getNoteBeanDao();
        buyAtBeanDao = daoSession.getBuyAtBeanDao();
        buyBeanDao = daoSession.getBuyBeanDao();
        addrBeanDao = daoSession.getAddrBeanDao();
        long lasttime = Hawk.get(Back_Up_Time_Last, 0l);
//        long lasttime = Calendar.getInstance().getTimeInMillis();
        if (lasttime <= 0) {
            tvTime.setText("未备份");
        } else {
            Date date = new Date(lasttime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = format.format(date);
            tvTime.setText("上次备份:" + str);
        }
        loadView();
    }

    //处理页面
    private void loadView() {
        if ("".equals(Hawk.get(Info.User_Code, ""))) {
            llLogin.setVisibility(View.VISIBLE);
            llLogined.setVisibility(View.GONE);
        } else {
            llLogined.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            tvUser.setText(Hawk.get(Info.User_Name, ""));
        }
    }

    private Runnable runnable;
    private Handler handlerForNotice;

    @Override
    protected void initData() {
        allNum = buyBeanDao.count() + addrBeanDao.count() + buyAtBeanDao.count() + noteBeanDao.count();
        tvLocNum.setText("本地数据:" + MathUtil.toInt(allNum + ""));
        getCloudSize();
        //循环
        handlerForNotice = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handlerForNotice.postDelayed(runnable, 1000);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Upload_ProgressBar, ""));
            }
        };
        //启动循环
        handlerForNotice.postDelayed(runnable, 0);
    }

    private void getCloudSize(){
        WebResponse webResponse = new WebResponse();
        webResponse.json = Hawk.get(Info.User_Code, "");
        App.getRService().doIOActionPost("AppGetAllDataSizeIO", webResponse, new LinSubscribe<WebResponse>() {
            @Override
            public void onNext(WebResponse webResponse) {
//                super.onNext(webResponse);
                if (webResponse.state){
                    tvWebNum.setText("云备份:"+webResponse.size);
                }else{
                    tvWebNum.setText("云备份获取错误");
                }
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                tvWebNum.setText("云备份获取错误");
            }
        });
    }

    //上传数据
    private void pushData() {
        LoadingUtil.showDialog(mContext, "正在上传....");
        WebResponse webResponse = new WebResponse();
        ArrayList<BuyBean> buyBeans = new ArrayList<>();
        ArrayList<BuyAtBean> buyAtBeans = new ArrayList<>();
        ArrayList<AddrBean> addrBeans = new ArrayList<>();
        ArrayList<NoteBean> noteBeans = new ArrayList<>();
        buyBeans.addAll(daoSession.getBuyBeanDao().loadAll());
        buyAtBeans.addAll(daoSession.getBuyAtBeanDao().loadAll());
        addrBeans.addAll(daoSession.getAddrBeanDao().loadAll());
        noteBeans.addAll(daoSession.getNoteBeanDao().loadAll());
        webResponse.buyBeans = buyBeans;
        webResponse.buyAtBeans = buyAtBeans;
        webResponse.addrBeans = addrBeans;
        webResponse.noteBeans = noteBeans;
        webResponse.json = Hawk.get(Info.User_Code, "");//用于确定数据文件名
        webResponse.size = buyBeans.size() + buyAtBeans.size() + addrBeans.size();
        Lg.e("上传", webResponse);
        App.getRService().doIOActionPost("BackUpBuyBean", webResponse, new LinSubscribe<WebResponse>() {
            @Override
            public void onNext(WebResponse commonResponse) {
                LoadingUtil.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
                LoadingUtil.showAlter(mContext, "数据备份失败,服务器无响应");
            }
        });


//        App.getRService().doIOActionPost("BackUpAppDataIO", webResponse, new LinSubscribe<WebResponse>() {
//            @Override
//            public void onNext(WebResponse commonResponse) {
////                super.onNext(commonResponse);
//                if (commonResponse.state) {
//                    Hawk.put(Back_Up_Time_Last, Calendar.getInstance().getTimeInMillis());//保存本地点击的时间戳
//                    Date date = new Date(Calendar.getInstance().getTimeInMillis());
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String str = format.format(date);
//                    tvTime.setText("上次备份:" + str);
//                    handlerForNotice.postDelayed(runnable, 0);
//                    LoadingUtil.showAlter(mContext, "数据备份成功", "共有数据:" + commonResponse.size);
//                    getCloudSize();
//                } else {
//                    LoadingUtil.showAlter(mContext, commonResponse.backString);
//                }
//                LoadingUtil.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                LoadingUtil.dismiss();
//                LoadingUtil.showAlter(mContext, "数据备份失败,服务器无响应");
//            }
//        });
    }

    //获取所有网络数据
    private void pullData(){
        WebResponse webResponse = new WebResponse();
        webResponse.json = Hawk.get(Info.User_Code, "");
        App.getRService().doIOActionPost("AppGetAllDataIO", webResponse, new LinSubscribe<WebResponse>() {
            @Override
            public void onNext(WebResponse commonResponse) {
                if (commonResponse.state) {
                    Lg.e("得到数据",commonResponse);
                    dealPullData(commonResponse);
                } else {
                    LoadingUtil.showAlter(mContext, commonResponse.backString);
                }
                LoadingUtil.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
                LoadingUtil.showAlter(mContext, "数据备份失败，服务器无响应");
            }
        });
    }

    //处理pull下来的数据
    private void dealPullData(WebResponse webResponse){
        for (int i = 0; i < webResponse.noteBeans.size(); i++) {//当本地没有home页面数据时
            if (noteBeanDao.queryBuilder().where(NoteBeanDao.Properties.NBuyName.eq(webResponse.noteBeans.get(i).NBuyName)).count()<=0){
                noteBeanDao.insertInTx(webResponse.noteBeans.get(i));
            }
        }
        for (int i = 0; i < webResponse.addrBeans.size(); i++) {//当本地没有home页面数据时
            if (addrBeanDao.queryBuilder().where(AddrBeanDao.Properties.FName.eq(webResponse.addrBeans.get(i).FName)).count()<=0){
                addrBeanDao.insertInTx(webResponse.addrBeans.get(i));
            }
        }
        for (int i = 0; i < webResponse.buyBeans.size(); i++) {//当本地没有home页面数据时
            if (buyBeanDao.queryBuilder().where(BuyBeanDao.Properties.FName.eq(webResponse.buyBeans.get(i).FName)).count()<=0){
                buyBeanDao.insertInTx(webResponse.buyBeans.get(i));

            }
        }
        for (int i = 0; i < webResponse.buyAtBeans.size(); i++) {//当本地没有home页面数据时
            if (buyAtBeanDao.queryBuilder().where(BuyAtBeanDao.Properties.FID.eq(webResponse.buyAtBeans.get(i).FID)).count()<=0){
                buyAtBeanDao.insertInTx(webResponse.buyAtBeans.get(i));
            }
        }

        LoadingUtil.showAlter(mContext,"处理完毕");
        Lg.e("处理完毕");
    }


    @Override
    protected void initListener() {
        ivBackup.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (Calendar.getInstance().getTimeInMillis() - Hawk.get(Back_Up_Time_Last, 1000l) > Time_Control_Push) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("是否进行备份数据")
                            .setMessage("网络端将会被覆盖为新备份数据,如需要请先下载合并到本地;\n备份成功后，将会有时常为一分钟的冷却时间，期间不允许再次备份")
                            .setPositiveButton("确认备份", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pushData();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create().show();
                }else{
                    LoadingUtil.showAlter(mContext,"云备份受限");
                }


            }
        });
        ivDown.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (Calendar.getInstance().getTimeInMillis() - Hawk.get(Back_Up_Time_Last, 1000l) > Time_Control_Push) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("将会下载网络数据到本地")
                            .setMessage("合并注意事项：标签、备注历史若已存在，将不会新增，标签内明细数据将会以标签进行合并")
                            .setPositiveButton("确认同步合并", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pullData();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create().show();
                }else{
                    LoadingUtil.showAlter(mContext,"云同步受限");
                }


            }
        });


        edPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ivLoging.setBackground(getResources().getDrawable(R.mipmap.home_no));
                }else{
                    ivLoging.setBackground(getResources().getDrawable(R.mipmap.home));
                }
            }
        });

        ivOwn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("是否退出登录")
                        .setPositiveButton("退出登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Calendar.getInstance().getTimeInMillis() - Hawk.get(Login_Max_Time, 1000l) > Time_Control_Login) {
                                    Hawk.put(Info.User_Name, "");
                                    Hawk.put(Info.User_Pwd, "");
                                    Hawk.put(Info.User_Code, "");
                                    Hawk.put(Info.User_Token, "");
                                    loadView();
                                }else{
                                    LoadingUtil.showAlter(mContext,"登出受限");
                                }

                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();
                return true;
            }
        });

    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.View_Upload_ProgressBar://
                double res = MathUtil.div(MathUtil.sub(Calendar.getInstance().getTimeInMillis() + "", Hawk.get(Back_Up_Time_Last, 0l) + "") + "", Time_Control_Push+"");
                double lastRes = MathUtil.mul(res + "", "100");
                Lg.e("进度条", lastRes);
                if (lastRes >= 100) {
                    handlerForNotice.removeCallbacks(runnable);
                    pgTime.setProgress(100);
                    tvPgNum.setText("可重新备份");
                } else {
                    pgTime.setProgress(MathUtil.toInt(lastRes + ""));
                    tvPgNum.setText(MathUtil.toInt(lastRes + "") + "%");
                }
                break;
//            case EventBusInfoCode.View_Save://
//                String num = (String) event.postEvent;
//                saveNote(MathUtil.toDBigString(num));
//
//                break;

        }
    }
    //注册或登录账号
    private void login() {
        if (TextUtils.isEmpty(edName.getText().toString()) || TextUtils.isEmpty(edPwd.getText().toString())) {
            Toast.showText(mContext, "用户名和密码不能为空");
            return;
        }
        LoadingUtil.showDialog(mContext,"正在进行注册登录...");
        WebResponse webResponse = new WebResponse();
        AppLinkBean bean = new AppLinkBean();
        bean.FName = edName.getText().toString();
        bean.FPwd = edPwd.getText().toString();
        webResponse.json = gson.toJson(bean);
        App.getRService().doIOActionPost("AppLoginIO", webResponse, new LinSubscribe<WebResponse>() {
            @Override
            public void onNext(WebResponse commonResponse) {
                super.onNext(commonResponse);
                LoadingUtil.dismiss();
                if (commonResponse.state) {
                    Lg.e("登录成功");
                    Hawk.put(Login_Max_Time, Calendar.getInstance().getTimeInMillis());
                    Hawk.put(Info.User_Name, edName.getText().toString());
                    Hawk.put(Info.User_Pwd, edPwd.getText().toString());
                    Hawk.put(Info.User_Code, commonResponse.json);
                    Hawk.put(Info.User_Token, commonResponse.FToken);
                    loadView();
                } else {
                    Lg.e("登录失败" + commonResponse.backString);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
                Lg.e("登录失败:" + e.getMessage());
            }
        });

    }


    @OnClick({R.id.ll1, R.id.btn_login, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                finish();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.iv_right:


                if (Calendar.getInstance().getTimeInMillis() - Hawk.get(Control_WebView_Time, 1000l) > Time_Control_WebView) {
                    WebAppActivity.start(mContext);
                }else{
                    LoadingUtil.showAlter(mContext,"访问受限");
                }
                break;
        }
    }




    @Override
    protected void onDestroy() {
        handlerForNotice.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void OnReceive(String code) {

    }

    public static final String Back_Up_Time = "Back_Up_Time";
    public static final String Back_Up_Time_Last = "Back_Up_Time_Last";
    public static final String Login_Max_Time = "Login_Max_Time";
    public static final String Control_WebView_Time = "Control_WebView_Time";
    public static final int Time_Control_Login = 30000;//单词退出登录的间隔
    public static final int Time_Control_Push = 30000;//单词push的间隔
    public static final int Time_Control_WebView = 30000;//单次访问网络数据的间隔

    //防止点击过快的替换类Button
    public abstract class NoDoubleClickListener implements View.OnClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 1500;//间隔多少秒43200  12小时
        private long lastClickTime = Hawk.get(Back_Up_Time_Last, 0l);//获取上次备份的时间戳

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
//            if (currentTime - lastClickTime > Hawk.get(Back_Up_Time, 60000)) {//默认未60秒
//            if (currentTime - lastClickTime > Time_Control_Push) {//默认未60秒
            if (currentTime - lastClickTime > 3000) {//默认未60秒
                lastClickTime = currentTime;
                Lg.e("时间", lastClickTime);
                Lg.e("点击OK");
                onNoDoubleClick(v);
            } else {
                Toast.showText(mContext, "别点那么快谢谢 (=. =");
                Lg.e("太快了");
            }
        }

        protected abstract void onNoDoubleClick(View view);
    }






    public static void start(Context context) {
        Intent intent = new Intent(context, BackUpActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putExtra("buybean", buybean);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }
}
