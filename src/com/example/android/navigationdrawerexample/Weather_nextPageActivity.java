package com.example.android.navigationdrawerexample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.DB.mydbhelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Weather_nextPageActivity extends Activity {
	TextView tv_city_name1,tv_city_temp2,tv_city_temp3,tv_city_temp4,tv_city_index_d;
	String msggmainfo;
	List<CWeather> clist;
	String city;
	static String weather1,weather2,weather3,weather4;
	static String wind1,wind2,wind3,wind4;
	static String temp1,temp2,temp3,temp4,index_d,st1;
	static String img1,img_title2,img_title3,img_title4;
	static String higher_temp2,lower_temp2;
	static String[] s2;
	static String xianshi2;
	
	static String higher_temp3,lower_temp3;
	static String[] s3;
	static String xianshi3;
	static String higher_temp4,lower_temp4;
	static String[] s4;
	static String xianshi4;
	ImageView img_city_temp2,img_city_temp3,img_city_temp4;
	RelativeLayout temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_next_page);
		subThread();
		tv_city_name1 = (TextView) this.findViewById(R.id.tv_city_name1);
		tv_city_temp2 = (TextView) this.findViewById(R.id.tv_city_temp2);
		tv_city_temp3 = (TextView) this.findViewById(R.id.tv_city_temp3);
		tv_city_temp4 = (TextView) this.findViewById(R.id.tv_city_temp4);
		tv_city_index_d = (TextView) this.findViewById(R.id.tv_city_index_d);
		img_city_temp2 = (ImageView) this.findViewById(R.id.img_city_temp2);
		img_city_temp3 = (ImageView) this.findViewById(R.id.img_city_temp3);
		img_city_temp4 = (ImageView) this.findViewById(R.id.img_city_temp4);
		temp = (RelativeLayout) this.findViewById(R.id.weatherlayout2);
	}//onCreate.

	private void subThread() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mydbhelper dbh = new mydbhelper(Weather_nextPageActivity.this);
				SQLiteDatabase sdb = dbh.getReadableDatabase();
				String cityCode = "";
				String[] table = new String[]{"_id","citycode"};
				Cursor cr = sdb.query(mydbhelper.TABLE_NAME_CITYCODE, table, "_id=?", new String[]{"1"}, null, null, null);
				while(cr.moveToNext()){
					String mainCityCode = cr.getString(1);
					cityCode = mainCityCode;
					System.out.println("mainCityCode    " + mainCityCode);
				}
				String weatherUrl = "http://m.weather.com.cn/data/"+cityCode+".html";
				
				String wgsoninfo = connWeb(weatherUrl);

				Message msg = new Message();
				msg.obj = wgsoninfo;
				whandler.sendMessage(msg);
			}// run.
		}// Runnable.
		).start();
	}// method.

	
	Handler whandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			System.out.println("handleMessage..");
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			msggmainfo = (String) msg.obj;
			System.out.println("main Thread:" + msggmainfo);

			clist = getGsonList(msggmainfo);

			Iterator it = clist.iterator();
			if (it.hasNext()) { 
				CWeather cw = (CWeather) it.next();
				city = cw.getCity();
				temp2 = cw.getTemp2();
				temp3 = cw.getTemp3();
				temp4 = cw.getTemp4();
				weather1 = cw.getWeather1();
				weather2 = cw.getWeather2();
				weather3 = cw.getWeather3();
				weather4 = cw.getWeather4();
				wind2 = cw.getWind2();
				wind3 = cw.getWind3();
				wind4 = cw.getWind4();
				img_title2 = cw.getImg_title2();
				img_title3 = cw.getImg_title3();
				img_title4 = cw.getImg_title4();
				index_d = cw.getIndex_d();
			}// if.
			//将取到的值显示
			//设置字体
			Typeface face = Typeface.createFromAsset(getAssets(),"fonts/1.ttf");   
			tv_city_name1.setTypeface(face);
			tv_city_name1.setText(city);
			Typeface face1 = Typeface.createFromAsset(getAssets(),"fonts/BadbOy.ttf");   
			tv_city_temp2.setTypeface(face1);
			//处理得到的天气1
			s2 = temp2.split("~");
			higher_temp2 = s2[0];
			lower_temp2 = s2[1];
			xianshi2 = higher_temp2+"/"+lower_temp2;
			tv_city_temp2.setText(xianshi2);
			tv_city_temp3.setTypeface(face1);
			tv_city_temp4.setTypeface(face1);
			//处理得到的天气2
			s3 = temp3.split("~");
			higher_temp3 = s3[0];
			lower_temp3 = s3[1];
			xianshi3 = higher_temp3+"/"+lower_temp3;
			tv_city_temp3.setText(xianshi3);
			//处理得到的天气3
			s4 = temp4.split("~");
			higher_temp4 = s4[0];
			lower_temp4 = s4[1];
			xianshi4 = higher_temp4+"/"+lower_temp4;
			tv_city_temp4.setText(xianshi4);
			tv_city_index_d.setTypeface(face);
			tv_city_index_d.setText(index_d);
			System.out.println("拿到的天气图标代号  " + img_title2);
			//第二天天气图标
			System.out.println("翻页获取img2" + img_title2);
			System.out.println("翻页获取img3" + img_title3);
			System.out.println("翻页获取img4" + img_title4);
			if("晴".equals(img_title2)){
				img_city_temp2.setImageResource(R.drawable.sunny);
			}
			else if("多云".equals(img_title2)){
				img_city_temp2.setImageResource(R.drawable.yin);
			}
			else if("小雨".equals(img_title2)){
				img_city_temp2.setImageResource(R.drawable.rainy);
			}
			else if("阴".equals(img_title2)){
				img_city_temp2.setImageResource(R.drawable.yin);
			}
			//第三天天气图标
			if("晴".equals(img_title3)){
				img_city_temp3.setImageResource(R.drawable.sunny);
			}
			else if("多云".equals(img_title3)){
				img_city_temp3.setImageResource(R.drawable.yin);
			}
			else if("小雨".equals(img_title3)){
				img_city_temp3.setImageResource(R.drawable.rainy);
			}
			else if("阴".equals(img_title3)){
				img_city_temp3.setImageResource(R.drawable.yin);
			}
			//第四天天气图标
			if("晴".equals(img_title4)){
				img_city_temp4.setImageResource(R.drawable.sunny);
			}
			else if("多云".equals(img_title4)){
				img_city_temp4.setImageResource(R.drawable.yin);
			}
			else if("小雨".equals(img_title4)){
				img_city_temp4.setImageResource(R.drawable.rainy);
			}
			else if("阴".equals(img_title4)){
				img_city_temp4.setImageResource(R.drawable.yin);
			}
			//根据天气改变页面背景
			if("晴".equals(weather1)){
				Resources resources = getBaseContext().getResources(); 
				Drawable d = resources.getDrawable(R.drawable.red); 
				temp.setBackgroundDrawable(d);
			}
			if("小雨转多云".equals(weather1)){
				Resources resources = getBaseContext().getResources(); 
				Drawable d = resources.getDrawable(R.drawable.chengzongse); 
				temp.setBackgroundDrawable(d);
			}
			if("多云".equals(weather1)){
				Resources resources = getBaseContext().getResources(); 
				Drawable d = resources.getDrawable(R.drawable.huanglan); 
				temp.setBackgroundDrawable(d);
			}
			if("小雨".equals(weather1)){
				Resources resources = getBaseContext().getResources(); 
				Drawable d = resources.getDrawable(R.drawable.bluehuang); 
				temp.setBackgroundDrawable(d);
			}
			if("多云转晴".equals(weather1)){
				Resources resources = getBaseContext().getResources(); 
				Drawable d = resources.getDrawable(R.drawable.chenghuang); 
				temp.setBackgroundDrawable(d);
			}
		}// handlerMessage.
	};// whandler.

	
	private String connWeb(String url) {
		String str = "";
		try {
			System.out.println("connWeb...");
			HttpGet request = new HttpGet(url);
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(response.getEntity());
				System.out.println("response str:" + str);
			}// if.
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}// connWeb.

	public List<CWeather> getGsonList(String msg) {
		List<CWeather> mlist = new ArrayList<CWeather>();

		try {
			JSONObject job = new JSONObject(msg);
			JSONObject wjob = job.getJSONObject("weatherinfo");
			CWeather cw = new CWeather();

			cw.setCityid(wjob.getString("cityid"));
			cw.setCity(wjob.getString("city"));
			cw.setTemp1(wjob.getString("temp1"));
			cw.setTemp2(wjob.getString("temp2"));
			cw.setTemp3(wjob.getString("temp3"));
			cw.setTemp4(wjob.getString("temp4"));
			cw.setWeather1(wjob.getString("weather1"));
			cw.setWeather2(wjob.getString("weather2"));
			cw.setWeather3(wjob.getString("weather3"));
			cw.setWeather4(wjob.getString("weather4"));
			cw.setImg_title1(wjob.getString("img_title1"));
			cw.setImg_title2(wjob.getString("img_title2"));
			cw.setImg_title3(wjob.getString("img_title3"));
			cw.setImg_title4(wjob.getString("img_title4"));
			cw.setWind1(wjob.getString("wind1"));
			cw.setWind2(wjob.getString("wind2"));
			cw.setWind3(wjob.getString("wind3"));
			cw.setWind4(wjob.getString("wind4"));
			cw.setDate_y(wjob.getString("date_y"));
			cw.setSt1(wjob.getString("st1"));
			cw.setSt2(wjob.getString("st2"));
			cw.setSt3(wjob.getString("st3"));
			cw.setSt4(wjob.getString("st4"));
			cw.setFl1(wjob.getString("fl1"));
			cw.setIndex_d(wjob.getString("index_d"));

			mlist.add(cw);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mlist;
	}// method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather_next_page, menu);
		return true;
	}

}//Weather_nextPageActivity
