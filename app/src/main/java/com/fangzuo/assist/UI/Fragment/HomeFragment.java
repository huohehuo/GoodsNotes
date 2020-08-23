package com.fangzuo.assist.UI.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Activity.ProductInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseOrderActivity;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Adapter.HomeRyAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Beans.TestBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.Dao.NoteBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.UI.Activity.AddNoteActivity;
import com.fangzuo.assist.UI.Activity.ShowNoteActivity;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.greendao.gen.BuyAtBeanDao;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.hawk.Hawk;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment {
    @BindView(R.id.ry_list)
    EasyRecyclerView ryList;
    private HomeRyAdapter adapter;
    private FragmentActivity mContext;
    private NoteBeanDao noteBeanDao;
    private BuyAtBeanDao buyAtBeanDao;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        mContext = getActivity();
        noteBeanDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getNoteBeanDao();
        buyAtBeanDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getBuyAtBeanDao();

        return v;
    }

    @Override
    public void initView() {
//        ryList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void initData() {
        adapter = new HomeRyAdapter(mContext);
        ryList.setAdapter(adapter);
        ryList.setLayoutManager(new LinearLayoutManager(mContext));
        ryList.setRefreshing(true);
        adapter.clear();
        adapter.addAll(noteBeanDao.queryBuilder().orderDesc(NoteBeanDao.Properties.Ntime).build().list());
        adapter.notifyDataSetChanged();
        ryList.setRefreshing(false);

        loadListData();
    }

    private void loadListData(){

        adapter.notifyDataSetChanged();
        //列表点击事件
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                NoteBean thisNo = adapter.getAllData().get(position);
                Lg.e("点击",thisNo);
                AddNoteActivity.start(mContext,thisNo.NBuyName+"");

            }
        });
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                new AlertDialog.Builder(mContext)
                        .setTitle("是否删除该条数据")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除附带的明细
                                buyAtBeanDao.deleteInTx(buyAtBeanDao.queryBuilder().where(BuyAtBeanDao.Properties.FBuyName.eq(adapter.getAllData().get(position).getNBuyName())).build().list());
                                //删除明细
                                noteBeanDao.deleteInTx(adapter.getAllData().get(position));
                                initData();
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void initListener() {
        TestBean buyBean = new TestBean();
        JsonObject object =new JsonObject();
        JSONObject objects =new JSONObject();


//        Lg.e("实体1",buyBean);
//        Lg.e("实体1",buyBean.FName);
//        Lg.e("实体1","FName".equals(buyBean.FName));
//        Lg.e("实体2",buyBean.getClass().getSimpleName());
//        Lg.e("实体3",buyBean.hashCode());
//
//        String string = "FName,FNumber,FUnit,FDate,FCreate";
//        String[] strings = string.split(",");
//        for (int i = 0; i < strings.length; i++) {
//            Lg.e("数组字段：",strings[i]);
//            if ("FName".equals(strings[i])){
////                buyBean.FName =
//            }
//        }


    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (App.hasChangeView){
                if (null != adapter)loadListData();
                App.hasChangeView = false;
            }
//            adapter.clear();
//            adapter.addAll(noteBeanDao.loadAll());
//            initData();
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    protected void OnReceive(String barCode) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
