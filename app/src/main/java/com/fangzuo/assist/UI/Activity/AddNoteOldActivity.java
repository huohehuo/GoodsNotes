package com.fangzuo.assist.UI.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.MoodRyAdapter;
import com.fangzuo.assist.Beans.MoodBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.ImageUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.VibratorUtil;
import com.fangzuo.assist.widget.SpinnerAddrUIDlg;
import com.fangzuo.assist.widget.SpinnerBuyUIDlg;
import com.fangzuo.assist.widget.piccut.CropImageActivity;
import com.fangzuo.assist.widget.piccut.SelectPhotoDialog;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class AddNoteOldActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.ry_icon_list)
    EasyRecyclerView ryIconList;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ed_detail)
    EditText edDetail;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.iv_mood)
    ImageView ivMood;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.sp_buy)
    SpinnerBuyUIDlg spBuyUIDlg;
    @BindView(R.id.sp_addr)
    SpinnerAddrUIDlg spAddrUIDlg;

    private MoodRyAdapter adapter;
    private NoteBeanDao noteBeanDao;
    private SelectPhotoDialog selectPhotoDialog;
    String getPicFormPhone="";
    private byte[] imagebyte;
    private int moodInt;
    private NoteBean noteBean;
    public static String baseLoc = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initBar();
        getPermisssion();
        noteBean = new NoteBean();
        selectPhotoDialog = new SelectPhotoDialog(AddNoteOldActivity.this, R.style.CustomDialog);
        noteBeanDao = daoSession.getNoteBeanDao();

        ryIconList.setAdapter(adapter = new MoodRyAdapter(mContext));
//        ryIconList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ryIconList.setLayoutManager(new GridLayoutManager(this,6));
//        ryList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void initData() {
        tvTime.setText(CommonUtil.getTime(true));
        adapter.addAll(CommonUtil.getMoodBeanList());
//        ivMood.setImageResource(R.drawable.happy);
        ivMood.setBackground(getResources().getDrawable(R.drawable.happy));
        moodInt = 0;
        spBuyUIDlg.setAutoSelection("","",false);
        spAddrUIDlg.setAutoSelection("","",false);
    }

    @Override
    protected void initListener() {
        ivMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ryIconList.getVisibility()==View.GONE){
                    ryIconList.setVisibility(View.VISIBLE);
                }else{
                    ryIconList.setVisibility(View.GONE);
                }
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MoodBean moodBean = adapter.getAllData().get(position);
                Lg.e("选择图标",moodBean);
                ivMood.setBackground(getResources().getDrawable(moodBean.MLocint));
                moodInt = moodBean.type;

            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, AddNoteOldActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }


    @OnClick({R.id.iv_add, R.id.tv_time, R.id.btn_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                saveNote();
                break;
            case R.id.tv_time:
                datePicker(tvTime);
                break;
            case R.id.btn_choose:
                getPic();
                break;
        }
    }

    private void saveNote(){
        noteBean = new NoteBean();
//        noteBean.NTitle= edName.getText().toString();
//        noteBean.NDetail= edDetail.getText().toString();
//        noteBean.NBuyName= spBuyUIDlg.getDataName();
//        noteBean.NAddrName= spAddrUIDlg.getDataName();
//        noteBean.Ntime = tvTime.getText().toString();
//        noteBean.NCreateTime = CommonUtil.getTimeLong(true);
//        noteBean.NMoodLocInt = moodInt;
//        noteBeanDao.insert(noteBean);
//        Toast.showText(mContext, "添加成功");
//        VibratorUtil.Vibrate(mContext, 250);
    }





    //获取照片
    private void getPic(){
        selectPhotoDialog.setDialogCallBack(new SelectPhotoDialog.SelectPhoteDialogCallBack() {
            @Override
            public void OnclickLiseten(int id) {
                switch (id) {
                    case SelectPhotoDialog.BN_TAKEPHOTO:
//                        File file = new File(Pic_Path);
//                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
//                                mContext.getPackageName()+".new.provider",
//                                file);
//                        ivPic.setBackground(getResources().getDrawable(adapter.getAllData().get(position).MLocint));
//                        Hawk.put("pic",((BitmapDrawable) iv.getDrawable()).getBitmap());
//                        if (null==ImageUtil.viewToBitmap(ivPic)){
//                            @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.happy);
//                            Bitmap mBitmap = BitmapFactory.decodeStream(is);
//                            noteBean.NMoodByte = ImageUtil.getBitmap2Byte(mBitmap);
//                        }else{
//                            noteBean.NMoodByte = ImageUtil.getBitmap2Byte(((BitmapDrawable) ivPic.getDrawable()).getBitmap());
//
//                        }

                        getPicFormPhone =baseLoc+"fangzuo/fzkj-camera"+CommonUtil.getTimeLong(false)+".jpg";
                        CropImageActivity.startActivity(AddNoteOldActivity.this, getPicFormPhone, true, 3);
                        selectPhotoDialog.dismiss();
                        break;
                    case SelectPhotoDialog.BN_SELECTPHOTO:
                        getPicFormPhone =baseLoc+"fangzuo/fzkj-camera"+CommonUtil.getTimeLong(false)+".jpg";
                        CropImageActivity.startActivity(AddNoteOldActivity.this, getPicFormPhone, false, 2);
                        selectPhotoDialog.dismiss();
                        break;
                    case SelectPhotoDialog.BN_CANCEL:
                        selectPhotoDialog.dismiss();
                        break;
                }
            }
        });
        selectPhotoDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
//                case REQUEST_NICKNAME:
////                    binding.tvNickName.setText(data.getStringExtra(RESULT));
//                    break;
//                case REQUEST_MOTTO:
////                    binding.tvMotto.setText(data.getStringExtra(RESULT));
//                    break;
                case 2:
                    Log.e("pp", "获取到图片");
                    // Glide.with(EditMyInfoActivity.this).load(new File(URL.PATH_SELECT_AVATAR)).into(binding.ciAvatar);
                    Bitmap bitmap = BitmapFactory.decodeFile(getPicFormPhone);
                    if (bitmap != null) {
                        imagebyte = ImageUtil.getBitmap2Byte(bitmap);
                    }
                    ivPic.setImageBitmap(bitmap);
//                    basePic = bitmap;
                    break;
                case 3:
                    Log.e("pp", "获取到图片");
                    // Glide.with(EditMyInfoActivity.this).load(new File(URL.PATH_SELECT_AVATAR)).into(binding.ciAvatar);
                    Bitmap bitmap2 = BitmapFactory.decodeFile(getPicFormPhone);
                    if (bitmap2 != null) {
                        imagebyte = ImageUtil.getBitmap2Byte(bitmap2);
                    }
                    ivPic.setImageBitmap(bitmap2);
//                    basePic = bitmap2;
                    break;
            }
        }
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
