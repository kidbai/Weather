package com.example.android.navigationdrawerexample;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.exina.android.dbhelper.DBHelper;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Addnew extends Activity {
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
	Button tp_button;
	EditText ed_level;
	String p_title;
	String date;
	int Year,Month,Day;
	int hour, minute;
    static final int TIME_DIALOG_ID = 0;
    Calendar c=Calendar.getInstance();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew);
		ActionBar actionBar = getActionBar(); //获得并隐藏操作栏
		//actionBar.hide();
		Resources r = getResources();  
		Drawable myDrawable = r.getDrawable(R.drawable.actionbar);  //为操作栏增加背景图片
		actionBar.setBackgroundDrawable(myDrawable); 
		actionBar.setTitle("新增出行事件"); 
		//获取日期
		Intent in =getIntent();
	    Bundle bu = new Bundle();
	    bu = in.getExtras();
	    date=bu.getString("date");
	    Year=bu.getInt("Year");
	    Month=bu.getInt("Month");
	    Day=bu.getInt("Day");
	    
	    tv_title=(TextView) findViewById(R.id.tv_title1);
	    tv_site=(TextView) findViewById(R.id.tv_site);
	    tv_pdate=(TextView) findViewById(R.id.tv_pdate);
	    tv_rtime=(TextView) findViewById(R.id.tv_rtime);
	    tv_cdate=(TextView) findViewById(R.id.tv_ctime);
	    tv_level=(TextView) findViewById(R.id.tv_level);
	    ed_pdate=(EditText) findViewById(R.id.ed_pdate);
	    tp_button=(Button) findViewById(R.id.tp_button);
	    ed_pdate.setText(date);
		tv_cdate.setText(date);
		
		timePicker1=(TimePicker) findViewById(R.id.timePicker1);
		timePicker1.setIs24HourView(true);//设置为24小时格式
		timePicker1.setVisibility(View.GONE);
		//设置添加提醒时间按钮事件
		tp_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//显示隐藏的timepicker
				showDialog(TIME_DIALOG_ID);
			}
		});
		
	}
	
   //设置上方的菜单栏
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_top_menu, menu);
		return true;
	}
	//设置添加按钮触发后事件，将信息插入数据库
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		    ed_title=(EditText) findViewById(R.id.ed_title);
		    ed_site=(EditText) findViewById(R.id.ed_site);
		   
		    //ed_rtime=(EditText) findViewById(R.id.ed_rtime);
		    ed_level=(EditText) findViewById(R.id.ed_level);
		    DBHelper myDBHelper=new DBHelper(Addnew.this);
		    SQLiteDatabase sdb=myDBHelper.getReadableDatabase();
			
			ContentValues cv=new ContentValues();
			//判断主题不能为空
			p_title=ed_title.getText().toString();
			if(p_title.toString().equals("")) {
				Toast.makeText(Addnew.this, "主题不能为空", 1).show();
				return false;
			}
			
			String p_site=ed_site.getText().toString();
			String p_pdate=ed_pdate.getText().toString();
			String p_level=ed_level.getText().toString();
			System.out.println(Year+":"+Month+":"+Day);
			System.out.println(hour+":"+minute);
			
			
			String r_time=date+"-"+hour+"-"+minute;
			cv.put("title", p_title);
			cv.put("site",p_site);
			cv.put("plan_date", p_pdate);
			cv.put("remind_time",r_time);
			cv.put("current_date", date);
			cv.put("level", p_level);
			sdb.insert(myDBHelper.TABLE_NAME, null, cv);
			Toast.makeText(Addnew.this, "新建出行计划成功", 1).show();
			
			Intent in=new Intent(Addnew.this,OutPlan.class);
			in.addFlags(in.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle bu =new Bundle();
			bu.putString("date", date);
			in.putExtras(bu);
			startActivity(in);
			Addnew.this.finish();
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

	 //设置提醒时间确定按钮点击后事件
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
              c.set(Year, Month, Day, hour, minute);
  			// 进行闹铃注册   
  			Intent alarmIntent = new Intent(Addnew.this, AlarmReceiver.class);  
  			alarmIntent.putExtra("msg", p_title);
  			PendingIntent sender = PendingIntent.getActivity(Addnew.this, 0, alarmIntent, 0);
  			//获取AlarmManager对象
  			AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);  
  			//设置提醒时间
  			manager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), sender);
           
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
