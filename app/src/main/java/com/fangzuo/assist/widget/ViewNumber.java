package com.fangzuo.assist.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.VibratorUtil;

import java.util.Calendar;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewNumber extends RelativeLayout {
    @BindView(R.id.tv_result)
    AppCompatTextView tvResult;
    @BindView(R.id.tvdel)
    TextView tvdel;
    Context mContext;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.tv9)
    TextView tv9;
    @BindView(R.id.tv0)
    TextView tv0;
    @BindView(R.id.tvdian)
    TextView tvdian;
    @BindColor(R.color.cpb_blue)
    int blue;
    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.red)
    int red;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_jia)
    TextView tvJia;
    @BindView(R.id.tv_jian)
    TextView tvJian;
    @BindView(R.id.sp_addr)
    SpinnerAddrUIDlg spAddrUIDlg;

    public ViewNumber(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_number, this);
        ButterKnife.bind(this);
        mContext = context;
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        tvDay.setText(CommonUtil.getTime(true));
        spAddrUIDlg.setAutoSelection("","",true);
    }


    //数量变化
    private void setTvResult(String num) {
        String s = tvResult.getText().toString();
        Lg.e("numssss",s);
        Lg.e("num???",num);
        if ("ok".equals(num)){
            if (s.contains("+")){
                String[] split = s.split("\\+", 2);
                Lg.e("得到",split);
                if (!checkNum(split))return;//检测小数点
                if (split.length==1){
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,split[0]));
                    return;
                }
                if ("".equals(split[1])){
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,split[0]));
                    return;
                }
                String res = MathUtil.sum(split[0],split[1])+"";
                if (res.contains("-")){
                    Toast.showText(mContext,"不允许出现负数");
                    return;
                }
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,res));
            }else if (s.contains("-")){
                String[] split = s.split("-", 2);
                Lg.e("得到",split);
                if (!checkNum(split))return;//检测小数点
                if (split.length==1){
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,split[0]));
                    return;
                }
                if ("".equals(split[1])){
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,split[0]));
                    return;
                }
                String res = MathUtil.sub(split[0],split[1])+"";
                if (res.contains("-")){
                    Toast.showText(mContext,"不允许出现负数");
                    return;
                }
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,res));
            }else{
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.View_Save,tvResult.getText().toString()));
            }
        }else if (s.contains("+")){
            if ("+".equals(num)){
                String[] split = s.split("\\+", 2);
                Lg.e("得到",split);
                if (!checkNum(split))return;
                if (split.length>1 && "".equals(split[1]))return;
                tvResult.setText(MathUtil.sum(split[0],split[1])+num);
            }else if ("-".equals(num)){
                String[] split = s.split("\\+", 2);
                if (split.length==1 ||"".equals(split[1])){//当截取的第二个为空，则返回
                    tvResult.setText(s.replace("+","-"));
                    return;
                }
                if (!checkNum(split))return;
                if (split.length>1 && "".equals(split[1]))return;
                tvResult.setText(MathUtil.sum(split[0],split[1])+"-");
                return;
            }else{
                tvResult.setText(s + num);
            }
        }else if (s.contains("-")){
            if ("-".equals(num)){
                String[] split = s.split("-", 2);
                Lg.e("得到",split);
                if (!checkNum(split))return;
                if (split.length>1 && "".equals(split[1]))return;
                String res = MathUtil.sub(split[0],split[1])+"";
                if (res.contains("-")){
                    Toast.showText(mContext,"不允许出现负数");
                    return;
                }
                tvResult.setText(res+num);
            }else if ("+".equals(num)){
                String[] split = s.split("-", 2);
                if (split.length==1 ||"".equals(split[1])){//当截取的第二个为空，则返回
                    tvResult.setText(s.replace("-","+"));
                    return;
                }
                if (!checkNum(split))return;
                if (split.length>1 && "".equals(split[1]))return;
                String res = MathUtil.sub(split[0],split[1])+"";
                if (res.contains("-")){
                    Toast.showText(mContext,"不允许出现负数");
                    return;
                }
                tvResult.setText(res+num);
                return;
            }else{
                tvResult.setText(s + num);
            }
        }else {
            tvResult.setText(s + num);
        }
    }
    //检测小数点是否符合要求
    private boolean checkNum(String[] split){
        if (split.length == 1){
            int num=0;
            String[] sp = split[0].split("");
            for (int i = 0; i < sp.length; i++) {
                if (".".equals(sp[i]))num++;
            }
            if (num>=2){
                Toast.showText(mContext,"小数位有误，请检查运算");
                return false;
            }
        }else if (split.length == 2){
//            if ("".equals(split[1]))return false;
            int num=0;
            int num2=0;
            String[] sp = split[0].split("");
            String[] sp2 = split[1].split("");
            for (int i = 0; i < sp.length; i++) {
                if (".".equals(sp[i]))num++;
            }
            for (int i = 0; i < sp2.length; i++) {
                if (".".equals(sp2[i]))num2++;
            }
            if (num>=2 || num2>=2){
                Toast.showText(mContext,"小数位有误，请检查运算");
                return false;
            }
        }
        return true;
    }


    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tv7, R.id.tv8, R.id.tv9, R.id.tv0, R.id.tvdian, R.id.tvdel,R.id.iv_history, R.id.tv_day, R.id.tv_jian, R.id.tv_jia, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                setTvResult("1");
                break;
            case R.id.tv2:
                setTvResult("2");
                break;
            case R.id.tv3:
                setTvResult("3");
                break;
            case R.id.tv4:
                setTvResult("4");
                break;
            case R.id.tv5:
                setTvResult("5");
                break;
            case R.id.tv6:
                setTvResult("6");
                break;
            case R.id.tv7:
                setTvResult("7");
                break;
            case R.id.tv8:
                setTvResult("8");
                break;
            case R.id.tv9:
                setTvResult("9");
                break;
            case R.id.tv0:
                setTvResult("0");
                break;
            case R.id.tvdian:
                setTvResult(".");
                break;
            case R.id.tvdel:
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);
                delResult();
                break;
            case R.id.iv_history:
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);
                spAddrUIDlg.performClick();
                break;
            case R.id.tv_day:
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);
                datePickerWithData(tvDay,tvDay.getText().toString());
                break;
            case R.id.tv_jian:
                setTvResult("-");
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);
                break;
            case R.id.tv_jia:
                setTvResult("+");
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);
                break;
            case R.id.tv_ok:
                setTvResult("ok");
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);
                break;
        }
        setColor(view.getId());//设置颜色
    }

    //修改点击后的颜色
    private void setColor(int id) {
        tv1.setBackgroundColor(white);
        tv2.setBackgroundColor(white);
        tv3.setBackgroundColor(white);
        tv4.setBackgroundColor(white);
        tv5.setBackgroundColor(white);
        tv6.setBackgroundColor(white);
        tv7.setBackgroundColor(white);
        tv8.setBackgroundColor(white);
        tv9.setBackgroundColor(white);
        tv0.setBackgroundColor(white);
        tvdian.setBackgroundColor(white);
        tvdel.setBackgroundColor(white);
        tvJia.setBackgroundColor(white);
        tvJian.setBackgroundColor(white);
        switch (id){
            case R.id.tv_jia:
                tvJia.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv_jian:
                tvJian.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv0:
                tv0.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv1:
                tv1.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv2:
                tv2.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv3:
                tv3.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv4:
                tv4.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv5:
                tv5.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv6:
                tv6.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv7:
                tv7.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv8:
                tv8.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tv9:
                tv9.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tvdian:
                tvdian.setBackgroundColor(blue);
                VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
            case R.id.tvdel:
                if (tvResult.getText().toString().equals("")){
                    tvdel.setBackgroundColor(red);
                }else{
                    tvdel.setBackgroundColor(blue);
                    VibratorUtil.Vibrate(mContext, Info.View_Number_Click);break;
                }

        }

    }
    //获取时间
    public String getDate(){
        return tvDay.getText().toString();
    }
    public String getNote(){//获取说明
        return edName.getText().toString();
    }
    //设置说明
    public void setNote(String string){
        edName.setText(string);
        edName.setSelection(string.length());
        spAddrUIDlg.reLoad();
    }
    //获取数量
    public String getNum() {
        return tvResult.getText().toString();
    }
    //清空数量
    public void clearNum() {
        tvResult.setText("");
    }
    //删除
    private void delResult() {
        String s = tvResult.getText().toString();
        if (s.length() > 0) {
            tvResult.setText(s.substring(0, s.length() - 1));
        }
    }
    private String date;
    private int year;
    private int month;
    private int day;
    public String datePickerWithData(final TextView v,String time) {
        if (time.length()==10){
            year = MathUtil.toInt(time.substring(0,4));
            month = MathUtil.toInt(time.substring(5,7))-1;
            day = MathUtil.toInt(time.substring(8,time.length()));
        }
        Lg.e("获取时间1"+year);
        Lg.e("获取时间2"+month);
        Lg.e("获取时间3"+day);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                date = year + "-" + ((month < 9) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                v.setText(date);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
        return date;
    }

}
