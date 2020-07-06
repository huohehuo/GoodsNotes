package com.fangzuo.assist.UI.Fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.BottomSheetDialogListView;
import com.fangzuo.assist.widget.SpringBackBottomSheetDialog;
import com.fangzuo.greendao.gen.NoteBeanDao;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RiliFragment extends BaseFragment {


//    @BindView(R.id.calendarView)
//    CalendarView calendarView;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.picker)
    LinearLayout picker;
    @BindView(R.id.cdv_date)
    com.haibin.calendarview.CalendarView cdvDate;
    private FragmentActivity mContext;
    private NoteBeanDao noteBeanDao;
    private boolean isChangeView = false;

    public RiliFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rili, container, false);
        ButterKnife.bind(this, v);
        mContext = getActivity();
        noteBeanDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getNoteBeanDao();
        return v;
    }

    @Override
    public void initView() {
//        cbChangeView.setChecked(Hawk.get(Info.CbChangeView, false));
//        isChangeView = cbChangeView.isChecked();
//        if (cbChangeView.isChecked()) {
//            tvRili.setTextColor(mContext.getResources().getColor(R.color.red));
//            tvMood.setTextColor(mContext.getResources().getColor(R.color.black));
//        } else {
//            tvRili.setTextColor(mContext.getResources().getColor(R.color.black));
//            tvMood.setTextColor(mContext.getResources().getColor(R.color.red));
//        }
    }

    private Map<String, Calendar> map = new HashMap<>();
    @Override
    protected void initData() {
//        Calendar calendar1 = getSchemeCalendar(2019, 12, 11, "1");
//        Calendar calendar2 = getSchemeCalendar(2019, 12, 12, "2");
//        Calendar calendar3 = getSchemeCalendar(2019, 12, 13, "3");
//        Calendar calendar4 = getSchemeCalendar(2019, 12, 6, "4");
//        map.put(calendar1.toString(), calendar1);
//        map.put(calendar2.toString(), calendar2);
//        map.put(calendar3.toString(), calendar3);
//        map.put(calendar4.toString(), calendar4);
//        cdvDate.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setScheme(text);
        return calendar;
    }
    private Calendar getSchemeCalendar( Calendar calendar, String text) {
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {
        tvMonth.setText(cdvDate.getCurYear() + "年" + cdvDate.getCurMonth() + "月");
        //月份切换改变事件
        cdvDate.setOnMonthChangeListener(new com.haibin.calendarview.CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                tvMonth.setText(year + "年" + month + "月");
            }
        });
        final boolean[] type = {true, true, false, false, false, false};
        //时间选择器选择年月，对应的日历切换到指定日期
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        java.util.Calendar c = java.util.Calendar.getInstance();
                        c.setTime(date);
                        int year = c.get(java.util.Calendar.YEAR);
                        int month = c.get(java.util.Calendar.MONTH);
                        //滚动到指定日期
                        cdvDate.scrollToCalendar(year, month + 1, 1);
                    }
                }).setType(type).build();
                pvTime.show();
            }
        });
//        cdvDate.setOnDateSelectedListener(new COnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Calendar calendar, boolean isClick) {
//
//                //显示用户选择的日期
//                Toast.showText(mContext, year + "年" + month + "月" + dayOfMonth + "日");
//            }
//        });
        cdvDate.setOnCalendarSelectListener(new com.haibin.calendarview.CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
                Lg.e("改变？？？",calendar.toString());
            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                Lg.e("改变？？？!",calendar.toString());
                Lg.e("点击日历",calendar.toString());
                setDataBg(calendar);
                showCustomSheet();
            }
        });


//        //改变布局
//        cbChangeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    tvRili.setTextColor(mContext.getResources().getColor(R.color.red));
//                    tvMood.setTextColor(mContext.getResources().getColor(R.color.black));
//                    Hawk.put(Info.ChangeView, 1);
//                } else {
//                    Hawk.put(Info.ChangeView, 0);
//                    tvRili.setTextColor(mContext.getResources().getColor(R.color.black));
//                    tvMood.setTextColor(mContext.getResources().getColor(R.color.red));
//                }
//                Hawk.put(Info.CbChangeView, isChecked);
//                //当控件状态与初始化的不一致时，设置值给HomeFragment布局做更新state
//                if (isChecked != isChangeView) {
//                    App.hasChangeView = true;
//                    isChangeView = isChecked;
//                }
//            }
//        });
//        tvMood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cbChangeView.setChecked(false);
//            }
//        });
//        tvRili.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cbChangeView.setChecked(true);
//            }
//        });

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
    private void setDataBg(Calendar calendar){
        Map<String, Calendar> map = new HashMap<>();
        map.put(calendar.toString(), getSchemeCalendar(calendar,"4"));
        cdvDate.setSchemeDate(map);

    }

    private void showCustomSheet(){
        SpringBackBottomSheetDialog c = new SpringBackBottomSheetDialog(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.list,null,false);
        BottomSheetDialogListView l = v.findViewById(R.id.listview);
        initListView(l);
        c.setContentView(v);

        l.bindBottomSheetDialog(v);
        c.addSpringBackDisLimit(-1);

        c.show();
    }

    private void initListView(final BottomSheetDialogListView l){
        final List<String> datas = new ArrayList<>();

        for(int i=0;i<40;i++){
            datas.add(String.valueOf(i));
        }

        l.setAdapter(
                new ListAdapter() {
                    @Override
                    public boolean areAllItemsEnabled() {
                        return false;
                    }

                    @Override
                    public boolean isEnabled(int position) {
                        return false;
                    }

                    @Override
                    public void registerDataSetObserver(DataSetObserver observer) {

                    }

                    @Override
                    public void unregisterDataSetObserver(DataSetObserver observer) {

                    }

                    @Override
                    public int getCount() {
                        return datas.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return datas.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public boolean hasStableIds() {
                        return false;
                    }

                    @Override
                    public View getView(final int position, View convertView,final ViewGroup parent) {
                        if(convertView == null){
                            convertView = new TextView(parent.getContext());
                            convertView.setLayoutParams(
                                    new AbsListView.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            40*3 // 40dp
                                    )
                            );
                        }
                        TextView t = (TextView) convertView;
                        t.setTextColor(Color.BLACK);
                        t.setGravity(Gravity.CENTER);
                        t.setText(datas.get(position));
                        t.setTextSize(17);
                        t.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        android.widget.Toast.makeText(parent.getContext(),""+position, android.widget.Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                        t.setOnTouchListener(
                                new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                                            l.setCoordinatorDisallow();
                                        }
                                        return false;
                                    }
                                }
                        );
                        return t;
                    }

                    @Override
                    public int getItemViewType(int position) {
                        return 0;
                    }

                    @Override
                    public int getViewTypeCount() {
                        return 1;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                }
        );
    }

    @Override
    protected void OnReceive(String barCode) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            tvNum.setText("叙:" + noteBeanDao.loadAll().size());
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    @OnClick({R.id.iv_book, R.id.tv_num})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_book:
//                break;
//            case R.id.tv_num:
////                tvNum.setText("叙:" + noteBeanDao.loadAll().size());
//
//                break;
//        }
//    }
}
