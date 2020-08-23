package com.fangzuo.assist.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.BaseDataColorRyAdapter;
import com.fangzuo.assist.Adapter.BaseDataModelRyAdapter;
import com.fangzuo.assist.Adapter.BaseDataStuffRyAdapter;
import com.fangzuo.assist.Adapter.BaseDataUnitRyAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.BuyAtBean;
import com.fangzuo.assist.Dao.ColorBean;
import com.fangzuo.assist.Dao.ModelBean;
import com.fangzuo.assist.Dao.StuffBean;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.UnitBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.greendao.gen.BuyAtBeanDao;
import com.fangzuo.greendao.gen.ColorBeanDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.ModelBeanDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.StuffBeanDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.UnitBeanDao;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchDataActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.ry_list)
    EasyRecyclerView ryList;
    @BindView(R.id.cancle)
    LinearLayout cancle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.model)
    TextView model;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.pg)
    ProgressBar pg;
    @BindView(R.id.et_search)
    EditText edSearch;
    @BindView(R.id.btn_add)
    Button btnAdd;
    private String searchString="";
    private String searchBuyAt="";
    private String searchString4wavehouse;
    private String backBus;
    private BuyAtBeanDao buyAtBeanDao;
    private SearchDataActivity mContext;
    private BaseDataModelRyAdapter adapterModel;
    private BaseDataStuffRyAdapter adapterStuff;
    private BaseDataColorRyAdapter adapterColor;
    private BaseDataUnitRyAdapter adapterUnit;

    @Override
    public void initView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_data);
        ButterKnife.bind(this);
        mContext = this;
        ryList.setLayoutManager(new LinearLayoutManager(mContext));
        //接收
        Intent intent = getIntent();
        if (intent != null) {
            searchBuyAt = intent.getStringExtra("buyat");
//            searchString = intent.getStringExtra("search");
//            searchString4wavehouse = intent.getStringExtra("search_storage");
            backBus = intent.getStringExtra("backBus");
//            where = intent.getIntExtra("where",0);
//            fidcontainer = intent.getStringArrayListExtra("fid");
//            Lg.e("Intent:"+fidcontainer.toString());
        }
        edSearch.setText(searchString);
        buyAtBeanDao = daoSession.getBuyAtBeanDao();
        if (EventBusInfoCode.Event_Model.equals(backBus)) title.setText("查询(规格)");
        if (EventBusInfoCode.Event_Color.equals(backBus)) title.setText("查询(颜色)");
        if (EventBusInfoCode.Event_Stuff.equals(backBus)) title.setText("查询(材料)");
        if (EventBusInfoCode.Event_Unit.equals(backBus)) title.setText("查询(单位)");
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        super.receiveEvent(event);
    }

    @Override
    public void initData() {
        pg.setVisibility(View.VISIBLE);
        if (EventBusInfoCode.Event_Model.equals(backBus)) {
//            model.setText("编码");name.setText("名称");
            adapterModel = new BaseDataModelRyAdapter(mContext);
            ModelBeanDao beanDao = daoSession.getModelBeanDao();
            List<ModelBean> list = beanDao.queryBuilder().where(
                    ModelBeanDao.Properties.FName.like("%"+edSearch.getText().toString()+"%")
            ).orderAsc(ModelBeanDao.Properties.FName).build().list();
            ryList.setAdapter(adapterModel);
            adapterModel.setClientName(searchBuyAt,buyAtBeanDao);
            ryList.setRefreshing(true);
            adapterModel.clear();
            adapterModel.addAll(list);
            adapterModel.notifyDataSetChanged();
            ryList.setRefreshing(false);
            if (adapterModel.getCount()>0){
                btnAdd.setVisibility(View.GONE);
                tvTip.setVisibility(View.GONE);
            }else{
                tvTip.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.VISIBLE);
            }
            pg.setVisibility(View.GONE);
        }else if (EventBusInfoCode.Event_Stuff.equals(backBus)) {
            adapterStuff = new BaseDataStuffRyAdapter(mContext);
            StuffBeanDao beanDao = daoSession.getStuffBeanDao();
            List<StuffBean> list = beanDao.queryBuilder().where(
                    StuffBeanDao.Properties.FName.like("%"+edSearch.getText().toString()+"%")
            ).orderAsc(StuffBeanDao.Properties.FName).build().list();
            ryList.setAdapter(adapterStuff);
            adapterStuff.setClientName(searchBuyAt,buyAtBeanDao);
            ryList.setRefreshing(true);
            adapterStuff.clear();
            adapterStuff.addAll(list);
            adapterStuff.notifyDataSetChanged();
            ryList.setRefreshing(false);
            if (adapterStuff.getCount()>0){
                btnAdd.setVisibility(View.GONE);
                tvTip.setVisibility(View.GONE);
            }else{
                btnAdd.setVisibility(View.VISIBLE);
                tvTip.setVisibility(View.VISIBLE);
            }
            pg.setVisibility(View.GONE);
        }else if (EventBusInfoCode.Event_Color.equals(backBus)) {
            adapterColor = new BaseDataColorRyAdapter(mContext);
            ColorBeanDao beanDao = daoSession.getColorBeanDao();
            List<ColorBean> list = beanDao.queryBuilder().where(
                    ColorBeanDao.Properties.FName.like("%"+edSearch.getText().toString()+"%")
            ).orderAsc(ColorBeanDao.Properties.FName).build().list();
            ryList.setAdapter(adapterColor);
            adapterColor.setClientName(searchBuyAt,buyAtBeanDao);
            ryList.setRefreshing(true);
            adapterColor.clear();
            adapterColor.addAll(list);
            adapterColor.notifyDataSetChanged();
            ryList.setRefreshing(false);
            if (adapterColor.getCount()>0){
                btnAdd.setVisibility(View.GONE);
                tvTip.setVisibility(View.GONE);
            }else{
                btnAdd.setVisibility(View.VISIBLE);
                tvTip.setVisibility(View.VISIBLE);
            }
            pg.setVisibility(View.GONE);
        }else if (EventBusInfoCode.Event_Unit.equals(backBus)) {
            adapterUnit = new BaseDataUnitRyAdapter(mContext);
            UnitBeanDao beanDao = daoSession.getUnitBeanDao();
            List<UnitBean> list = beanDao.queryBuilder().where(
                    UnitBeanDao.Properties.FName.like("%"+edSearch.getText().toString()+"%")
            ).orderAsc(UnitBeanDao.Properties.FName).build().list();
            ryList.setAdapter(adapterUnit);
            adapterUnit.setClientName(searchBuyAt,buyAtBeanDao);
            ryList.setRefreshing(true);
            adapterUnit.clear();
            adapterUnit.addAll(list);
            adapterUnit.notifyDataSetChanged();
            ryList.setRefreshing(false);
            if (adapterUnit.getCount()>0){
                btnAdd.setVisibility(View.GONE);
                tvTip.setVisibility(View.GONE);
            }else{
                btnAdd.setVisibility(View.VISIBLE);
                tvTip.setVisibility(View.VISIBLE);
            }
            pg.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        switch (backBus){
            case EventBusInfoCode.Event_Model:
                adapterModel.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        EventBusUtil.sendEvent(new ClassEvent(backBus, adapterModel.getAllData().get(position).FName));
                        onBackPressed();
                    }
                });
                break;
            case EventBusInfoCode.Event_Stuff:
                adapterStuff.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        EventBusUtil.sendEvent(new ClassEvent(backBus, adapterStuff.getAllData().get(position).FName));
                        onBackPressed();
                    }
                });
                break;
            case EventBusInfoCode.Event_Color:
                adapterColor.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        EventBusUtil.sendEvent(new ClassEvent(backBus, adapterColor.getAllData().get(position).FName));
                        onBackPressed();
                    }
                });
                break;
            case EventBusInfoCode.Event_Unit:
                adapterUnit.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        EventBusUtil.sendEvent(new ClassEvent(backBus, adapterUnit.getAllData().get(position).FName));
                        onBackPressed();
                    }
                });
                break;
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {//该按钮只有在列表为空时，才会显示
            @Override
            public void onClick(View v) {
                switch (backBus){
                    case EventBusInfoCode.Event_Model:
                        if (daoSession.getModelBeanDao().queryBuilder().where(ModelBeanDao.Properties.FName.eq(edSearch.getText().toString())).count()<=0){
                            daoSession.getModelBeanDao().insert(new ModelBean(edSearch.getText().toString(), CommonUtil.getTime(true), CommonUtil.getTimeLong(false)));
                            EventBusUtil.sendEvent(new ClassEvent(backBus, edSearch.getText().toString()));
                            onBackPressed();
                        }
                        break;
                    case EventBusInfoCode.Event_Stuff:
                        if (daoSession.getStuffBeanDao().queryBuilder().where(StuffBeanDao.Properties.FName.eq(edSearch.getText().toString())).count()<=0){
                            daoSession.getStuffBeanDao().insert(new StuffBean(edSearch.getText().toString(), CommonUtil.getTime(true), CommonUtil.getTimeLong(false)));
                            EventBusUtil.sendEvent(new ClassEvent(backBus, edSearch.getText().toString()));
                            onBackPressed();
                        }
                        break;
                    case EventBusInfoCode.Event_Color:
                        if (daoSession.getColorBeanDao().queryBuilder().where(ColorBeanDao.Properties.FName.eq(edSearch.getText().toString())).count()<=0){
                            daoSession.getColorBeanDao().insert(new ColorBean(edSearch.getText().toString(), CommonUtil.getTime(true), CommonUtil.getTimeLong(false)));
                            EventBusUtil.sendEvent(new ClassEvent(backBus, edSearch.getText().toString()));
                            onBackPressed();
                        }
                        break;
                    case EventBusInfoCode.Event_Unit:
                        if (daoSession.getUnitBeanDao().queryBuilder().where(UnitBeanDao.Properties.FName.eq(edSearch.getText().toString())).count()<=0){
                            daoSession.getUnitBeanDao().insert(new UnitBean(edSearch.getText().toString(), CommonUtil.getTime(true), CommonUtil.getTimeLong(false)));
                            EventBusUtil.sendEvent(new ClassEvent(backBus, edSearch.getText().toString()));
                            onBackPressed();
                        }
                        break;
                }
            }
        });







        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initData();
            }
        });

    }

    @Override
    protected void OnReceive(String code) {

    }

    public static void start(Context context,String searchBuyAt, String backBus) {
        Intent starter = new Intent(context, SearchDataActivity.class);
        starter.putExtra("buyat", searchBuyAt);
        starter.putExtra("backBus", backBus);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }




    public static void start(Context context, String search, String search_storage, String backBus) {
        Intent starter = new Intent(context, SearchDataActivity.class);
        starter.putExtra("search", search);
        starter.putExtra("search_storage", search_storage);
        starter.putExtra("backBus", backBus);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
        this.overridePendingTransition(R.anim.bottom_end, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(0, R.anim.bottom_end);
    }
}


