package com.example.android.navigationdrawerexample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.exina.android.dbhelper.DBHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Plans_detail extends Activity{
	TextView tv_title;
	TextView tv_site;
	TextView tv_pdate;
	TextView tv_rtime;
	TextView tv_cdate;
	TextView tv_level;
	EditText ed_title;
	EditText ed_site;
	EditText ed_pdate;
	TimePicker timePicker1;
	EditText ed_level;
	Button tp_button;
	String date;
	int Year,Month,Day;
	int hour, minute;
    static final int TIME_DIALOG_ID = 0;
    Calendar c=Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew);
		ActionBar actionBar = getActionBar(); //获得并隐藏操作栏
		Resources r = getResources();  
		Drawable myDrawable = r.getDrawable(R.drawable.actionbar);  
		actionBar.setBackgroundDrawable(myDrawable); 
		actionBar.setTitle("出行事件详细"); 
		tv_title=(TextView) findViewById(R.id.tv_title1);
	    tv_site=(TextView) findViewById(R.id.tv_site);
	    tv_pdate=(TextView) findViewById(R.id.tv_pdate);
	    tv_rtime=(TextView) findViewById(R.id.tv_rtime);
	    tv_cdate=(TextView) findViewById(R.id.tv_ctime);
	    tv_level=(TextView) findViewById(R.id.tv_level);
	    tp_button=(Button) findViewById(R.id.tp_button);
	    //获得意图
	    Intent intent = getIntent();
	    Bundle bu = intent.getExtras();
	    Year=bu.getInt("Year");
	    Month=bu.getInt("Month");
	    Day=bu.getInt("Day");
		ed_title=(EditText) findViewById(R.id.ed_title);
	    ed_site=(EditText) findViewById(R.id.ed_site);
	    ed_pdate=(EditText) findViewById(R.id.ed_pdate);
	    ed_level=(EditText) findViewById(R.id.ed_level);
	    date=bu.getString("current_date");
	    tv_cdate.setText(date);
		
		
	    ed_title.setText(bu.getString("title"));
	    ed_site.setText(bu.getString("site"));
	    ed_pdate.setText(bu.getString("plan_date"));
	    ed_level.setText(bu.getString("level"));
	    int uid=bu.getInt("uid");
	
		
	    timePicker1=(TimePicker) findViewById(R.id.timePicker1);
		timePicker1.setIs24HourView(true);//设置为24小时格式
		timePicker1.setVisibility(View.GONE);
		//设置提醒时间按钮事件
        tp_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
		});
				
		
	   
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addnew_top_menu, menu);
		return true;
	}
	//将修改后的信息在数据库中更新
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		    ed_title=(EditText) findViewById(R.id.ed_title);
		    ed_site=(EditText) findViewById(R.id.ed_site);
		    ed_pdate=(EditText) findViewById(R.id.ed_pdate);
		    ed_level=(EditText) findViewById(R.id.ed_level);
		    DBHelper myDBHelper=new DBHelper(Plans_detail.this);
		    SQLiteDatabase sdb=myDBHelper.getReadableDatabase();
			
			ContentValues cv=new ContentValues();
			String p_title=ed_title.getText().toString();
			if(p_title.toString().equals("")) {
				Toast.makeText(Plans_detail.this, "主题不能为空", 1).show();
				return false;
			}
			
			String p_site=ed_site.getText().toString();
			String p_pdate=ed_pdate.getText().toString();
			String p_level=ed_level.getText().toString();
			c.set(Year, Month, Day, hour, minute);
			// 进行闹铃注册   
			Intent alarmIntent = new Intent(Plans_detail.this, AlarmReceiver.class);  
			alarmIntent.putExtra("msg", p_title);
			PendingIntent sender = PendingIntent.getBroadcast(Plans_detail.this, 0, alarmIntent, 0); 
			//获取AlarmManager对象
			AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);  
			//设置提醒时间
			manager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), sender);
			
			String r_time=date+"-"+hour+"-"+minute;
			cv.put("title", p_title);
			cv.put("site",p_site);
			cv.put("plan_date", p_pdate);
			cv.put("current_date", date);
			cv.put("remind_time",r_time);
			cv.put("level", p_level);
			sdb.update("plans", cv, null, null);
			Toast.makeText(Plans_detail.this, "修改出行计划成功", 1).show();
			
			Intent in=new Intent(Plans_detail.this,OutPlan.class);
			in.addFlags(in.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle bu =new Bundle();
			bu.putString("date", date);
			in.putExtras(bu);
			startActivity(in);
			Plans_detail.this.finish();
			return true;
	}
    

	 @Override
     protected Dialog onCreateDialog(int id)
     {
          switch (id) {
          case TIME_DIALOG_ID:
               return new TimePickerDialog(
                       this, mTimeSetListener, hour, minute, false);
          }
          return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
    new TimePickerDialog.OnTimeSetListener()
    {
         public void onTimeSet(
                 TimePicker view, int hourOfDay, int minuteOfHour)
         {
              hour = hourOfDay;
              minute = minuteOfHour;
           
              SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
              Date date = new Date(0,0,0, hour, minute);
              String strDate = timeFormat.format(date);
           
              Toast.makeText(getBaseContext(),
                       "You have selected " + strDate,
                       Toast.LENGTH_SHORT).show();           
         }
    };

    public void onClick(View view) {
         Toast.makeText(getBaseContext(),
                   "Remind time is " +
                          timePicker1.getCurrentHour() +
                           ":" + timePicker1.getCurrentMinute(),
                          Toast.LENGTH_SHORT).show();
    }  

}
