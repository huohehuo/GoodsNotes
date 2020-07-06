package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangzuo.assist.Dao.BuyAtBean;
import com.fangzuo.assist.Dao.BuyBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.LocDataUtil;
import com.fangzuo.assist.Utils.MathUtil;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.hawk.Hawk;

public class BuyAtRyAdapter extends RecyclerArrayAdapter<BuyAtBean> {
    Context context;
    public BuyAtRyAdapter(Context context) {
        super(context);
        this.context = context;
    }
//    @Override
//    public int getViewType(int position) {
//        if (Hawk.get(Info.ChangeView,0)==0){
//            return 1;
//        }else{
//            return 2;
//        }

//    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == 1){
            return new MarkHolder(parent);
//        }else{
//            return new MarkHolderForDate(parent);
//        }
    }
    class MarkHolder extends BaseViewHolder<BuyAtBean> {

        private TextView name;
        private AppCompatTextView tvAddr;
        private TextView time;
        private TextView tvSign;
        public MarkHolder(ViewGroup parent) {
            super(parent, R.layout.item_buyat);
            name= $(R.id.tv_name);
            tvAddr= $(R.id.tv_addr);
            time= $(R.id.tv_time);
            tvSign= $(R.id.tv_sign);
        }

        @Override
        public void setData(BuyAtBean data) {
            super.setData(data);
            name.setText(data.FAddrName);
            tvAddr.setText(data.FNum);
            time.setText(data.FCreateData);
            tvSign.setBackgroundColor(getColor(data.FNum));
        }
    }

    private int getColor(String num){
        double res = MathUtil.toD(num);
        if (res<= Hawk.get(Info.Num_Lv1_f,20d))return context.getResources().getColor(R.color.buylv1);
        if (res>Hawk.get(Info.Num_Lv1_f,20d) && res<=Hawk.get(Info.Num_Lv2_f,50d))return context.getResources().getColor(R.color.buylv2);
        if (res>Hawk.get(Info.Num_Lv2_f,50d) && res<=Hawk.get(Info.Num_Lv3_f,110d))return context.getResources().getColor(R.color.buylv3);
        if (res>Hawk.get(Info.Num_Lv3_f,110d) && res<=Hawk.get(Info.Num_Lv4_f,200d))return context.getResources().getColor(R.color.buylv4);
        if (res>Hawk.get(Info.Num_Lv5_f,200d))return context.getResources().getColor(R.color.buylv5);
        return context.getResources().getColor(R.color.white);
    }
//
//    class MarkHolderForDate extends BaseViewHolder<BuyAtBean> {
//
//        private TextView name;
//        private TextView time;
//        private TextView time_left;
//        private TextView detail;
//        private ImageView icon;
//        public MarkHolderForDate(ViewGroup parent) {
//            super(parent, R.layout.item_home_date);
//            name= $(R.id.tv_name);
//            time= $(R.id.tv_time);
//            time_left= $(R.id.tv_date_left);
//            icon= $(R.id.iv_icon);
//            detail= $(R.id.tv_detail);
////            checkBox = $(R.id.view_cb);
//        }
//
//        @Override
//        public void setData(BuyAtBean data) {
//            super.setData(data);
//            name.setText(data.NTitle);
//            time.setText(data.Ntime);
//            time_left.setText(data.Ntime);
//            if (null==data.NDetail || "".equals(data.NDetail)){
//                detail.setVisibility(View.GONE);
//            }else{
//                detail.setText(data.NDetail);
//            }
//            Glide.with(getContext())
//                    .load(CommonUtil.getMoodByType(data.NMoodLocInt))
////                    .load(R.drawable.happy)
////                    .load(CommonUtil.getPicServerUrl()+data.getPicName())
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
//                    .into(icon);
//        }
//    }



//    //纯文字布局
//    class MainHolderForTxt extends BaseViewHolder<PlanBean> {
//
//        private TextView time;
//        private TextView eesay;
//        private ImageView favour;
//        private TextView num;
//        public MainHolderForTxt(ViewGroup parent) {
//            super(parent, R.layout.item_plan_for_txt);
//            time = $(R.id.tv_time);
//            eesay = $(R.id.tv_main_essay);
//            num = $(R.id.tv_favour);
//            favour = $(R.id.iv_favour);
//        }
//
//        @Override
//        public void setData(PlanBean data) {
//            super.setData(data);
//            eesay.setText(data.getEssay());
//            time.setText(data.getCreatedAt());
////            num.setText(data.getFavour().get__op());
//
//            favour.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(App.getContext(), "喜欢+1", Toast.LENGTH_SHORT).show();
//                }
//            });
//
////            Glide.with(getContext())
////                    .load(data.getPic())
////                    .placeholder(R.mipmap.ic_launcher)
////                    .centerCrop()
////                    .into(imageView);
//
//        }
//    }
}
