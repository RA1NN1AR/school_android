package com.example.schoolapp_android.Son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.schoolapp_android.R;

import java.util.ArrayList;

import htmlservice.check_kebiao;
import htmlservice.check_kecheng;
import javabean.JavaBean;

public class kebiao_activity extends AppCompatActivity {
    private final int WC = 50;
    private final int MP = ViewGroup.LayoutParams.WRAP_CONTENT;
    TableLayout tableLayout;
    private String user;
    private ArrayList<JavaBean> kebiao;
    private ArrayList<JavaBean> kecheng;
    private String zhouci;
    private String xueqi;
    private Spinner yearSpinner;
    private ArrayAdapter<String> adapter = null;
    private static final String[] yearSeason ={"2020年第一学期","2020年第二学期","2021年第一学期","2021年第二学期","2022年第一学期","2022年第二学期"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kebiao);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("课程表");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        String[] weekly = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        tableLayout = (TableLayout) findViewById(R.id.table1);
        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        int ci=0;
        for (int i = 1; i <= 12; i++) {

            TableRow tableRow = new TableRow(kebiao_activity.this);
            for (int j = 1; j <= 8; j++) {
                ci++;
                TextView text = new TextView(getApplicationContext());
                text.setGravity(Gravity.CENTER);
                text.setHeight(150);
                text.setWidth(50);
                text.setId(ci);

                if (j == 1 && i != 1) {
                    text.setText(String.valueOf(i - 1));
                } else if (j > 1 && i == 1 && j < 9) {
                    text.setText(weekly[j - 2]);
                }
                tableRow.addView(text);
            }
            //新建的TableRow添加到TableLayout
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(WC, MP,1));
        }
        zhouci="1";//TODO:周次
//        xueqi="2019-2020第二学期"; //TODO:学期(已替换成数组yearSeason)
        new thread_valiUser().execute();//TODO:生成方法

        yearSpinner=(Spinner)findViewById(R.id.snipper_Year);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, yearSeason);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setVisibility(View.VISIBLE);
//        yearSpinner.setOnItemClickListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
//                //TODO:刷新
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class thread_valiUser extends AsyncTask<Void,String, ArrayList<JavaBean>> {


        @Override
        protected ArrayList<JavaBean> doInBackground(Void... voids) {
            check_kebiao a=new check_kebiao();

            return a.check_kebiao(user);
        }

        @Override
        protected void onPostExecute(ArrayList<JavaBean> javaBeans) {
            super.onPostExecute(javaBeans);
            kebiao=javaBeans;
            new thread_valiUser2().execute();
        }
    }
    private class thread_valiUser2 extends AsyncTask<Void,String, ArrayList<JavaBean>> {


        @Override
        protected ArrayList<JavaBean> doInBackground(Void... voids) {
            check_kecheng a=new check_kecheng();

            return a.check_kecheng(user);
        }

        @Override
        protected void onPostExecute(ArrayList<JavaBean> javaBeans) {
            super.onPostExecute(javaBeans);
            kecheng=javaBeans;
            int week=0;
            int start=0;
            int end=0;
            String address="";
            String classname="";
            for(int n=0;n<kebiao.size();n++) {
                if(kebiao.get(n).CoA_Semester.equals(xueqi)&&kebiao.get(n).CoA_weekly.equals(zhouci)){
                    //获取第几周的课
                    if(kebiao.get(n).CoA_week==null){
                        for(int i=0;i<kecheng.size();i++){

                            if(kecheng.get(i).Coi_id.equals(kebiao.get(n).Coi_id)){
                                week=Integer.parseInt(kecheng.get(i).Coi_week);
                            }
                        }
                    }else{
                        week=Integer.parseInt(kebiao.get(n).CoA_week);
                    }
                    //获取开始节次
                    if(kebiao.get(n).CoA_start==null){
                        for(int i=0;i<kecheng.size();i++){

                            if(kecheng.get(i).Coi_id.equals(kebiao.get(n).Coi_id)){
                                start=Integer.parseInt(kecheng.get(i).Coi_start);
                            }
                        }
                    }else{
                        start=Integer.parseInt(kebiao.get(n).CoA_start);
                    }
                    //获取结束节次
                    if(kebiao.get(n).CoA_end==null){
                        for(int i=0;i<kecheng.size();i++){
                            if(kecheng.get(i).Coi_id.equals(kebiao.get(n).Coi_id)){
                                end=Integer.parseInt(kecheng.get(i).Coi_end);
                            }
                        }
                    }else{
                        end=Integer.parseInt(kebiao.get(n).CoA_end);
                    }
                    //获取课程名
                    for(int i=0;i<kecheng.size();i++){
                        if(kecheng.get(i).Coi_id.equals(kebiao.get(n).Coi_id)){
                            classname=kecheng.get(i).Coi_name;
                        }
                    }
                    //规划课程名
                    if(classname.length()>6){
                        classname=classname.substring(0,5)+"...";
                    }
                    //获取上课地点
                    if(kebiao.get(n).CoA_address==null){
                        for(int i=0;i<kecheng.size();i++){

                            if(kecheng.get(i).Coi_id.equals(kebiao.get(n).Coi_id)){
                                address=kecheng.get(i).Coi_address;
                            }
                        }
                    }else{
                        address=kebiao.get(n).CoA_address;
                    }
                    System.out.println(week+":"+start+":"+end);
                    boolean bol=true;
                    //第一个值为————行  第二个值为————列
//
                    for (int i = 0;i<7; i++) { //列
                        for (int j = 0;j<tableLayout.getChildCount(); j++) {//行
                            if (i==week&&j>=start&&j<=end){
                                int c= j*8+i+1;
                                if(bol){
                                    TextView textView;
                                    TextView textView1;
                                    textView1=findViewById(c+8);
                                    textView=findViewById(c);
                                    if(address!="0"){
                                        textView1.setText(address);
                                        textView1.setTextSize(11f);
                                        textView1.setTextColor(Color.rgb(255,255,255));
                                        TextPaint tp=textView1.getPaint();
                                        tp.setFakeBoldText(true);
                                    }
                                    textView.setText(classname);
                                    textView.setTextSize(11f);
                                    textView.setTextColor(Color.rgb(255,255,255));
                                    TextPaint tp=textView.getPaint();
                                    tp.setFakeBoldText(true);
                                    bol=false;
                                }

                                ((TableRow) tableLayout.getChildAt(j)).getChildAt(i).setBackgroundColor(Color.rgb(0, 150, 255));
                            }


                        }//j

                    }//i
                }else{

                }

                }//over
            }

        }
    }
