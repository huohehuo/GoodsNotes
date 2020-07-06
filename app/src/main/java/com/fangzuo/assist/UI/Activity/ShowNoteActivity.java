package com.fangzuo.assist.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.MoodRyAdapter;
import com.fangzuo.assist.Beans.MoodBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerAddrUIDlg;
import com.fangzuo.assist.widget.SpinnerBuyUIDlg;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowNoteActivity extends BaseActivity {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_detail)
    EditText edDetail;
//    @BindView(R.id.sp_buy)
//    SpinnerBuyUIDlg spBuyUIDlg;
    @BindView(R.id.sp_addr)
    SpinnerAddrUIDlg spAddrUIDlg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private NoteBeanDao noteBeanDao;
    private NoteBean noteBean;
    private String notId;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_show_note);
        ButterKnife.bind(this);
        initBar();
        noteBeanDao = daoSession.getNoteBeanDao();
        Intent in = getIntent();
        Bundle b = in.getExtras();
        notId = b.getString("id", "");
//        isPushDown = b.getInt("isPushDown");
//        activity = b.getInt("activity");

    }

    @Override
    protected void initData() {
        noteBean = noteBeanDao.queryBuilder().where(NoteBeanDao.Properties.Id.eq(notId)).build().unique();
        if (null == noteBean) {
            LoadingUtil.showAlter(mContext, "本地无法找到该记录", "");
        } else {
//            edName.setText(noteBean.NTitle);
            tvTime.setText(noteBean.Ntime);
//            edDetail.setText(noteBean.NDetail);
            tvTitle.setText(noteBean.NBuyName);
//            spBuyUIDlg.setAutoSelection("",noteBean.NBuyName,false);
            spAddrUIDlg.setAutoSelection("",noteBean.NAddrName,false);
        }

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//        ab.setTitle("确认退出");
//        ab.setMessage("退出会自动执行完单,是否退出?");
//        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                ordercode++;
//                Log.e("ordercode", ordercode + "");
//                share.setOrderCode(OtherInStoreActivity.this, ordercode);
//                finish();
//            }
//        });
//        ab.setNegativeButton("取消", null);
//        ab.create().show();
    }

    @Override
    protected void OnReceive(String code) {

    }

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, ShowNoteActivity.class);
        intent.putExtra("id", id);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }

    @OnClick({R.id.iv_add, R.id.tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
//                if (!"".equals(edName.getText().toString())){
//                    noteBean.NTitle = edName.getText().toString();
//                    noteBean.NDetail = edDetail.getText().toString();
                    noteBean.Ntime = tvTime.getText().toString();
//                    noteBean.NBuyName = spBuyUIDlg.getDataName();
                    noteBean.NAddrName = spAddrUIDlg.getDataName();
                    noteBeanDao.update(noteBean);
                    finish();
//                }else{
//                    finish();
//                }
                break;
            case R.id.tv_time:
                datePicker(tvTime);
                break;
        }
    }
}
