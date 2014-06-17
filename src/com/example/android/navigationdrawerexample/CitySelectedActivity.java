package com.example.android.navigationdrawerexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.android.DB.mydbhelper;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CitySelectedActivity extends Activity {

	String cityName;
	ListView lv;
	List<String> list;
	ArrayList<HashMap<String,String>> citylist = new ArrayList<HashMap<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_selected);
		//添加城市信息
		mydbhelper dbh = new mydbhelper(CitySelectedActivity.this);
		SQLiteDatabase sdb = dbh.getReadableDatabase();
		ContentValues cv = new ContentValues();
		String city = "";
		//String cityid = "";
		//Toast.makeText(CitySelectedActivity.this,"数据库创建成功" ,1).show();
		String[] table = new String[]{"_id","city", "cityid"};
		Cursor cr = sdb.query(mydbhelper.TABLE_NAME_CITY, table, null, null, null, null, null);
		if(cr != null)
		{
			if(cr.getCount() == 0)
			{
				city = "null";
				//cityid = "null";
			}
		}
		while(cr.moveToNext())
		{
			city = cr.getString(1);
			//cityid = cr.getString(2);
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("city", city);
			citylist.add(map);
		}
		//System.out.println("successful");
		lv = (ListView) this.findViewById(R.id.lv_cityname);
		lv.setVerticalScrollBarEnabled(false);
		
		SimpleAdapter cityadapter = new SimpleAdapter(this, citylist, R.layout.lv_city,
				new String[]{"city"}, new int[]{R.id.tv_cityname});
		lv.setAdapter(cityadapter);
		
		//点击
		AdapterView.OnItemClickListener cityselectedAdapter = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				TextView tv_city = (TextView) view.findViewById(R.id.tv_cityname);
				cityName = tv_city.getText().toString();
				new AlertDialog.Builder(CitySelectedActivity.this)
				.setTitle("确认添加城市？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						mydbhelper dbh = new mydbhelper(CitySelectedActivity.this);
						SQLiteDatabase sdb = dbh.getReadableDatabase();
						ContentValues cv = new ContentValues();
						cv.put("oftenusecity", cityName);
						sdb.insert(mydbhelper.TABLE_NAME_OFTENUSECITY, null, cv);
						System.out.println("添加成功");
						Intent in = new Intent(CitySelectedActivity.this,MainActivity.class);
						startActivity(in);
						CitySelectedActivity.this.finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).create().show();
			}
		};
		lv.setOnItemClickListener(cityselectedAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city_selected, menu);
		return true;
	}

}
