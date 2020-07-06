package com.fangzuo.assist.UI.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.WebResponse;
import com.fangzuo.assist.Dao.AddrBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.BuyAtBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.UI.Activity.AboutActivity;
import com.fangzuo.assist.UI.Activity.BackUpActivity;
import com.fangzuo.assist.UI.Activity.BaseDataActivity;
import com.fangzuo.assist.UI.Activity.ScanTestActivity;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OwnFragment extends BaseFragment {
    @BindView(R.id.iv_book)
    ImageView ivBook;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.cb_change_view)
    CheckBox cbChangeView;
    @BindView(R.id.tv_mood)
    TextView tvMood;
    @BindView(R.id.tv_rili)
    TextView tvRili;
    @BindView(R.id.tv_loc_num)
    AppCompatTextView tvLocNum;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.tv_cloud_num)
    AppCompatTextView tvCloudNum;
    @BindView(R.id.ll_cloud)
    LinearLayout llCloud;
    @BindView(R.id.ll_lv)
    LinearLayout llLv;

    private FragmentActivity mContext;
    private NoteBeanDao noteBeanDao;
    private DaoSession daoSession;
    private boolean isChangeView = false;

    public OwnFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_own, container, false);
        ButterKnife.bind(this, v);
        mContext = getActivity();
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        noteBeanDao = daoSession.getNoteBeanDao();
        return v;
    }

    @Override
    public void initView() {
        cbChangeView.setChecked(Hawk.get(Info.CbChangeView, false));
        isChangeView = cbChangeView.isChecked();
        if (cbChangeView.isChecked()) {
            tvRili.setTextColor(mContext.getResources().getColor(R.color.red));
            tvMood.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            tvRili.setTextColor(mContext.getResources().getColor(R.color.black));
            tvMood.setTextColor(mContext.getResources().getColor(R.color.red));
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {
        //改变布局
        cbChangeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvRili.setTextColor(mContext.getResources().getColor(R.color.red));
                    tvMood.setTextColor(mContext.getResources().getColor(R.color.black));
                    Hawk.put(Info.ChangeView, 1);
                } else {
                    Hawk.put(Info.ChangeView, 0);
                    tvRili.setTextColor(mContext.getResources().getColor(R.color.black));
                    tvMood.setTextColor(mContext.getResources().getColor(R.color.red));
                }
                Hawk.put(Info.CbChangeView, isChecked);
                //当控件状态与初始化的不一致时，设置值给HomeFragment布局做更新state
                if (isChecked != isChangeView) {
                    App.hasChangeView = true;
                    isChangeView = isChecked;
                }
            }
        });
        tvMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbChangeView.setChecked(false);
            }
        });
        tvRili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbChangeView.setChecked(true);
            }
        });

        //        redorBlue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton radioButton = findViewById(i);
//                isRed = radioButton.getText().toString().equals("红字");
//                redblue = isRed ? "红字" : "蓝字";
//        if(checkedId==b1.getId()){
//            Toast.makeText(main.this,"b1选中", Toast.LENGTH_LONG).show();
//        }
//        if(checkedId==b2.getId()){
//            Toast.makeText(main.this,"b2选中", Toast.LENGTH_LONG).show();
//        }
//        if(checkedId==b3.getId()){
//            Toast.makeText(main.this,"b3选中", Toast.LENGTH_LONG).show();
//        }
//            }
//        });
    }
    private AlertDialog alertDialog;
    private void showLvDlg(){
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//        ab.setTitle("请选择物料");
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_lv_dlg, null);
        final EditText lv1 = v.findViewById(R.id.ed_lv1);final EditText lv1f = v.findViewById(R.id.ed_lv1_f);
        final EditText lv2 = v.findViewById(R.id.ed_lv2);final EditText lv2f = v.findViewById(R.id.ed_lv2_f);
        final EditText lv3 = v.findViewById(R.id.ed_lv3);final EditText lv3f = v.findViewById(R.id.ed_lv3_f);
        final EditText lv4 = v.findViewById(R.id.ed_lv4);
        final EditText lv4f = v.findViewById(R.id.ed_lv4_f);
        final TextView lv5 = v.findViewById(R.id.tv_lv5);
        final TextView lv5f = v.findViewById(R.id.tv_lv5_f);
        Button btn = v.findViewById(R.id.btn);
        lv1.setText(Hawk.get(Info.Num_Lv1,100)+"");lv1f.setText(Hawk.get(Info.Num_Lv1_f,20)+"");
        lv2.setText(Hawk.get(Info.Num_Lv2,200)+"");lv2f.setText(Hawk.get(Info.Num_Lv2_f,50)+"");
        lv3.setText(Hawk.get(Info.Num_Lv3,350)+"");lv3f.setText(Hawk.get(Info.Num_Lv3_f,110)+"");
        lv4.setText(Hawk.get(Info.Num_Lv4,500)+"");lv4f.setText(Hawk.get(Info.Num_Lv4_f,200)+"");
        lv5.setText("大于 "+Hawk.get(Info.Num_Lv4,500));lv5f.setText("大于 "+Hawk.get(Info.Num_Lv4_f,200));
        final List<EditText> list = new ArrayList<>();
        list.add(lv1);list.add(lv2);list.add(lv3);list.add(lv4);
        list.add(lv1f);list.add(lv2f);list.add(lv3f);list.add(lv4f);
        ab.setView(v);
        LoadingUtil.dismiss();
        lv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                lv5.setText("大于 "+lv4.getText().toString());
            }
        });
        lv4f.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                lv5f.setText("大于 "+lv4f.getText().toString());
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dealDlgNullText(list)){
                    Hawk.put(Info.Num_Lv1,MathUtil.toD(lv1.getText().toString()));Hawk.put(Info.Num_Lv1_f,MathUtil.toD(lv1f.getText().toString()));
                    Hawk.put(Info.Num_Lv2,MathUtil.toD(lv2.getText().toString()));Hawk.put(Info.Num_Lv2_f,MathUtil.toD(lv2f.getText().toString()));
                    Hawk.put(Info.Num_Lv3,MathUtil.toD(lv3.getText().toString()));Hawk.put(Info.Num_Lv3_f,MathUtil.toD(lv3f.getText().toString()));
                    Hawk.put(Info.Num_Lv4,MathUtil.toD(lv4.getText().toString()));Hawk.put(Info.Num_Lv4_f,MathUtil.toD(lv4f.getText().toString()));
                    Toast.showText(mContext,"设置成功");
                    App.hasChangeView = true;
                    alertDialog.dismiss();
                }else{
                    LoadingUtil.showAlter(mContext,"注意","等级不能为空");
                }
            }
        });

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                BarCode barCode = (BarCode) productselectAdapter.getItem(i);
//                products = productDao.queryBuilder().where(
//                        ProductDao.Properties.FItemID.eq(barCode.FItemID)
//                ).build().list();
//                default_unitID = barCode.FUnitID;
////                                chooseUnit(default_unitID);
//                product = products.get(0);
//                tvorIsAuto(product);
//                alertDialog.dismiss();
//            }
//        });
        alertDialog = ab.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    //检测是否存在为输入的数字
    private boolean dealDlgNullText(List<EditText> list){
        boolean hasNull=false;
        for (int i = 0; i < list.size(); i++) {
            if ("".equals(list.get(i).getText().toString())){
                hasNull = true;
                break;
            }
        }
        return hasNull;

    }

    @Override
    protected void OnReceive(String barCode) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (null != noteBeanDao) tvNum.setText("叙:" + noteBeanDao.loadAll().size());
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_book, R.id.tv_num,R.id.ll_data, R.id.ll_cloud, R.id.ll_lv, R.id.tv_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_book:
                break;
            case R.id.tv_num:
                tvNum.setText("叙:" + noteBeanDao.loadAll().size());

                break;
            case R.id.ll_data:
                BaseDataActivity.start(mContext);
                break;
            case R.id.ll_cloud:
                BackUpActivity.start(mContext);
//                ScanTestActivity.start(mContext);
                break;
            case R.id.ll_lv:
                LoadingUtil.showDialog(mContext,"正在获取...");
               showLvDlg();
                break;
            case R.id.tv_about:
                AboutActivity.start(mContext);
                break;
        }
    }


}
